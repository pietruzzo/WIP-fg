package shared.computation;

import shared.AkkaMessages.StepMessage;
import shared.Vertex;
import shared.data.BoxMsg;
import shared.data.SynchronizedIterator;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ComputationRuntime {

    private final ComputationId computationId;
    private final Computation computation;
    private final Map<String, String> partition_Identification;
    private final HashMap<String, VertexProxy> vertices; //con gli edges modificati

    private int stepNumber;
    private SynchronizedIterator<Map.Entry<String, List<StepMessage>>> ingoingMessages; //Vertex name - List of Messages
    private BoxMsg outgoingMessages;


    public ComputationRuntime(ComputationId computationId, Computation computation, Map<String, String> partition_identification, HashMap<String, VertexProxy> vertices) {
        this.computationId = computationId;
        this.computation = computation;
        partition_Identification = partition_identification;
        this.vertices = vertices;
    }

    public BoxMsg<StepMessage> compute (BoxMsg incoming, int stepNumber, ThreadPoolExecutor executors) throws ExecutionException, InterruptedException {
        Collection<Future> executions = new LinkedList<>();
        this.stepNumber = stepNumber;
        this.ingoingMessages = incoming.getSyncIterator();
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

        SynchronizedIterator<VertexProxy> vertex_iterator= new SynchronizedIterator<>(vertices.values().iterator());

        //Launch executors
        for (int i = 0; i < executors.getMaximumPoolSize(); i++) {
            executors.submit(new ComputeResultsThread(this, vertex_iterator));
        }

        //Wait executors end
        for (Future<?> future: executions) {
            future.get();
        }
    }

    public ComputationId getComputationId() {
        return computationId;
    }


    public Map<String, String> getPartition_Identification() {
        return partition_Identification;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public BoxMsg getOutgoingMessages() {
        return outgoingMessages;
    }



    static class ComputationThread implements Runnable {

        private final ComputationRuntime computationRuntime;

        ComputationThread(ComputationRuntime computationRuntime){
            this.computationRuntime = computationRuntime;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Map.Entry<String, List<StepMessage>> next = computationRuntime.ingoingMessages.next();
                    VertexProxy vertex = computationRuntime.vertices.get(next.getKey());
                    List<StepMessage> messages = next.getValue();
                    int stepNumber = computationRuntime.stepNumber;
                    List<StepMessage> outbox = computationRuntime.computation.iterate(vertex, messages, stepNumber);

                    //Register outgoing messages
                    for (StepMessage sm: outbox) {
                        computationRuntime.outgoingMessages.put(sm.destinationVertex, sm);
                    }

                }
            } catch (NoSuchElementException e) {
                //No more Elements -> End of Execution
            }
        }
    }

    static class ComputeResultsThread extends ComputationThread {

        SynchronizedIterator<VertexProxy> vertexIterator;
        ComputeResultsThread(ComputationRuntime computationRuntime, SynchronizedIterator<VertexProxy> vertexIterator) {
            super(computationRuntime);
            this.vertexIterator = vertexIterator;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    VertexProxy vertex = vertexIterator.next();
                    super.computationRuntime.computation.compute_result(vertex);
                }
            } catch (NoSuchElementException e) {
                //No more Elements -> End of Execution
            }
        }
    }
}
