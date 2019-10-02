package shared.computation;

import akka.japi.Pair;
import shared.AkkaMessages.StepMsg;
import shared.data.BoxMsg;
import shared.data.SynchronizedIterator;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ComputationRuntime {

    private final long timestamp;
    private final Computation computation;
    private final LinkedHashMap<String, String> freeVars; //FreeVars
    private final Map<String, VertexProxy> vertices; //con gli edges modificati

    private int stepNumber;
    private BoxMsg outgoingMessages;
    private BoxMsg inboxMessages;
    private SynchronizedIterator<Map.Entry<String, List<StepMsg>>> ingoingMessages; //Vertex name - List of Messages


    public ComputationRuntime(long timestamp, Computation computation, LinkedHashMap<String, String> freeVars, Map<String, VertexProxy> vertices) {
        this.timestamp = timestamp;
        this.computation = computation;
        this.freeVars = freeVars;
        this.vertices = vertices;
        this.inboxMessages = new BoxMsg(timestamp);
    }

    public BoxMsg<StepMsg> compute (int stepNumber, ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {
        Collection<Future> executions = new LinkedList<>();
        this.stepNumber = stepNumber;
        this.ingoingMessages = this.inboxMessages.getSyncIterator();
        outgoingMessages = new BoxMsg(stepNumber);
        //Launch executors
        for (int i = 0; i < executors.getMaximumPoolSize(); i++) {
            executors.submit(new ComputationThread(this));
        }
        //Wait executors end
        for (Future<?> future: executions) {
            future.get();
        }
        //Return BoxMsg
        return outgoingMessages;
    }

    public void getResults (ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {
        Collection<Future> executions = new LinkedList<>();

        //Launch executors
        for (int i = 0; i < executors.getMaximumPoolSize(); i++) {
            executors.submit(new ComputeResultsThread(this));
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
        //TODO: we need a callback to original vertex
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

        ComputationThread(ComputationRuntime computationRuntime){
            this.computationRuntime = computationRuntime;
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
            SynchronizedIterator <VertexProxy> vertexIterator= new SynchronizedIterator<>(computationRuntime.vertices.values().iterator());
            while (true) {
                VertexProxy vertex = vertexIterator.next();
                registerOutgoingMsg(computationRuntime.computation.firstIterate(vertex));
            }
        }

        private void step(){
            while (true) {
                Map.Entry<String, List<StepMsg>> next = computationRuntime.ingoingMessages.next();
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

        SynchronizedIterator<VertexProxy> vertexIterator;
        ComputeResultsThread(ComputationRuntime computationRuntime) {
            super(computationRuntime);
        }

        @Override
        public void run() {
            try {
                vertexIterator= new SynchronizedIterator<>(super.computationRuntime.vertices.values().iterator());
                while (true) {
                    VertexProxy vertex = vertexIterator.next();
                    List<Pair<String, String>> results = super.computationRuntime.computation.compute_result(vertex);
                    for (Pair<String, String> e: results) {
                        super.computationRuntime.registerResult(vertex.getVertexName(), e.first(), e.second());
                    }
                }
            } catch (NoSuchElementException e) {
                //No more Elements -> End of Execution
            }
        }
    }
}
