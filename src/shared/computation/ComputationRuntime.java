package shared.computation;

import akka.japi.Pair;
import org.apache.flink.api.java.tuple.Tuple2;
import org.jetbrains.annotations.Nullable;
import shared.AkkaMessages.StepMsg;
import shared.Utils;
import shared.data.BoxMsg;
import shared.data.SynchronizedIterator;
import shared.exceptions.ComputationFinishedException;
import shared.exceptions.VariableNotDefined;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class ComputationRuntime {

    private final ComputationCallback taskManager;
    private Computation computation;
    private final LinkedHashMap<String, String> freeVars; //FreeVars, can be null
    private Map<String, Vertex> vertices; //con gli edges modificati

    private int stepNumber;
    private BoxMsg outgoingMessages;
    private BoxMsg inboxMessages;


    public ComputationRuntime(ComputationCallback taskManager, @Nullable Computation computation, LinkedHashMap<String, String> freeVars, Map<String, Vertex> vertices) {
        this.taskManager = taskManager;
        this.computation = computation;
        this.freeVars = freeVars;
        this.vertices = vertices;
        this.inboxMessages = new BoxMsg(0);
    }

    public ComputationRuntime(ComputationCallback taskManager, @Nullable Computation computation, LinkedHashMap<String, String> freeVars) {
        this.taskManager = taskManager;
        this.computation = computation;
        this.freeVars = freeVars;
        this.vertices = new HashMap<>();
        this.inboxMessages = null;
    }


    public void compute (int stepNumber, ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {

        this.stepNumber = stepNumber;
        outgoingMessages = new BoxMsg(stepNumber);

        //If inbox is empty -> Computation finished, empty outboxes
        if (stepNumber > 0 && (inboxMessages == null || inboxMessages.isEmpty()) ) {
            throw new ComputationFinishedException();
        }


        if (stepNumber == 0) { //First superstep -> substitute aggregate variables and prestartMethod
            /*
            computation.computationParameters.values().stream().forEach(parameter -> {
                if (!parameter.isLabel() && !parameter.isValue()) {
                    try {
                        List<String[]> aggregateRaw = taskManager.getVariableSolver().getAggregate(parameter.getParameter()[0], this.getPartition(), parameter.getTimeAgo(), parameter.getwType());
                        List<String> aggregate = aggregateRaw.stream().flatMap(agg -> Arrays.asList(agg).stream()).collect(Collectors.toList());
                        parameter.setLabel(false);
                        parameter.setValue(true);
                        parameter.setParameter(aggregate.toArray(String[]::new));
                    } catch (VariableNotDefined e) {
                        //Skip
                    }

                }
            });
         */

            //Set partition for NODE/EDGE variables solvers
            computation.computationParameters.setPartition(this.getPartition());

            //Run prestart
            computation.preStart();
        }

        //Launch executors
        Utils.parallelizeAndWait(executors, new ComputationThread(this, new SynchronizedIterator<>(vertices.values().iterator()), this.inboxMessages.getSyncIterator()));

        //Reset Inbox
        this.inboxMessages = null;

    }

    public void computeResults(ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {
        Collection<Future> executions = new LinkedList<>();

        //Launch executors
        Utils.parallelizeAndWait(executors, new ComputeResultsThread(this, new SynchronizedIterator<>(vertices.values().iterator()), null));

        //Wait executors end
        for (Future<?> future: executions) {
            future.get();
        }
    }

    public Computation getComputation() {
        return computation;
    }

    public void setComputation(Computation computation) {
        this.computation = computation;
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

    /**
     * Attach partition and return
     * @return
     */
    public BoxMsg getOutgoingMessages() {
        this.outgoingMessages.setPartition(this.freeVars);
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

        SynchronizedIterator<Map.Entry<String, ArrayList<TMes>>> entry = ingoingMessages.getSyncIterator();

        try{
            while(true){
                Map.Entry<String, ArrayList<TMes>> e = entry.next();

                for (TMes message: e.getValue()) {

                    this.inboxMessages.put(e.getKey(), message);

                }
            }
        } catch (NoSuchElementException e) {/*End*/}
    }


    static class ComputationThread implements Utils.DuplicableRunnable {

        private final ComputationRuntime computationRuntime;
        private final SynchronizedIterator<Vertex> vertexIterator;
        private final SynchronizedIterator<Map.Entry<String, List<ArrayList<StepMsg>>>> ingoingMessages;

        ComputationThread(ComputationRuntime computationRuntime, @Nullable SynchronizedIterator<Vertex> vertexIterator, @Nullable SynchronizedIterator<Map.Entry<String, List<ArrayList<StepMsg>>>> ingoingMessages){
            this.computationRuntime = computationRuntime;
            this.vertexIterator = vertexIterator;
            this.ingoingMessages = ingoingMessages;
        }

        private ComputationThread(ComputationThread computationThread){
            this.computationRuntime = computationThread.computationRuntime;
            this.vertexIterator = computationThread.vertexIterator;
            this.ingoingMessages = computationThread.ingoingMessages;
        }

        @Override
        public void run() {
            try {
                if (computationRuntime.stepNumber == 0){
                    firstStep();
                } else {
                    step();
                }
            } catch (NoSuchElementException e) {
                //No more Elements -> End of Execution
            }
        }

        private void firstStep(){
            while (true) {
                Vertex vertex = vertexIterator.next();
                registerOutgoingMsg(computationRuntime.computation.firstIterate(vertex));
            }
        }

        private void step(){
            while (true) {
                Map.Entry<String, List<ArrayList<StepMsg>>> next = ingoingMessages.next();
                Vertex vertex = computationRuntime.vertices.get(next.getKey());
                List<ArrayList<StepMsg>> messages = next.getValue();
                List<StepMsg> flattered = messages.stream().flatMap(arraylist -> arraylist.stream()).collect(Collectors.toList());
                int stepNumber = computationRuntime.stepNumber;
                registerOutgoingMsg(computationRuntime.computation.iterate(vertex, flattered, stepNumber));
            }
        }

        private void registerOutgoingMsg (List<StepMsg> outbox) {

            if (outbox == null || outbox.isEmpty())
                return;

            for (StepMsg sm : outbox) {
                computationRuntime.outgoingMessages.put(sm.destinationVertex, sm);
            }
        }

        @Override
        public Utils.DuplicableRunnable getCopy() {
            return new ComputationThread(this);
        }
    }

    static class ComputeResultsThread extends ComputationThread {


        ComputeResultsThread(ComputationRuntime computationRuntime, @Nullable SynchronizedIterator<Vertex> vertexIterator, @Nullable SynchronizedIterator<Map.Entry<String, List<ArrayList<StepMsg>>>> ingoingMessages) {
            super(computationRuntime, vertexIterator, ingoingMessages);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Vertex vertex = super.vertexIterator.next();
                    List<Pair<String, String[]>> results = super.computationRuntime.computation.computeResults(vertex);
                    for (Pair<String, String[]> e: results) {
                        for (Tuple2<String, Long> returnVar : super.computationRuntime.computation.getVarsTemporalWindow()) {
                            if (returnVar.f0.equals(e.first())) {
                                super.computationRuntime.registerResult(vertex.getNodeId(), returnVar, e.second());
                                break;
                            }
                        }

                    }
                }
            } catch (NoSuchElementException e) {/* END */}
        }
    }
}
