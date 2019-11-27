package shared.antlr4;

import akka.japi.Pair;
import org.antlr.v4.runtime.*;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.AkkaMessages.modifyGraph.*;
import shared.VertexM;
import shared.antlr4.input.CommandsBaseListener;
import shared.antlr4.input.CommandsBaseVisitor;
import shared.antlr4.input.CommandsLexer;
import shared.antlr4.input.CommandsParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputParser extends CommandsBaseVisitor {


    /**
     * Parse input string and return message to send to JobManager
     * @param inputString
     * @return
     */
    static public Serializable parse(String inputString) {
        CharStream charStream = CharStreams.fromString(inputString);
        CommandsLexer lexer = new CommandsLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        CommandsParser parser = new CommandsParser(tokens);

        InputParser classVisitor = new InputParser();
        Serializable parsedMsg = classVisitor.visitClientCommand(parser.clientCommand());
        return parsedMsg;
    }

    /**
     * TOP Rule
     * @return generated message to send to task manager
     */
    @Override public Serializable visitClientCommand(CommandsParser.ClientCommandContext ctx) {
        return visitUpdateCommand(ctx.updateCommand());
    }

    /**
     * @return generated message to send to task manager
     */
    @Override public Serializable visitUpdateCommand(CommandsParser.UpdateCommandContext ctx) {

        final Long timestamp = visitTimestamp(ctx.timestamp());
        final ArrayList<Pair<String, String[]>> state = visitLabelValues(ctx.labelValues());

        if (ctx.vertexUpdate() == null) { //EDGE

            Tuple3<String, String, List<String>> edgeOptions = visitEdgeUpdate(ctx.edgeUpdate());

            if (edgeOptions.f1.equals("insert")) {
                return new AddEdgeMsg(
                        edgeOptions.f2.get(0),
                        edgeOptions.f2.get(1),
                        state,
                        timestamp
                );
            } else if (edgeOptions.f1.equals("update")) {
                return new UpdateEdgeMsg(
                        edgeOptions.f2.get(0),
                        edgeOptions.f2.get(1),
                        state,
                        timestamp
                );
            } else if (edgeOptions.f1.equals("delete")) {
                return new DeleteEdgeMsg(
                        edgeOptions.f2.get(0),
                        edgeOptions.f2.get(1),
                        timestamp
                );
            }

        } else {

            Tuple3<String, String, String> vertexOptions = visitVertexUpdate(ctx.vertexUpdate());

            if (vertexOptions.f1.equals("insert") || vertexOptions.f1.equals("update")) {
                return new UpdateVertexMsg(
                        vertexOptions.f2,
                        state,
                        timestamp
                );
            } else if (vertexOptions.f1.equals("delete")) {
                return new DeleteVertexMsg(
                        vertexOptions.f2,
                        timestamp
                );
            }

        }

        throw new RuntimeException("Input not recognized");

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Tuple3<String, String, String> visitVertexUpdate(CommandsParser.VertexUpdateContext ctx) {

        return new Tuple3<>("vertex", ctx.updateType().getText(), visitIdentifier(ctx.identifier()));

    }
    /**
     * @return vertex, updateType and src, dest
     */
    @Override public Tuple3<String, String, List<String>> visitEdgeUpdate(CommandsParser.EdgeUpdateContext ctx) {

        return new Tuple3<>("vertex", ctx.updateType().getText(), visitEdgeIdentifier(ctx.edgeIdentifier()));

    }

    /**
     * @return generate multivalue state
     */
    @Override public ArrayList<Pair<String, String[]>> visitLabelValues(CommandsParser.LabelValuesContext ctx) {

        if (ctx == null) {
            return new ArrayList<>();
        }

        final List<CommandsParser.IdentifierContext> identifiers = ctx.identifier();
        final List<CommandsParser.ValueContext> values = ctx.value();

        ArrayList<Pair<String, String[]>> state = new ArrayList<>();

        for (int i = 0; i < identifiers.size(); i++) {
            state.add(new Pair<>(
                    visitIdentifier(identifiers.get(i)),
                    visitValue(values.get(i))
                    )
            );
        }

        return state;
    }

    /**
     * @return edge identifier
     */
    @Override public List<String> visitEdgeIdentifier(CommandsParser.EdgeIdentifierContext ctx) {
        List<String> edge = new ArrayList<>();
        edge.add((String)visit(ctx.identifier(0)));
        edge.add((String)visit(ctx.identifier(1)));
        return edge;
    }

    /**
     * return the identifier text
     */
    @Override public String visitIdentifier(CommandsParser.IdentifierContext ctx) {
        return (String) visitChildren(ctx);
    }

    /**
     * @return timestamp for labels
     */
    @Override public Long visitTimestamp(CommandsParser.TimestampContext ctx) {
        return Long.parseLong(ctx.getText());
    }

    /**
     * @return value of the label
     */
    @Override public String[] visitValue(CommandsParser.ValueContext ctx) {
        final List<CommandsParser.LitteralsContext> litterals = ctx.litterals();

        return litterals
                .stream()
                .map(RuleContext::getText)
                .toArray(String[]::new);

    }

    /**
     * @return String fo alphanumerical literals
     */
    @Override public String visitLitterals(CommandsParser.LitteralsContext ctx) {
        return ctx.getText();
    }
}
