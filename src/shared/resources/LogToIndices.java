package shared.resources;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
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
            getStatisticsForEachDataset(path, begin, end);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String getLinesFromFile (String path, String beginString, String endString) {
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
        return  contentBuilder.toString();
    }

    public static List<String> convertFromString (String logString, String beginString, String endString) {
        List<String> result = new ArrayList<>();
        Long beginTimestamp = null;
        String[] lines = logString.split("\n");

        for (String line: lines) {

            if (line.contains(beginString)){
                beginTimestamp = Long.parseLong(line.replaceFirst(beginString, ""));
            } else if (line.contains(endString)){
                result.add(Long.toString(Long.parseLong(line.replaceFirst(endString, "")) - beginTimestamp));
                beginTimestamp = null;
            }
        }

        return result;

    }

    /**
     * Get indices for single file
     * @param file filepath
     * @param beginString begin of an operation
     * @param endString end of an operation
     * @return indices
     * @throws IOException
     */
    public static Map<String, String> getIndices (String file, String beginString, String endString) throws IOException {

        String lines = getLinesFromFile(file, beginString, endString);

        List<String> deltas = convertFromString(lines, beginString, endString);

        HashMap<String, String> results = new HashMap<>();

        List<Double> doubleCollection = deltas
                .stream()
                .map(deltaS -> Double.parseDouble(deltaS))
                .collect(Collectors.toList());


        final double avg = doubleCollection.stream().mapToDouble(d->d).average().getAsDouble();
        final double count = (double) doubleCollection.stream().mapToDouble(d->d).count();

        double sumOfSquares = doubleCollection.stream().mapToDouble(d->d).map(e -> Math.pow(e - avg, 2)).sum();
        double popVariance = sumOfSquares / count;
        double sampleVariance = sumOfSquares / (count - 1);
        double stdDev = Math.sqrt(popVariance);

        results.put("avgDemand", Double.toString(avg));
        results.put("popVar", Double.toString(popVariance));
        results.put("sampleVar", Double.toString(sampleVariance));
        results.put("stdDev", Double.toString(stdDev));
        results.put("throughput", Double.toString( 1.0 / avg ));
        results.put("NumberOfInputs", Double.toString(count));
        results.put("totalExecTime", Double.toString(getTotalExecTime(file)));

        return results;
    }

    /**
     * Given a path to log -> return total execution time
     */
    public static double getTotalExecTime (String path) {

        long begin, end;
        String last = "";
        try
        {
            BufferedReader is = new BufferedReader(new FileReader(path));
            is.readLine();
            String s = is.readLine();
            String[] s1 = s.split("_");

            begin = Long.parseLong(s1[s1.length-1]);

            while (s != null && !s.contains("CLOSING")){
                last = s;
                s = is.readLine();
            }

            s1 = last.split("_");

            end = Long.parseLong(s1[s1.length-1]);

            return end - begin;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Put on file all statistics for all datasets
     * @param folder path to folder
     */
    static void getStatisticsForEachDataset (String folder, String beginString, String endString) throws IOException {

        Stream<String> validPaths = Files
                .walk(Paths.get(folder))
                .filter(file -> new File(file.toUri()).isFile() && (file.getFileName().toString().endsWith(".txt") || file.getFileName().toString().endsWith(".log")))
                .filter(file -> {
                    try {
                        System.out.println(file);
                        return FileUtils.readFileUtf8(new File(file.toUri())).contains(beginString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(file -> file.toString());

        List<Tuple2<String, Map<String, String>>> statistics = validPaths.map(pathString -> {
            try {
                return new Tuple2<>(pathString, getIndices(pathString, beginString, endString));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());


        //region:general statistics
        BufferedWriter writer = new BufferedWriter(new FileWriter(folder + File.separator + "statistics.txt"));

        statistics.forEach(statTuple -> {
            try {
                writer.write(statTuple.f0);
                writer.newLine();
                writer.write(statTuple.f1.toString());
                writer.newLine();
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();

        //endregion

        Set<String> listOfStatistics = statistics.get(0).f1.keySet();

        listOfStatistics.forEach(s -> {
            try {
                BufferedWriter writerS = new BufferedWriter(new FileWriter(folder + File.separator + s + ".txt"));

                statistics.forEach(statTuple -> {
                    try {

                        List<String> strings = Arrays.asList(statTuple.f0.replaceAll(folder, "").split("/"));
                        strings.remove("");
                        String name = strings.get(0).replace("dataset", "").replace(".txt", "");

                        writerS.write(name + "\t" + statTuple.f1.get(s));
                        writerS.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                writerS.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
