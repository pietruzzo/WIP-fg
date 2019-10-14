package shared.computation;

import akka.japi.Pair;
import jdk.internal.jline.internal.Nullable;
import shared.AkkaMessages.StepMsg;
import shared.Utils;
import shared.data.BoxMsg;
import shared.data.SynchronizedIterator;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ComputationRuntime {

    private final ComputationCallback taskManager;
    private final long timestamp;
    private final Computation computation;
    private final LinkedHashMap<String, String> freeVars; //FreeVars, can be null
    private final Map<String, VertexProxy> vertices; //con gli edges modificati

    private int stepNumber;
    private BoxMsg outgoingMessages;
    private BoxMsg inboxMessages;


    public ComputationRuntime(ComputationCallback taskManager, long timestamp, Computation computation, LinkedHashMap<String, String> freeVars, Map<String, VertexProxy> vertices) {
        this.taskManager = taskManager;
        this.timestamp = timestamp;
        this.computation = computation;
        this.freeVars = freeVars;
        this.vertices = vertices;
        this.inboxMessages = new BoxMsg(timestamp);
    }

    public BoxMsg<StepMsg> compute (int stepNumber, ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {
        this.stepNumber = stepNumber;
        outgoingMessages = new BoxMsg(stepNumber);

        //Launch executors
        Utils.parallelizeAndWait(executors, new ComputationThread(this, new SynchronizedIterator<>(vertices.values().iterator()), this.inboxMessages.getSyncIterator()));

        //Return BoxMsg
        return outgoingMessages;
    }

    public void computeResults(ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {
        Collection<Future> executions = new LinkedList<>();

        //Launch executors
        for (int i = 0; i < executors.getMaximumPoolSize(); i++) {
            executors.submit(new ComputeResultsThread(this, new SynchronizedIterator<>(vertices.values().iterator()), null));
        }

        //Wait executors end
        for (Future<?> future: executions) {
            future.get();
        }
    }

    public long getComputationId() {
        return timestamp;
    }


    public Map<String, String> getPartition() {
        return freeVars;
    }

    public int getStepNumber() {
        return stepNumber;
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
     * @param key
     * @param value
     */
    private void registerResult(String vertexName, String key, String value) {
        taskManager.updateState(vertexName, key, value, this.timestamp);
    }

    /**
     * Add incoming messages to existing ones if they have the same timestamp, otherwise flush older version
     * and add new messages
     */
    public <TMes> void updateIncomingMsgs(BoxMsg ingoingMessages){
        if (ingoingMessages.getStepNumber()> this.inboxMessages.getStepNumber()){
            this.inboxMessages = new BoxMsg(ingoingMessages.getStepNumber());
        }
        SynchronizedIterator<Map.Entry<String, ArrayList<TMes>>> entry = ingoingMessages.getSyncIterator();
        try{
            while(true){
                Map.Entry<String, ArrayList<TMes>> e = entry.next();

                for (TMes message: e.getValue()) {
                    this.inboxMessages.put(e.getKey(), e.getValue());
                }
            }
        } catch (NoSuchElementException e) {/*End*/}
    }


    static class ComputationThread implements Runnable {

        private final ComputationRuntime computationRuntime;
        private final SynchronizedIterator<VertexProxy> vertexIterator;
        private final SynchronizedIterator<Map.Entry<String, List<StepMsg>>> ingoingMessages;

        ComputationThread(ComputationRuntime computationRuntime, @Nullable SynchronizedIterator<VertexProxy> vertexIterator, @Nullable SynchronizedIterator<Map.Entry<String, List<StepMsg>>> ingoingMessages){
            this.computationRuntime = computationRuntime;
            this.vertexIterator = vertexIterator;
            this.ingoingMessages = ingoingMessages;
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
                VertexProxy vertex = vertexIterator.next();
                registerOutgoingMsg(computationRuntime.computation.firstIterate(vertex));
            }
        }

        private void step(){
            while (true) {
                Map.Entry<String, List<StepMsg>> next = ingoingMessages.next();
                VertexProxy vertex = computationRuntime.vertices.get(next.getKey());
                List<StepMsg> messages = next.getValue();
                int stepNumber = computationRuntime.stepNumber;
                registerOutgoingMsg(computationRuntime.computation.iterate(vertex, messages, stepNumber));
            }
        }

        private void registerOutgoingMsg (List<StepMsg> outbox) {
            for (StepMsg sm : outbox) {
                computationRuntime.outgoingMessages.put(sm.destinationVertex, sm);
            }
        }
    }

    static class ComputeResultsThread extends ComputationThread {


        ComputeResultsThread(ComputationRuntime computationRuntime, @Nullable SynchronizedIterator<VertexProxy> vertexIterator, @Nullable SynchronizedIterator<Map.Entry<String, List<StepMsg>>> ingoingMessages) {
            super(computationRuntime, vertexIterator, ingoingMessages);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    VertexProxy vertex = super.vertexIterator.next();
                    List<Pair<String, String>> results = super.computationRuntime.computation.compute_result(vertex);
                    for (Pair<String, String> e: results) {
                        super.computationRuntime.registerResult(vertex.getVertexName(), e.first(), e.second());
                    }
                }
            } catch (NoSuchElementException e) {/* END */}
        }
    }
}
