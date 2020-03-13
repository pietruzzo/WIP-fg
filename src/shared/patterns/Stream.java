package shared.patterns;

import master.OngoingAggregate;
import master.PatternCallback;
import shared.AkkaMessages.ExtractMsg;
import shared.PropertyHandler;
import shared.streamProcessing.Operations;
import shared.streamProcessing.StreamProcessingCallback;

import java.io.Serializable;
import java.util.List;

public class Stream extends Pattern{

    private List<Operations> operationsList;

    public Stream(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer, List<Operations> operationsList) {
        super(trigger, variablesToBeGenerated, transportLayer);
        this.operationsList = operationsList;
    }


    @Override
    boolean startPatternLogic() {

        //Set Reduce and collect aggregates
        setReduceCollectAggregates();

        PropertyHandler.writeOnPerformanceLog("ENTERING_STREAMS_"+ System.currentTimeMillis());

        //Distribute operations
        transportLayer.sendToAllSlaves(new ExtractMsg(operationsList));

        //Wait ack from all
        transportLayer.becomeAwaitAckFromAll();

        return false;
    }

    @Override
    public boolean processMessage(Serializable message) {

        PropertyHandler.writeOnPerformanceLog("EXITING_STREAMS_"+ System.currentTimeMillis());

        //After acks
        return true;
    }

    private void setReduceCollectAggregates () {

        //Flush OngoingAggregates


        //Put new OngoingAggregates on JobManager

        operationsList
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
                        transportLayer.putInOngoingAggregateList(((Operations.Evaluate) operation).getTransaction_id(), oa);


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
                        transportLayer.putInOngoingAggregateList(((Operations.Emit) operation).getTransaction_id(), oa);


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
                        transportLayer.putInOngoingAggregateList(((Operations.Reduce) operation).getTransaction_id(), oa);

                    }

                });
    }
}
