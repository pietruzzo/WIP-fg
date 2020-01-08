package shared.antlr4.pattern.patternSubParsers;

import master.PatternCallback;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternParser;
import shared.patterns.Partition;
import shared.patterns.Selection;
import shared.patterns.Trigger;
import shared.selection.PartitioningSolver;
import shared.selection.SelectionSolver;

public class PartitionParser extends PatternBaseListener {

    private PartitioningSolver partitioningSolver;


    public static Partition getSelectionSolver (shared.antlr4.pattern.PatternParser.PartitionContext ctx, Trigger trigger, PatternCallback callback) {

        PartitionParser partitionParser = new PartitionParser();
        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(partitionParser, ctx);

        Partition partition = new Partition(
                trigger,
                null,
                callback,
                partitionParser.partitioningSolver
        );

        return partition;
    }

    /**
     * constructor for PartitioningSolver
     */
    @Override public void enterPartition(shared.antlr4.pattern.PatternParser.PartitionContext ctx) {
        if (ctx.getText().contains(".GroupE(")) {
            this.partitioningSolver = new PartitioningSolver(true);
        } else {
            this.partitioningSolver = new PartitioningSolver(false);
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Partition (GPattern)
     */
    @Override public void exitPartition(shared.antlr4.pattern.PatternParser.PartitionContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Partition
     */
    @Override public void enterPartitionFunction(shared.antlr4.pattern.PatternParser.PartitionFunctionContext ctx) {

        if (ctx.temporalVariable() != null){
            ctx.temporalVariable().stream().forEach(vCtx -> {
                Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar = CommonsParser.getTemporalVar(vCtx);
                this.partitioningSolver.addElement(new PartitioningSolver.Element(
                        temporalVar.f0,
                        SelectionSolver.Operation.Type.VARIABLE,
                        temporalVar.f1,
                        temporalVar.f2
                ));
            });
        }

        if (ctx.label() != null) {
            ctx.label().stream().forEach(lCtx -> {
                String label = CommonsParser.getLabel(lCtx);
                this.partitioningSolver.addElement(new PartitioningSolver.Element(
                        label,
                        SelectionSolver.Operation.Type.LABEL,
                        null,
                        null
                ));
            });
        }
    }
}
