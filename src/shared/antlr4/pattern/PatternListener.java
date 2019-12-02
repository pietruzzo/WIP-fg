// Generated from /home/pietro/IdeaProjects/WIP-fg/src/shared/antlr4/pattern/Pattern.g4 by ANTLR 4.7.2
package shared.antlr4.pattern;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PatternParser}.
 */
public interface PatternListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PatternParser#patternEntry}.
	 * @param ctx the parse tree
	 */
	void enterPatternEntry(PatternParser.PatternEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#patternEntry}.
	 * @param ctx the parse tree
	 */
	void exitPatternEntry(PatternParser.PatternEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#temporalPattern}.
	 * @param ctx the parse tree
	 */
	void enterTemporalPattern(PatternParser.TemporalPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#temporalPattern}.
	 * @param ctx the parse tree
	 */
	void exitTemporalPattern(PatternParser.TemporalPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#graphProcessing}.
	 * @param ctx the parse tree
	 */
	void enterGraphProcessing(PatternParser.GraphProcessingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#graphProcessing}.
	 * @param ctx the parse tree
	 */
	void exitGraphProcessing(PatternParser.GraphProcessingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#collectStreams}.
	 * @param ctx the parse tree
	 */
	void enterCollectStreams(PatternParser.CollectStreamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#collectStreams}.
	 * @param ctx the parse tree
	 */
	void exitCollectStreams(PatternParser.CollectStreamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#streamProcessing}.
	 * @param ctx the parse tree
	 */
	void enterStreamProcessing(PatternParser.StreamProcessingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#streamProcessing}.
	 * @param ctx the parse tree
	 */
	void exitStreamProcessing(PatternParser.StreamProcessingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#computation}.
	 * @param ctx the parse tree
	 */
	void enterComputation(PatternParser.ComputationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#computation}.
	 * @param ctx the parse tree
	 */
	void exitComputation(PatternParser.ComputationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#selection}.
	 * @param ctx the parse tree
	 */
	void enterSelection(PatternParser.SelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#selection}.
	 * @param ctx the parse tree
	 */
	void exitSelection(PatternParser.SelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#partition}.
	 * @param ctx the parse tree
	 */
	void enterPartition(PatternParser.PartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#partition}.
	 * @param ctx the parse tree
	 */
	void exitPartition(PatternParser.PartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#extraction}.
	 * @param ctx the parse tree
	 */
	void enterExtraction(PatternParser.ExtractionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#extraction}.
	 * @param ctx the parse tree
	 */
	void exitExtraction(PatternParser.ExtractionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#evaluation}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation(PatternParser.EvaluationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#evaluation}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation(PatternParser.EvaluationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation(PatternParser.OperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation(PatternParser.OperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#computationFunction}.
	 * @param ctx the parse tree
	 */
	void enterComputationFunction(PatternParser.ComputationFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#computationFunction}.
	 * @param ctx the parse tree
	 */
	void exitComputationFunction(PatternParser.ComputationFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#selectionFunction}.
	 * @param ctx the parse tree
	 */
	void enterSelectionFunction(PatternParser.SelectionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#selectionFunction}.
	 * @param ctx the parse tree
	 */
	void exitSelectionFunction(PatternParser.SelectionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#edgeSelection}.
	 * @param ctx the parse tree
	 */
	void enterEdgeSelection(PatternParser.EdgeSelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#edgeSelection}.
	 * @param ctx the parse tree
	 */
	void exitEdgeSelection(PatternParser.EdgeSelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(PatternParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(PatternParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#booleanAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanAndExpression(PatternParser.BooleanAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#booleanAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanAndExpression(PatternParser.BooleanAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(PatternParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(PatternParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(PatternParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(PatternParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#boolPredicate}.
	 * @param ctx the parse tree
	 */
	void enterBoolPredicate(PatternParser.BoolPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#boolPredicate}.
	 * @param ctx the parse tree
	 */
	void exitBoolPredicate(PatternParser.BoolPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#operands}.
	 * @param ctx the parse tree
	 */
	void enterOperands(PatternParser.OperandsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#operands}.
	 * @param ctx the parse tree
	 */
	void exitOperands(PatternParser.OperandsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#partitionFunction}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunction(PatternParser.PartitionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#partitionFunction}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunction(PatternParser.PartitionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#operationFunction}.
	 * @param ctx the parse tree
	 */
	void enterOperationFunction(PatternParser.OperationFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#operationFunction}.
	 * @param ctx the parse tree
	 */
	void exitOperationFunction(PatternParser.OperationFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#oneFieldOperationAlias}.
	 * @param ctx the parse tree
	 */
	void enterOneFieldOperationAlias(PatternParser.OneFieldOperationAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#oneFieldOperationAlias}.
	 * @param ctx the parse tree
	 */
	void exitOneFieldOperationAlias(PatternParser.OneFieldOperationAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#triggerComputation}.
	 * @param ctx the parse tree
	 */
	void enterTriggerComputation(PatternParser.TriggerComputationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#triggerComputation}.
	 * @param ctx the parse tree
	 */
	void exitTriggerComputation(PatternParser.TriggerComputationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#triggerInput}.
	 * @param ctx the parse tree
	 */
	void enterTriggerInput(PatternParser.TriggerInputContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#triggerInput}.
	 * @param ctx the parse tree
	 */
	void exitTriggerInput(PatternParser.TriggerInputContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#triggerTemporal}.
	 * @param ctx the parse tree
	 */
	void enterTriggerTemporal(PatternParser.TriggerTemporalContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#triggerTemporal}.
	 * @param ctx the parse tree
	 */
	void exitTriggerTemporal(PatternParser.TriggerTemporalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#triggerSensitivity}.
	 * @param ctx the parse tree
	 */
	void enterTriggerSensitivity(PatternParser.TriggerSensitivityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#triggerSensitivity}.
	 * @param ctx the parse tree
	 */
	void exitTriggerSensitivity(PatternParser.TriggerSensitivityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#emission}.
	 * @param ctx the parse tree
	 */
	void enterEmission(PatternParser.EmissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#emission}.
	 * @param ctx the parse tree
	 */
	void exitEmission(PatternParser.EmissionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#temporalVariable}.
	 * @param ctx the parse tree
	 */
	void enterTemporalVariable(PatternParser.TemporalVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#temporalVariable}.
	 * @param ctx the parse tree
	 */
	void exitTemporalVariable(PatternParser.TemporalVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(PatternParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(PatternParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(PatternParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(PatternParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(PatternParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(PatternParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(PatternParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(PatternParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#fireEvent}.
	 * @param ctx the parse tree
	 */
	void enterFireEvent(PatternParser.FireEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#fireEvent}.
	 * @param ctx the parse tree
	 */
	void exitFireEvent(PatternParser.FireEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link PatternParser#tupleField}.
	 * @param ctx the parse tree
	 */
	void enterTupleField(PatternParser.TupleFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PatternParser#tupleField}.
	 * @param ctx the parse tree
	 */
	void exitTupleField(PatternParser.TupleFieldContext ctx);
}