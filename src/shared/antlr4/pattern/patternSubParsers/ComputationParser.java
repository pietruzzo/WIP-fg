package shared.antlr4.pattern.patternSubParsers;

import master.PatternCallback;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.antlr4.GPatternParser;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternParser;
import shared.computation.ComputationParameters;
import shared.patterns.Computation;
import shared.patterns.Trigger;
import shared.selection.SelectionSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComputationParser extends PatternBaseListener {

    private Trigger trigger;
    private PatternCallback callback;
    private Computation computation;
    private String functionName;
    private ComputationParameters computationParameters;
    private List<String> returnVariablesNames;

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
    @Override public void enterComputation(shared.antlr4.pattern.PatternParser.ComputationContext ctx) { }
    /**
     * Generate pattern.Computation object
     */
    @Override public void exitComputationFunction(shared.antlr4.pattern.PatternParser.ComputationFunctionContext ctx) {

        this.computation = new Computation(this.trigger, null, this.callback);
        this.computation.setComputation(this.functionName, this.returnVariablesNames, this.computationParameters);

    }

    /**
     * Get return variableNames
     * @param ctx
     */
    @Override public void enterComputationReturnVariables(PatternParser.ComputationReturnVariablesContext ctx){
        // Name of target label
        this.returnVariablesNames = ctx.variable()
                .stream()
                .map(ctxVar -> CommonsParser.getVarName(ctxVar))
                .collect(Collectors.toList());
    }

    /**
     * Get initialize parameters
     * @param ctx
     */
    @Override public void enterComputationParameters(PatternParser.ComputationParametersContext ctx){
        this.computationParameters = new ComputationParameters();
    }

    @Override public void enterAliasedParameter(PatternParser.AliasedParameterContext ctx){

        if (ctx.operands().value() != null){
            String value = CommonsParser.getValue(ctx.operands().value());
            this.computationParameters.put(
                    ctx.Litterals().getText(),
                    new ComputationParameters.Parameter(value)
            );
        }
        else if (ctx.operands().label() != null){
            String label = CommonsParser.getLabel(ctx.operands().label());
            this.computationParameters.put(
                    ctx.Litterals().getText(),
                    new ComputationParameters.Parameter(label, false, true, null, null)
            );
        }
        else if (ctx.operands().temporalVariable() != null){
            Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar = CommonsParser.getTemporalVar(ctx.operands().temporalVariable());
            this.computationParameters.put(
                    ctx.Litterals().getText(),
                    new ComputationParameters.Parameter(temporalVar.f0, false, false, temporalVar.f1, temporalVar.f2)
            );
        }
    }

    @Override public void enterFunctionName(shared.antlr4.pattern.PatternParser.FunctionNameContext ctx) {
        this.functionName = ctx.Litterals().getText();
    }

}
