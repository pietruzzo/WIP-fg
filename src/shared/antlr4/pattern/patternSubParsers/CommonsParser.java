package shared.antlr4.pattern.patternSubParsers;

import master.PatternCallback;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.Utils;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternParser;
import shared.patterns.Computation;
import shared.patterns.Trigger;
import shared.selection.SelectionSolver;
import shared.variables.solver.VariableSolver;

import java.util.HashMap;
import java.util.Map;

public class CommonsParser extends PatternBaseListener {

    private String string;
    private Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar;
    private Map<String, Long> varPersistance = new HashMap<>();

    //Overwrite to make private
    private CommonsParser() {}

    public static String getLabel(shared.antlr4.pattern.PatternParser.LabelContext ctx) {
        CommonsParser commonsParser = new CommonsParser();
        commonsParser.enterLabel(ctx); //Write in string
        return commonsParser.string;
    }
    public static String getValue(shared.antlr4.pattern.PatternParser.ValueContext ctx) {
        CommonsParser commonsParser = new CommonsParser();
        commonsParser.enterValue(ctx); //Write in string
        return commonsParser.string;
    }
    public static String getVarName(shared.antlr4.pattern.PatternParser.VariableContext ctx) {
        CommonsParser commonsParser = new CommonsParser();
        commonsParser.enterVariable(ctx); //Write in string
        return commonsParser.string;
    }
    public static Long getMaxTemporalWindow(PatternParser.PatternEntryContext root, String varName) {
        CommonsParser commonsParser = new CommonsParser();
        commonsParser.enterPatternEntry(root);
        return commonsParser.varPersistance.get(varName);
    }
    public static Tuple3<String, String, SelectionSolver.Operation.WindowType> getTemporalVar(shared.antlr4.pattern.PatternParser.TemporalVariableContext ctx) {
        CommonsParser commonsParser = new CommonsParser();
        commonsParser.enterTemporalVariable(ctx); //Write in string
        return commonsParser.temporalVar;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void enterBoolPredicate(shared.antlr4.pattern.PatternParser.BoolPredicateContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void exitBoolPredicate(shared.antlr4.pattern.PatternParser.BoolPredicateContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void enterTemporalVariable(shared.antlr4.pattern.PatternParser.TemporalVariableContext ctx) {
        String name = ctx.variable().Litterals().getText();
        String timeUnits = "0s";
        SelectionSolver.Operation.WindowType wType = SelectionSolver.Operation.WindowType.AGO;

        if ( ctx.Timeunit() != null) {
            timeUnits = ctx.Timeunit().getText();
        }
        long timeWindow = Utils.solveTime(timeUnits);

        this.varPersistance.computeIfPresent(name, (k, v) -> Math.max(timeWindow, v));
        this.varPersistance.putIfAbsent(name, timeWindow);

        if (ctx.getText().replaceFirst(ctx.variable().getText(), "").contains("every")){
            wType = SelectionSolver.Operation.WindowType.EVERYWITHIN;
        } else if (ctx.getText().replaceFirst(ctx.variable().getText(), "").contains("within")) {
            wType = SelectionSolver.Operation.WindowType.WITHIN;
        }

        this.temporalVar = new Tuple3<>(name, timeUnits, wType);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void exitTemporalVariable(shared.antlr4.pattern.PatternParser.TemporalVariableContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void enterLabel(shared.antlr4.pattern.PatternParser.LabelContext ctx) {
        string = ctx.Litterals().getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    //@Override public void exitLabel(shared.antlr4.pattern.PatternParser.LabelContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void enterValue(shared.antlr4.pattern.PatternParser.ValueContext ctx) {
        string = ctx.Litterals().getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    //@Override public void exitValue(shared.antlr4.pattern.PatternParser.ValueContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    @Override public void enterVariable(shared.antlr4.pattern.PatternParser.VariableContext ctx) {
        string = ctx.Litterals().getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p> -> Common
     */
    //@Override public void exitVariable(shared.antlr4.pattern.PatternParser.VariableContext ctx) { }
}
