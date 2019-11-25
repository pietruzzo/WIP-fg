package shared.patterns;

import master.OngoingAggregate;
import master.PatternCallback;
import shared.AkkaMessages.ExtractMsg;
import shared.streamProcessing.Operations;
import shared.streamProcessing.StreamProcessingCallback;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Stream extends Pattern{

    private List<Operations> operationsList;
    private String fireNotif;

    public Stream(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer, List<Operations> operationsList, String fireNotif) {
        super(trigger, variablesToBeGenerated, transportLayer);
        this.operationsList = operationsList;
        this.fireNotif = fireNotif;
    }


    @Override
    boolean startPatternLogic() {

        //Set Reduce and collect aggregates
        setReduceCollectAggregates();

        //Distribute operations
        transportLayer.sendToAllSlaves(new ExtractMsg(operationsList));

        //Wait ack from all
        transportLayer.becomeAwaitAckFromAll();

        return false;
    }

    @Override
    public boolean processMessage(Serializable message) {

        //After acks
        return true;
    }

    private void setReduceCollectAggregates () {

        AtomicInteger identifier = new AtomicInteger(0);

        //Flush OngoingAggregates


        //Put new OngoingAggregates on JobManager

        operationsList
                .parallelStream()
                .forEach(operation -> {

                    if ( operation instanceof Operations.Evaluate ) {

                        Operations.Evaluate evaluate = (Operations.Evaluate) operation;

                        //generate OngoingAggregate for evaluation
                        OngoingAggregate oa = new OngoingAggregate(
                                transportLayer.getNumSlaves(),
                                StreamProcessingCallback.AggregateType.EVALUATION,
                                null,
                                transportLayer.getSelf(),
                                evaluate.fireEvent
                        );

                        // Put in a concurrent datastructure
                        transportLayer.putInOngoingAggregateList(identifier.getAndIncrement(), oa);


                    } else if ( operation instanceof Operations.Emit ) {

                        //generate OngoingAggregate for Aggregate variables
                        OngoingAggregate oa = new OngoingAggregate(
                                transportLayer.getNumSlaves(),
                                StreamProcessingCallback.AggregateType.VARIABLE_AGGREGATE,
                                null,
                                transportLayer.getSelf(),
                                null
                        );

                        // Put in a concurrent datastructure
                        transportLayer.putInOngoingAggregateList(identifier.getAndIncrement(), oa);


                    } else if ( operation instanceof  Operations.Reduce ) {

                        Operations.Reduce reductor = (Operations.Reduce) operation;

                        //Generate OngoingAggregate for Reduction
                        OngoingAggregate oa = new OngoingAggregate(
                                transportLayer.getNumSlaves(),
                                StreamProcessingCallback.AggregateType.REDUCE,
                                reductor.accumulator,
                                transportLayer.getSelf(),
                                null
                        );

                        // Put in a concurrent datastructure
                        transportLayer.putInOngoingAggregateList(identifier.getAndIncrement(), oa);

                    }

                });
    }
}
