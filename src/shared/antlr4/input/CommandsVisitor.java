// Generated from /home/pietro/IdeaProjects/WIP-fg/src/shared/antlr4/input/Commands.g4 by ANTLR 4.8
package shared.antlr4.input;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CommandsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CommandsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CommandsParser#clientCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClientCommand(CommandsParser.ClientCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#updateCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateCommand(CommandsParser.UpdateCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#vertexUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVertexUpdate(CommandsParser.VertexUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#edgeUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdgeUpdate(CommandsParser.EdgeUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#labelValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelValues(CommandsParser.LabelValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#vertexType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVertexType(CommandsParser.VertexTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#updateType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateType(CommandsParser.UpdateTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#edgeIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdgeIdentifier(CommandsParser.EdgeIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(CommandsParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#timestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestamp(CommandsParser.TimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(CommandsParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link CommandsParser#litterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLitterals(CommandsParser.LitteralsContext ctx);
}