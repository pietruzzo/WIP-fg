package shared.antlr4.pattern.patternSubParsers;

import master.PatternCallback;
import shared.antlr4.pattern.PatternBaseListener;
import shared.patterns.Computation;
import shared.patterns.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComputationParser extends PatternBaseListener {

    private Trigger trigger;
    private PatternCallback callback;
    private Computation computation;
    private String functionName;

    private ComputationParser(Trigger trigger, PatternCallback callback) {
        this.trigger = trigger;
        this.callback = callback;
    }

    public static Computation getComputation (shared.antlr4.pattern.PatternParser.ComputationContext ctx, Trigger trigger, PatternCallback callback) {
        ComputationParser computationParser = new ComputationParser(trigger, callback);
        computationParser.enterComputation(ctx);
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

        // Name of target label
        List<String> returnLabelNames = ctx.label()
                .stream()
                .map(ctxLab -> CommonsParser.getLabel(ctxLab))
                .collect(Collectors.toList());

        //List of Parameters
        List<String> parameters = ctx.value()
                .stream()
                .map(ctxVal -> CommonsParser.getValue(ctxVal))
                .collect(Collectors.toList());

        this.computation = new Computation(this.trigger, null, this.callback);
        this.computation.setComputation(this.functionName, returnLabelNames, parameters);
    }

    @Override public void enterFunctionName(shared.antlr4.pattern.PatternParser.FunctionNameContext ctx) {
        this.functionName = ctx.Litterals().getText();
    }

}
