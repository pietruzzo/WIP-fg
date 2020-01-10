package shared.antlr4.pattern.patternSubParsers;

import akka.japi.Pair;
import master.PatternCallback;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternParser;
import shared.patterns.Selection;
import shared.patterns.Trigger;
import shared.selection.SelectionSolver;

public class SelectionParser extends PatternBaseListener {


    private SelectionSolver selectionSolver;


    private SelectionParser() {
        selectionSolver = new SelectionSolver();
    }

    public static Selection getSelectionSolver (shared.antlr4.pattern.PatternParser.SelectionContext ctx, Trigger trigger, PatternCallback callback) {
        SelectionParser selectionSolver = new SelectionParser();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(selectionSolver, ctx);

        Selection selection = new Selection(
                trigger,
                null,
                callback,
                selectionSolver.selectionSolver
        );

        return selection;
    }


    /**
     * Place token EDGE
     */
    @Override public void enterEdgeSelection(PatternParser.EdgeSelectionContext ctx) {
        this.selectionSolver.addElementEdgeToken();
    }

    /**
     * Append BinaryBoolean.OR
     */
    @Override public void exitLogicalExpression(PatternParser.LogicalExpressionContext ctx) {
        if (ctx.OR() != null){
            for (int i = 0; i < ctx.OR().size(); i++) {
                this.selectionSolver.addElementBinaryOp(SelectionSolver.BinaryBoolean.OperatorType.OR);
            }
        }
    }

    /**
     * Append BinaryBoolean.AND
     */
    @Override public void exitBooleanAndExpression(PatternParser.BooleanAndExpressionContext ctx) {
        if  (ctx.AND() != null) {
            for (int i = 0; i < ctx.AND().size(); i++) {
                this.selectionSolver.addElementBinaryOp(SelectionSolver.BinaryBoolean.OperatorType.AND);
            }
        }
    }
    /**
     * Append NOT token
     */
    @Override public void exitUnaryExpression(PatternParser.UnaryExpressionContext ctx) {
        if (ctx.NOT() != null) {
            this.selectionSolver.addElementUnaryOp(
                    SelectionSolver.UnaryBoolean.OperatorType.NOT
            );
        }
    }
    /**
     * ->Skip (no need to add anything)
     */
    @Override public void exitPrimaryExpression(PatternParser.PrimaryExpressionContext ctx) { }

    /**
     * Add boolean predicate
     */
    @Override public void exitBoolPredicate(PatternParser.BoolPredicateContext ctx) {

        //Fields to fill
        SelectionSolver.Operation.Operator operator;
        String[] valueLeft = new String[1]; //Value or Label/variable name
        String[] valueRight = new String[1];
        String withinLeft = null;
        String withinRight = null;
        SelectionSolver.Operation.Type typeLeft;
        SelectionSolver.Operation.Type typeRight;
        SelectionSolver.Operation.WindowType leftWType = SelectionSolver.Operation.WindowType.AGO;
        SelectionSolver.Operation.WindowType rightWType = SelectionSolver.Operation.WindowType.AGO;


        //Get operator type
        operator = SelectionSolver.Operation.Operator.getOperator(ctx.Operator().getText());


        if (ctx.leftOp.label() != null) {
            valueLeft[0] = CommonsParser.getLabel(ctx.leftOp.label());
            typeLeft = SelectionSolver.Operation.Type.LABEL;
        }
        else if (ctx.leftOp.value() != null) {
            valueLeft[0] = CommonsParser.getValue(ctx.leftOp.value());
            typeLeft = SelectionSolver.Operation.Type.VALUE;
        }
        else if (ctx.leftOp.temporalVariable() != null) {
            Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar =
                    CommonsParser.getTemporalVar(ctx.leftOp.temporalVariable());
            valueLeft[0] = temporalVar.f0;
            typeLeft = SelectionSolver.Operation.Type.VARIABLE;
            leftWType = temporalVar.f2;
            withinLeft = temporalVar.f1;

        }
        else throw new RuntimeException("Left operator unrecognized");


        if (ctx.rightOp.label() != null) {
            valueRight[0] = CommonsParser.getLabel(ctx.rightOp.label());
            typeRight = SelectionSolver.Operation.Type.LABEL;
        }
        else if (ctx.rightOp.value() != null) {
            valueRight[0] = CommonsParser.getValue(ctx.rightOp.value());
            typeRight = SelectionSolver.Operation.Type.VALUE;
        }
        else if (ctx.rightOp.temporalVariable() != null) {
            Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar =
                    CommonsParser.getTemporalVar(ctx.rightOp.temporalVariable());
            valueRight[0] = temporalVar.f0;
            typeRight = SelectionSolver.Operation.Type.VARIABLE;
            rightWType = temporalVar.f2;
            withinRight = temporalVar.f1;
        }
        else throw new RuntimeException("Right operator unrecognized");

        this.selectionSolver.addElementOperation(
                operator,
                new Pair<>(valueLeft, valueRight),
                new Pair<>(typeLeft, typeRight),
                new Pair<>(withinLeft, withinRight),
                new Pair<>(leftWType, rightWType)
        );
    }
}
