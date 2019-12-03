package shared.antlr4.pattern.patternSubParsers;

import computationImpl.OperationImplementations;
import master.PatternCallback;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternParser;
import shared.patterns.Stream;
import shared.patterns.Trigger;
import shared.selection.SelectionSolver;
import shared.streamProcessing.CustomBinaryOperator;
import shared.streamProcessing.Operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamParser extends PatternBaseListener {

    private List<Operations> operations;
    private String fireNotification;
    private AtomicInteger transactionId;
    private String genVar = null;

    public StreamParser() {
        this.operations = new ArrayList<>();
        this.fireNotification = null;
        this.transactionId = new AtomicInteger(new Random().nextInt());
    }

    public static Stream getStream (shared.antlr4.pattern.PatternParser.ExtractStreamProcessingContext ctx, Trigger trigger, PatternCallback callback) {
        StreamParser streamParser = new StreamParser();
        streamParser.enterExtractStreamProcessing(ctx);

        Stream stream = new Stream(
                trigger,
                streamParser.genVar,
                callback,
                streamParser.operations,
                streamParser.fireNotification
        );

        return stream;
    }

    public static Stream getStream (shared.antlr4.pattern.PatternParser.CollectStreamProcessingContext ctx, Trigger trigger, PatternCallback callback) {
        StreamParser streamParser = new StreamParser();
        streamParser.enterCollectStreamProcessing(ctx);

        Stream stream = new Stream(
                trigger,
                streamParser.genVar,
                callback,
                streamParser.operations,
                streamParser.fireNotification
        );

        return stream;
    }

    /**
     * Add StreamVariable
     */
    @Override public void enterCollectStreams(shared.antlr4.pattern.PatternParser.CollectStreamsContext ctx) {
        ctx.temporalVariable()
                .forEach(tctx -> {
                    Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar =
                            CommonsParser.getTemporalVar(tctx);
                    this.operations.add(new Operations.StreamVariable(temporalVar.f0, temporalVar.f1, temporalVar.f2));
                });
    }

    /**
     * Add extraction
     */
    @Override public void enterExtraction(shared.antlr4.pattern.PatternParser.ExtractionContext ctx) {

        //Get label names
        List<String> labelNames = new ArrayList<>();
        ctx.label().forEach(lctx-> {
            labelNames.add(CommonsParser.getLabel(lctx));
        });

        //Add extract operation
        if (ctx.getText().contains(".extractV(")){
            this.operations.add(new Operations.Extract(labelNames.toArray(String[]::new), false));
        } else if (ctx.getText().contains(".extractE(")){
            this.operations.add(new Operations.Extract(labelNames.toArray(String[]::new), true));
        } else {
            throw new RuntimeException(ctx.getText() + " unrecognized, it should contains .extractV( or .extractE(");
        }
    }

    /**
     * Add Evaluation
     */
    @Override public void enterEvaluation(shared.antlr4.pattern.PatternParser.EvaluationContext ctx) {

        SelectionSolver.Operation.Operator operator =
                SelectionSolver.Operation.Operator.valueOf(ctx.Operator().getText());

        String value = CommonsParser.getValue(ctx.value());

        String fireEvent = ctx.fireEvent().Litterals().getText();

        Long transactionId = Long.valueOf(this.transactionId.getAndIncrement());

        this.operations.add(new Operations.Evaluate(operator, transactionId, value, fireEvent));


    }

    /**
     * Add operation
     */
    @Override public void enterOperationFunction(shared.antlr4.pattern.PatternParser.OperationFunctionContext ctx) {

        String[] fields = new String[0];

        //Get fields
        if (ctx.tupleField() != null) {
           fields = ctx.tupleField()
                    .stream()
                    .map(field -> field.getText())
                    .collect(Collectors.toList())
                    .toArray(String[]::new);
        }

        //Get Registerd Operation
        if (ctx.getText().startsWith("map(")){
            Function<Tuple, Tuple> map = OperationImplementations.getMap(ctx.functionName().getText());
            this.operations.add(new Operations.Map(map));
        } else if (ctx.getText().startsWith("flatmap(")){
            Function<Tuple, java.util.stream.Stream<Tuple>> map = OperationImplementations.getFlatMap(ctx.functionName().getText());
            this.operations.add(new Operations.FlatMap(map));
        } else if (ctx.getText().startsWith("reduce(")){
            Tuple2<CustomBinaryOperator, Tuple> reduce = OperationImplementations.getReduce(ctx.functionName().getText());
            this.operations.add(new Operations.Reduce(reduce.f1, reduce.f0, Long.valueOf(this.transactionId.getAndIncrement()), fields));
        } else if (ctx.getText().startsWith("filter(")) {
            Predicate<Tuple> filter = OperationImplementations.getFilter(ctx.functionName().getText());
            this.operations.add(new Operations.Filter(filter));
        } else if (ctx.oneFieldOperationAlias() != null) {
            //Aliasing function
            if (ctx.oneFieldOperationAlias().getText().contains("avg")) {
                Tuple2<CustomBinaryOperator, Tuple> reduce = OperationImplementations.getReduce("avg");
                this.operations.add(new Operations.Reduce(reduce.f1, reduce.f0, Long.valueOf(this.transactionId.getAndIncrement()), fields));

            }
            else if (ctx.oneFieldOperationAlias().getText().contains("max")) {
                Tuple2<CustomBinaryOperator, Tuple> reduce = OperationImplementations.getReduce("max");
                this.operations.add(new Operations.Reduce(reduce.f1, reduce.f0, Long.valueOf(this.transactionId.getAndIncrement()), fields));

            }
            else if (ctx.oneFieldOperationAlias().getText().contains("min")) {
                Tuple2<CustomBinaryOperator, Tuple> reduce = OperationImplementations.getReduce("min");
                this.operations.add(new Operations.Reduce(reduce.f1, reduce.f0, Long.valueOf(this.transactionId.getAndIncrement()), fields));

            }
            else if (ctx.oneFieldOperationAlias().getText().contains("count")) {
                Tuple2<CustomBinaryOperator, Tuple> reduce = OperationImplementations.getReduce("count");
                this.operations.add(new Operations.Reduce(reduce.f1, reduce.f0, Long.valueOf(this.transactionId.getAndIncrement()), fields));

            }
            else if (ctx.oneFieldOperationAlias().getText().contains("select")) {
                Function<Tuple, Tuple> map = OperationImplementations.getMap("select");
                this.operations.add(new Operations.Map(map));
            }
        } else if (ctx.getText().startsWith("groupby")) {
            this.operations.add(new Operations.GroupBy(fields));
        } else if (ctx.getText().startsWith("Merge")) {
            this.operations.add(new Operations.Merge(fields));
        } else if (ctx.getText().startsWith("collect")) {
            this.operations.add(new Operations.Collect());
        }
    }

     /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Stream
     */
    @Override public void enterEmission(shared.antlr4.pattern.PatternParser.EmissionContext ctx) {

        Long transactionId = Long.valueOf(this.transactionId.getAndIncrement());

        String varName = CommonsParser.getVarName(ctx.variable());

        this.genVar = varName;

        //Get Root
        ParserRuleContext root = ctx;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        long persistence= CommonsParser.getMaxTemporalWindow((PatternParser.PatternEntryContext) root, varName);
        this.operations.add(new Operations.Emit(varName, persistence+1, transactionId));

    }

}
