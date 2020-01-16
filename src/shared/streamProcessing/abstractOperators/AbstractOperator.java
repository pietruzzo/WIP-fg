package shared.streamProcessing.abstractOperators;

import shared.streamProcessing.ExtractedStream;
import shared.streamProcessing.SubstitutableLabels;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractOperator implements Serializable {

    protected SubstitutableLabels labels;

    public void setLabels(SubstitutableLabels labels) {
        this.labels = labels;
    }

    public abstract ArrayList<String> getNewFieldNames(ExtractedStream.StreamType streamType);
}
