package shared.resources;

import org.apache.flink.api.java.utils.ParameterTool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class LogToIndices {

    /**
     * -b begin String
     * -e End String
     * -p path
     *
     *  ___OPENING_PEFORMANCE_LOG___
     * EXITING_GRAPH_SAVER_1580666836635
     * ENTERING_PARTITION_1580666836640
     * EXITING_PARTITION_1580666836711
     * ENTERING_STREAMS_1580666836715
     * _BEFORE_EMISSION_MEM_32448080
     * _AFTER_EMISSION_MEM_32477944
     * EXITING_STREAMS_1580666836806
     * EXITING_STREAMS_1580666836815
     * ENTERING_GRAPH_SAVER_1580666836815
     * EXITING_GRAPH_SAVER_1580666836824
     * ENTERING_PARTITION_1580666836824
     * EXITING_PARTITION_1580666836836
     * ENTERING_STREAMS_1580666836836
     * _BEFORE_EMISSION_MEM_32437608
     * _AFTER_EMISSION_MEM_32384192
     * EXITING_STREAMS_1580666836896
     * ___CLOSING_PERFORMANCE_LOG___
     */
    public static void main(String[] args) {

        ParameterTool parParser = ParameterTool.fromArgs(args);

        String begin = parParser.get("b");
        String end = parParser.get("e");
        String path = parParser.get("p");

        try {
            convertFromFile(path, begin, end);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void convertFromFile (String path, String beginString, String endString) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> {
                if (s.startsWith(beginString) || s.startsWith(endString)) {
                    contentBuilder.append(s).append("\n");
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String converted = convertFromString(contentBuilder.toString(), beginString, endString);

        String[] pathParts= path.split(File.separator);
        pathParts[pathParts.length-1] = pathParts[pathParts.length-1].replaceAll("\\.", "") + beginString+endString+".txt";
        final StringBuilder newPath = new StringBuilder();
        Arrays.stream(pathParts).forEach(s -> newPath.append(s).append(File.separator));

        try {
            Files.write(Paths.get(newPath.toString()), converted.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertFromString (String logString, String beginString, String endString) {
        StringBuilder result = new StringBuilder();
        Long beginTimestamp = null;
        String[] lines = logString.split("\n");

        for (String line: lines) {

            if (line.contains(beginString)){
                beginTimestamp = Long.parseLong(line.replaceFirst(beginString, ""));
            } else if (line.contains(endString)){
                result.append(Long.parseLong(line.replaceFirst(endString, "")) - beginTimestamp).append("\n");
                beginTimestamp = null;
            }
        }

        return result.toString();

    }
}
