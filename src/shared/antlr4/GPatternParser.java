package shared.antlr4;

import master.PatternCallback;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.Utils;
import shared.antlr4.pattern.PatternBaseListener;
import shared.antlr4.pattern.PatternLexer;
import shared.antlr4.pattern.PatternParser;
import shared.antlr4.pattern.patternSubParsers.*;
import shared.patterns.GraphSaver;
import shared.patterns.Pattern;
import shared.patterns.Trigger;
import shared.selection.SelectionSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GPatternParser extends PatternBaseListener {

    private ArrayList<Pattern> patternElements;
    private PatternCallback callback;

    private Trigger trigger;
    private List<String> sensitivityList;

    public GPatternParser(PatternCallback callback) {
        this.callback = callback;
        this.patternElements = new ArrayList<>();
    }

    static public ArrayList<Pattern> parse(String inputString, PatternCallback callback) {

        CharStream charStream = CharStreams.fromString(inputString);
        PatternLexer lexer = new PatternLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        PatternParser parser = new PatternParser(tokens);


        //Generate top Listener
        GPatternParser gPatternParser = new GPatternParser(callback);

        gPatternParser.enterTemporalPattern(parser.temporalPattern());
        return gPatternParser.patternElements;
    }


   @Override
    public void enterTemporalPattern(shared.antlr4.pattern.PatternParser.TemporalPatternContext ctx) {
       this.trigger = new Trigger(Trigger.TriggerEnum.ALL, null);
       this.sensitivityList = new ArrayList<>();

   }

    @Override
    public void exitTemporalPattern(shared.antlr4.pattern.PatternParser.TemporalPatternContext ctx) {

        if (ctx.emission() == null) return;

        String varName = CommonsParser.getVarName(ctx.emission().variable());



        GraphSaver graphSaver = new GraphSaver(
                this.trigger,
                varName,
                this.callback
        );

        //Get root
        ParserRuleContext root = ctx;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        Long maxTemporalWindow = CommonsParser.getMaxTemporalWindow((PatternParser.PatternEntryContext) root, varName);
        graphSaver.setStore(varName, maxTemporalWindow + 1);

    }

    @Override
    public void enterGraphProcessing(shared.antlr4.pattern.PatternParser.GraphProcessingContext ctx) {
        if (ctx.temporalVariable() != null) {

            Tuple3<String, String, SelectionSolver.Operation.WindowType> temporalVar =
                    CommonsParser.getTemporalVar(ctx.temporalVariable());

            GraphSaver graphSaver = new GraphSaver(
                    this.trigger,
                    null,
                    this.callback
            );
            graphSaver.setRetrieve(temporalVar.f0, temporalVar.f1);

            this.patternElements.add(graphSaver);
        } else {
            //".g()" case
            GraphSaver graphSaver = new GraphSaver(
                    this.trigger,
                    null,
                    this.callback
            );
            graphSaver.setResetPartitioning();

            this.patternElements.add(graphSaver);

        }

    }

    @Override
    public void enterTriggerComputation(shared.antlr4.pattern.PatternParser.TriggerComputationContext ctx) {

        //Solve trigger

        if (ctx.triggerInput() != null) {
            String triggerInput = ctx.triggerInput().getText();
            if (triggerInput.contains("(edgeaddition")){
                this.trigger = new Trigger(Trigger.TriggerEnum.EDGE_ADDITION, this.sensitivityList);
            } else if (triggerInput.contains("(edgedeletion")){
                this.trigger = new Trigger(Trigger.TriggerEnum.EDGE_DELETION, this.sensitivityList);
            } else if (triggerInput.contains("(edgeupdate")){
                this.trigger = new Trigger(Trigger.TriggerEnum.EDGE_UPDATE, this.sensitivityList);
            } else if (triggerInput.contains("(vertexaddition")){
                this.trigger = new Trigger(Trigger.TriggerEnum.VERTEX_ADDITION, this.sensitivityList);
            } else if (triggerInput.contains("(vertexdeletion")){
                this.trigger = new Trigger(Trigger.TriggerEnum.VERTEX_DELETION, this.sensitivityList);
            } else if (triggerInput.contains("(vertexupdate")){
                this.trigger = new Trigger(Trigger.TriggerEnum.VERTEX_UPDATE, this.sensitivityList);
            }

        } else if (ctx.triggerSensitivity() != null) {

            List<String> sensibleVars =
                    ctx.triggerSensitivity().variable()
                            .stream()
                            .map(var -> CommonsParser.getVarName(var))
                            .collect(Collectors.toList());

            this.sensitivityList.addAll(sensibleVars);
            this.trigger = new Trigger(Trigger.TriggerEnum.ALL, this.sensitivityList);

        } else if (ctx.triggerTemporal() != null) {

            long every = Utils.solveTime(ctx.triggerTemporal().Timeunit().getText());
            this.trigger = new Trigger(every, this.sensitivityList);

        }
    }

    @Override
    public void enterComputation(shared.antlr4.pattern.PatternParser.ComputationContext ctx) {
        this.patternElements.add(ComputationParser.getComputation(ctx, this.trigger, this.callback));
    }

    @Override
    public void enterSelection(shared.antlr4.pattern.PatternParser.SelectionContext ctx) {
        this.patternElements.add(SelectionParser.getSelectionSolver(ctx, this.trigger, this.callback));
    }

    @Override
    public void enterPartition(shared.antlr4.pattern.PatternParser.PartitionContext ctx) {
        this.patternElements.add(PartitionParser.getSelectionSolver(ctx, this.trigger, this.callback));
    }

    @Override
    public void enterStreamProcessing(shared.antlr4.pattern.PatternParser.StreamProcessingContext ctx) {
        this.patternElements.add(StreamParser.getStream(ctx, this.trigger, this.callback));
    }

}
