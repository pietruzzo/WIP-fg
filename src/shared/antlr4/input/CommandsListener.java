// Generated from /home/pietro/IdeaProjects/WIP-fg/src/shared/antlr4/input/Commands.g4 by ANTLR 4.8
package shared.antlr4.input;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CommandsParser}.
 */
public interface CommandsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CommandsParser#clientCommand}.
	 * @param ctx the parse tree
	 */
	void enterClientCommand(CommandsParser.ClientCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#clientCommand}.
	 * @param ctx the parse tree
	 */
	void exitClientCommand(CommandsParser.ClientCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#updateCommand}.
	 * @param ctx the parse tree
	 */
	void enterUpdateCommand(CommandsParser.UpdateCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#updateCommand}.
	 * @param ctx the parse tree
	 */
	void exitUpdateCommand(CommandsParser.UpdateCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#vertexUpdate}.
	 * @param ctx the parse tree
	 */
	void enterVertexUpdate(CommandsParser.VertexUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#vertexUpdate}.
	 * @param ctx the parse tree
	 */
	void exitVertexUpdate(CommandsParser.VertexUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#edgeUpdate}.
	 * @param ctx the parse tree
	 */
	void enterEdgeUpdate(CommandsParser.EdgeUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#edgeUpdate}.
	 * @param ctx the parse tree
	 */
	void exitEdgeUpdate(CommandsParser.EdgeUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#labelValues}.
	 * @param ctx the parse tree
	 */
	void enterLabelValues(CommandsParser.LabelValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#labelValues}.
	 * @param ctx the parse tree
	 */
	void exitLabelValues(CommandsParser.LabelValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#vertexType}.
	 * @param ctx the parse tree
	 */
	void enterVertexType(CommandsParser.VertexTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#vertexType}.
	 * @param ctx the parse tree
	 */
	void exitVertexType(CommandsParser.VertexTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#updateType}.
	 * @param ctx the parse tree
	 */
	void enterUpdateType(CommandsParser.UpdateTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#updateType}.
	 * @param ctx the parse tree
	 */
	void exitUpdateType(CommandsParser.UpdateTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#edgeIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterEdgeIdentifier(CommandsParser.EdgeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#edgeIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitEdgeIdentifier(CommandsParser.EdgeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(CommandsParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(CommandsParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp(CommandsParser.TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp(CommandsParser.TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(CommandsParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(CommandsParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CommandsParser#litterals}.
	 * @param ctx the parse tree
	 */
	void enterLitterals(CommandsParser.LitteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CommandsParser#litterals}.
	 * @param ctx the parse tree
	 */
	void exitLitterals(CommandsParser.LitteralsContext ctx);
}