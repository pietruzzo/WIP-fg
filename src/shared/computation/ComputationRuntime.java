package shared.computation;

import akka.actor.ActorRef;
import akka.japi.Pair;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.AkkaMessages.StepMsg;
import shared.data.BoxMsg;
import shared.exceptions.ComputationFinishedException;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ComputationRuntime {

    private final ComputationCallback taskManager;
    private Computation computation;
    private final LinkedHashMap<String, String> freeVars; //FreeVars, can be null
    private Map<String, Vertex> vertices; //con gli edges modificati

    private int stepNumber;
    private Map<ActorRef, BoxMsg> outgoingMessages;
    private BoxMsg inboxMessages;


    public ComputationRuntime(ComputationCallback taskManager, LinkedHashMap<String, String> freeVars, Map<String, Vertex> vertices) {
        this.taskManager = taskManager;
        this.computation = null;
        this.freeVars = freeVars;
        this.vertices = vertices;
        this.inboxMessages = new BoxMsg(0);
    }

    public ComputationRuntime(ComputationCallback taskManager, LinkedHashMap<String, String> freeVars) {
        this.taskManager = taskManager;
        this.computation = null;
        this.freeVars = freeVars;
        this.vertices = new HashMap<>();
        this.inboxMessages = new BoxMsg(0);
    }


    public void compute (int stepNumber) {

        this.stepNumber = stepNumber;

        //Initialize outbox
        //Create an outbox for each ActorRef
        outgoingMessages = new HashMap();
        for (ActorRef destinations: taskManager.getActors().values()) {
            BoxMsg box = new BoxMsg(stepNumber);
            box.setPartition(getPartition());
            outgoingMessages.put(destinations, box);
        }

        //If inbox is empty -> Computation finished, empty outboxes
        if (stepNumber > 0 && (inboxMessages == null || inboxMessages.isEmpty()) ) {
            throw new ComputationFinishedException();
        }


        if (stepNumber == 0) { //First superstep -> substitute aggregate variables and prestartMethod

            //Set partition for NODE/EDGE variables solvers
            computation.computationParameters.setPartition(this.getPartition());

            //Run prestart
            computation.preStart();
        }

        //Launch executors
        //Utils.parallelizeAndWait(executors, new ComputationThread(this, new SynchronizedIterator<>(vertices.values().iterator()), this.inboxMessages.getSyncIterator()));
        if (this.stepNumber == 0){
            for (Vertex vertex: vertices.values()) {
                registerOutgoingMsg(this.computation.firstIterate(vertex));
            }
        } else {
            for (Map.Entry<String, ArrayList<StepMsg>> next:  ((Set<Map.Entry<String, ArrayList<StepMsg>>>)inboxMessages.getData().entrySet())) {
                Vertex vertex = this.vertices.get(next.getKey());
                ArrayList<StepMsg> messages = next.getValue();
                registerOutgoingMsg(this.computation.iterate(vertex, messages, stepNumber));
            }
        }

        //Reset Inbox
        this.inboxMessages = null;

    }

    public void computeResults()  {

        //Launch executors
        //Utils.parallelizeAndWait(executors, new ComputeResultsThread(this, new SynchronizedIterator<>(vertices.values().iterator()), null));
        for (Vertex vertex: vertices.values()) {
            List<Pair<String, String[]>> results = this.computation.computeResults(vertex);
            for (Pair<String, String[]> e: results) {
                for (Tuple2<String, Long> returnVar : this.computation.getVarsTemporalWindow()) {
                    if (returnVar.f0.equals(e.first())) {
                        this.registerResult(vertex.getNodeId(), returnVar, e.second());
                        break;
                    }
                }
            }
        }

    }

    public Computation getComputation() {
        return computation;
    }

    public void setComputation(Computation computation) {
        try {
            this.computation = computation.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setVertices(Map<String, Vertex> vertices) { this.vertices = vertices; }

    public Map<String, String> getPartition() {
        return freeVars;
    }

    public int getStepNumber() {
        return stepNumber;
    };

    public Map<String, Vertex> getVertices() {
        return vertices;
    }

    public void putIntoVertices(String vertexName, Vertex vertex){
            this.vertices.put(vertexName, vertex);
    }

    /**
     * Attach partition and return
     * @return
     */
    public Map<ActorRef, BoxMsg> getOutgoingMessages() {
        return outgoingMessages;
    }

    /**
     * Register results of computation on node state with the timestamp of computation
     * @param vertexName
     * @param variableName
     * @param values
     */
    private void registerResult(String vertexName, Tuple2<String, Long> variableName, String[] values) { //DONE Modify to write on Variables (node)

            taskManager.registerComputationResult(vertexName, variableName, values, this.getPartition());

    }

    /**
     * Add incoming messages to existing ones if they have the same timestamp, otherwise create new inbox
     * and add new messages
     */
    public <TMes> void updateIncomingMsgs(BoxMsg ingoingMessages){

        if (inboxMessages == null){
            this.inboxMessages = new BoxMsg(ingoingMessages.getStepNumber());
        }

        Set<Map.Entry<String, ArrayList<TMes>>> entrySet = ingoingMessages.getData().entrySet();

        for (Map.Entry<String, ArrayList<TMes>> entry : entrySet) {

            Object o = this.inboxMessages.getData().computeIfAbsent(entry.getKey(), k -> entry.getValue());

            if (o != entry.getValue()) { //It was already present
                for (TMes message : entry.getValue()) {
                    this.inboxMessages.put(entry.getKey(), message);

                }
            }
        }
    }

    private void registerOutgoingMsg (List<StepMsg> outbox) {

        if (outbox == null || outbox.isEmpty())
            return;

        for (StepMsg sm : outbox) {

            //Get right outbox based on destinationVertex
            ActorRef actor = taskManager.getActor(sm.destinationVertex);
            BoxMsg actorBox = outgoingMessages.get(actor);

            actorBox.getData().computeIfAbsent(sm.destinationVertex, k-> new ArrayList<>());
            actorBox.put(sm.destinationVertex, sm);

        }
    }

}
