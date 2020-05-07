package shared.antlr4.pattern.patternSubParsers;

import master.PatternCallback;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternParser;
import shared.computation.ComputationParametersImpl;
import shared.patterns.Computation;
import shared.patterns.Trigger;
import shared.selection.SelectionSolver;

import java.util.List;
import java.util.stream.Collectors;

public class ComputationParser extends PatternBaseListener {

    private Trigger trigger;
    private PatternCallback callback;
    private Computation computation;
    private String functionName;
    private ComputationParametersImpl computationParameters;

    private ComputationParser(Trigger trigger, PatternCallback callback) {
        this.trigger = trigger;
        this.callback = callback;
    }

    public static Computation getComputation (shared.antlr4.pattern.PatternParser.ComputationContext ctx, Trigger trigger, PatternCallback callback) {

        //Entry point is computation context ctx

        //Walk it and attach listener
        ParseTreeWalker walker = new ParseTreeWalker();
        ComputationParser computationParser = new ComputationParser(trigger, callback);
        walker.walk(computationParser, ctx);

        return computationParser.computation;
    }

    /**
     * Entrypoint to ComputationTree parsing
     * @param ctx computation subTree
     */
    @Override public void enterComputation(shared.antlr4.pattern.PatternParser.ComputationContext ctx) {
        this.computationParameters = new ComputationParametersImpl();
    }
    /**
     * Generate pattern.Computation object
     */
    @Override public void exitComputationFunction(shared.antlr4.pattern.PatternParser.ComputationFunctionContext ctx) {

        this.computation = new Computation(this.trigger, null, this.callback);
        this.computation.setComputation(this.functionName, this.computationParameters);

    }

    /**
     * Get return variableNames
     * @param ctx
     */
    @Override public void enterComputationReturnVariables(PatternParser.ComputationReturnVariablesContext ctx){

        //Get Root
        ParserRuleContext root = ctx;
        while (root.getParent()!=null) {
            root = root.getParent();
        }
        PatternParser.PatternEntryContext fRoot = (PatternParser.PatternEntryContext)root;
        // Name of target label
        List<Tuple2<String, Long>> returnVariablesNames;
        returnVariablesNames = ctx.variable()
                .stream()
                .map(ctxVar -> CommonsParser.getVarName(ctxVar))
                .map(var -> new Tuple2<>(var, CommonsParser.getMaxTemporalWindow(fRoot, var)))
                .collect(Collectors.toList());

        //Add return Variables to Computation Parameters
        computationParameters.setReturnVarNames(returnVariablesNames);
    }

    /**
     * @param ctx
     */
    @Override public void enterComputationParameters(PatternParser.ComputationParametersContext ctx){
    }

    @Override public void enterAliasedParameter(PatternParser.AliasedParameterContext ctx){

        if (ctx.operands().value() != null){
            String value = CommonsParser.getValue(ctx.operands().value());
            this.computationParameters.put(
                    ctx.Litterals().getText(),
                    new ComputationParametersImpl.Parameter(value)
            );
        }
        else if (ctx.operands().label() != null){
            String label = CommonsParser.getLabel(ctx.operands().label());
            this.computationParameters.put(
                    ctx.Litterals().getText(),
                    new ComputationParametersImpl.Parameter(label, 1, null, null)
            );
        }
        else if (ctx.operands().temporalVariable() != null){
            Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar = CommonsParser.getTemporalVar(ctx.operands().temporalVariable());
            this.computationParameters.put(
                    ctx.Litterals().getText(),
                    new ComputationParametersImpl.Parameter(temporalVar.f0, 2, temporalVar.f1, temporalVar.f2)
            );
        }
    }

    @Override public void enterFunctionName(shared.antlr4.pattern.PatternParser.FunctionNameContext ctx) {
        this.functionName = ctx.Litterals().getText();
    }

}
