package shared.streamProcessing.abstractOperators;

import shared.streamProcessing.ExtractedStream;
import shared.streamProcessing.SubstitutableLabels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractOperator implements Serializable {

    protected SubstitutableLabels labels;

    public void setLabels(SubstitutableLabels labels) {
        this.labels = labels;
    }

    public abstract ArrayList<String> getNewFieldNames(ExtractedStream.StreamType streamType);

    public static ArrayList<String> newFieldsKeepingStreamType (ExtractedStream.StreamType streamType, Collection<String> newFields) {
        ArrayList<String> fields = new ArrayList<>();

        if (streamType.equals(ExtractedStream.StreamType.NODE)){
            fields.add(ExtractedStream.NODELABEL);
        } else if (streamType.equals(ExtractedStream.StreamType.EDGE)) {
            fields.add(ExtractedStream.NODELABEL);
            fields.add(ExtractedStream.EDGELABEL);
        }
        fields.addAll(newFields);
        return fields;
    }
}
