package shared.resources.graphGenerators;

import org.apache.commons.collections.list.TreeList;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import shared.Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class ConnectedGraph {

    public static void main(String[] args) throws FileNotFoundException {


        final ParameterTool param = ParameterTool.fromArgs(args);

        final int size = Integer.parseInt(param.get("size", "9000"));

        final String outputPath = param.get("name", "./dataset9k.txt");

        final double otherRandomEdges = param.getDouble("addedEdgesOnVerticesRatio", 1.0);

        final int updates = param.getInt("nUpdates", 100);

        TreeList vertices = new TreeList();
        List<String> usedVertices = new ArrayList<>();
        Map<String, List<String>> edges = new HashMap<>();

        Random randomExtractor = new Random();

        //Populate vertices
        for (int i = 0; i < size; i++) {
            String name = "v" + i;
            vertices.add(name);
        }

        int currentVertexIndex =  randomExtractor.nextInt(vertices.size());

        while (vertices.size() > 1) {
            String currentVertex = (String) vertices.get(currentVertexIndex);
            usedVertices.add(currentVertex);
            vertices.remove(currentVertexIndex);
            int nextVertexIndex = randomExtractor.nextInt(vertices.size());
            String nextVertex = (String) vertices.get(nextVertexIndex);

            //generate edge
            edges.put(currentVertex, new ArrayList<>());
            edges.get(currentVertex).add(nextVertex);

            currentVertexIndex = nextVertexIndex;
        }

        String currentVertex = (String) vertices.get(currentVertexIndex);
        usedVertices.add(currentVertex);
        edges.put(currentVertex, new ArrayList<>());

        if (otherRandomEdges != 0.0) {
            int edgesToAdd = (int) Math.floor(otherRandomEdges * size);

            for (int i = 0; i < edgesToAdd; i++) {
                String source = usedVertices.get(randomExtractor.nextInt(size));
                String dest = usedVertices.get(randomExtractor.nextInt(size));

                if (!source.equals(dest) && !edges.get(source).contains(dest) && !edges.get(dest).contains(source)) {
                    edges.get(source).add(dest);
                } else {
                    i = i-1;
                }
            }
        }

        //Timestamp progressive iterator
        Iterator<Integer> timestamp = new Iterator<>() {

            Integer i = 1;
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                i = i+1;
                return i-1;
            }
        };
        //Now add vertex labels
        List<Tuple2<String, List<String>>> labels = new ArrayList<>();

        Tuple2<String, List<String>> labelPartition = new Tuple2<>("labelP", new ArrayList<>());
        labelPartition.f1.add("1");
        labelPartition.f1.add("2");
        labelPartition.f1.add("3");
        labelPartition.f1.add("4");

        labels.add(labelPartition);
        PrintWriter out = Utils.getFileWriter(outputPath);

        for (String usedVertex : usedVertices) {
            out.println("vertex insert: " + usedVertex + ", " + generateLabelString(labels) + timestamp.next());
        }

        edges.forEach((key, value) -> value
                .forEach(edge -> out.println("edge insert: " + key + ", " + edge + ", " + generateLabelString(labels) + timestamp.next())));


        //Generate updates


        for (int i = 0; i < updates; i++) {
            out.println("vertex update: " + "v" + (i+100) + ", labelU = A ,"+ timestamp.next());
        }


        out.close();
    }


    /**
     *
     * @param labels label name, allowed value . If allowed labels contains null -> do not assign
     * @return
     */
    public static String generateLabelString (List<Tuple2<String, List<String>>> labels) {

        StringBuilder result = new StringBuilder();

        for (Tuple2<String, List<String>> label : labels) {
            String s = label.f1.get(new Random().nextInt(label.f1.size()));
            if (s != null) {
                result.append(label.f0).append(" = ").append(s).append(", ");
            }
        }
        return result.toString();
    }


}
