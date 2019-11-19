package shared.AkkaMessages;

import shared.streamProcessing.StreamProcessingCallback;

import java.io.Serializable;

public class AggregateMsg implements Serializable {

    private static final long serialVersionUID = 2000546L;

    public StreamProcessingCallback.Aggregate aggregate;

    public AggregateMsg(StreamProcessingCallback.Aggregate aggregate) {
        this.aggregate = aggregate;
    }
}