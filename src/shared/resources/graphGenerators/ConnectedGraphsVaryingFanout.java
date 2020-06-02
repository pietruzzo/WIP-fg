package shared.resources.graphGenerators;

import org.apache.commons.collections.list.TreeList;
import org.apache.flink.api.java.utils.ParameterTool;
import shared.Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class ConnectedGraphsVaryingFanout {

    public static void main(String[] args) throws FileNotFoundException {

        final ParameterTool param = ParameterTool.fromArgs(args);

        final int size = Integer.parseInt(param.get("size", "1000"));

        final String outputPath = param.get("name", "./");

        final double finalFanout = param.getDouble("finalValue", 10.0);

        final double delta = param.getDouble("increment", 1.0);

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

        //region: add Edges to Connect Graph
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

        //endregion

        double iterations = (finalFanout - 1.0) / delta;
        int edgesToAdd = (int) Math.floor(delta * size);

        //Write fanout 1
        String fileName = outputPath + 1 + ".txt";
        generateGraph(fileName, usedVertices, edges, updates);

        //Write fanout >= 1
        for (int j = 0; j < iterations; j++) {

            //add Edges
            for (int i = 0; i < edgesToAdd; i++) {
                String source = usedVertices.get(randomExtractor.nextInt(size));
                String dest = usedVertices.get(randomExtractor.nextInt(size));

                if (!source.equals(dest) && !edges.get(source).contains(dest) && !edges.get(dest).contains(source)) {
                    edges.get(source).add(dest);
                } else {
                    i = i-1;
                }
            }

            //Write edgesToFile
            fileName = outputPath + (j+2) + ".txt";
            generateGraph(fileName, usedVertices, edges, updates);
        }



    }

    static private void generateGraph (String outputPath, List<String> vertices, Map<String, List<String>> edges, int updates) throws FileNotFoundException {

        Random randomExtractor = new Random();
        int size = vertices.size();

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


        PrintWriter out = Utils.getFileWriter(outputPath);

        for (String usedVertex : vertices) {
            out.println("vertex insert: " + usedVertex + ", "  + timestamp.next());
        }

        edges.forEach((key, value) -> value
                .forEach(edge -> out.println("edge insert: " + key + ", " + edge + ", "  + timestamp.next())));


        //Generate updates


        for (int i = 0; i < updates; i++) {
            out.println("vertex update: " + vertices.get(randomExtractor.nextInt(size)) + ", labelU = A ,"+ timestamp.next());
        }

        out.close();
    }

}

