package shared.computation;

import akka.actor.ActorRef;
import akka.japi.Pair;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.PropertyHandler;
import shared.data.BoxMsg;
import shared.data.StepMsg;
import shared.exceptions.ComputationFinishedException;

import java.io.IOException;
import java.util.*;

public class ComputationRuntime {

    private final ComputationCallback taskManager;
    private Computation computation;
    private final LinkedHashMap<String, String> freeVars; //FreeVars, can be null
    private HashMap<String, Vertex> vertices; //with runtime view of edges
    private static final int msgThreshold = retrieveMsgThreshold();

    private int stepNumber;
    private Map<ActorRef, BoxMsg> outgoingMessages;
    private BoxMsg inboxMessages;
    private Map<String, Vertex> activeVertices;


    public ComputationRuntime(ComputationCallback taskManager, LinkedHashMap<String, String> freeVars, HashMap<String, Vertex> vertices) {
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

        //Initialize reset label
        this.computation.resetHalted();
        ArrayList<String> haltedVertices = new ArrayList<>();

        if (stepNumber == 0) { //First superstep -> substitute aggregate variables and prestartMethod

            //Initialize ActiveVertices
            activeVertices = (Map<String, Vertex>) vertices.clone();

            //Set partition for NODE/EDGE variables solvers
            ((ComputationParametersImpl)computation.computationParameters)
                    .setPartition(this.getPartition());

            //Run prestart
            computation.preStart();

            //Execute first iteration
            for (Vertex vertex: activeVertices.values()) {
                registerOutgoingMsg(this.computation.firstIterate(vertex));
                //If vertex has voted to halt -> remove from active vertices
                if (this.computation.resetHalted()) {
                    haltedVertices.add(vertex.getNodeId());
                }
            }

        } else if (activeVertices.isEmpty()) {
            //No message has arrived, and all vertices voted to halt -> computation has terminated
            throw new ComputationFinishedException();
        } else {

            //It isn't the first iteration

            for (Vertex vertex: activeVertices.values()) {
                ArrayList<StepMsg> messages = (ArrayList<StepMsg>) inboxMessages.get(vertex.getNodeId());
                if ( messages == null ) {
                    messages = new ArrayList<>();
                }
                registerOutgoingMsg(this.computation.iterate(vertex, messages, stepNumber));
                //If vertex has voted to halt -> remove from active vertices
                if (this.computation.resetHalted()) {
                    haltedVertices.add(vertex.getNodeId());
                }
            }

        }

        //Remove halted vertices
        for (String deleted: haltedVertices) {
            activeVertices.remove(deleted);
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
                for (Tuple2<String, Long> returnVar :
                        ((ComputationParametersImpl)this.computation.computationParameters)
                                .getReturnVarsTemporalWindow()) {
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

    public void setVertices(HashMap<String, Vertex> vertices) { this.vertices = vertices; }

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

        Set<Map.Entry<String, ArrayList<TMes>>> entrySet = ingoingMessages.entrySet();

        for (Map.Entry<String, ArrayList<TMes>> entry : entrySet) {


            if (this.inboxMessages.containsKey(entry.getKey())) {
                // Vertex already in in inbox -> append new messages
                for (TMes message : entry.getValue()) {
                    this.inboxMessages.addToValue(entry.getKey(), message);
                }
            } else {
                // Generate the entry with the list of incoming messages in inbox
                this.inboxMessages.put(entry.getKey(), entry.getValue());
                //Vertex receiving message becomes active
                this.activeVertices.putIfAbsent(entry.getKey(), vertices.get(entry.getKey()));
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

            actorBox.addToValue(sm.destinationVertex, sm);

            //Send partial outboxes
            if (actorBox.numMessages() > this.msgThreshold){
                taskManager.sendPartialOutbox(actorBox, actor);
                outgoingMessages.put(actor, new BoxMsg(actorBox.getStepNumber()));
            }

        }
    }

    private static int retrieveMsgThreshold () {
        try {
            return Integer.parseInt(PropertyHandler.getProperty("outboxSizeThreshold"));
        } catch (IOException e) {
            return 10000;
        }
    }

}
