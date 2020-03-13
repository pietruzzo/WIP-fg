package shared.resources.graphGenerators;

import org.apache.flink.api.java.utils.ParameterTool;
import shared.Utils;

import java.io.*;

public class ConvertToGraphX {

    public static void main(String[] args) throws IOException {

        final ParameterTool param = ParameterTool.fromArgs(args);

        final String path = param.get("path", "");

        final String fileName = param.get("filename", "dataset1M.txt");

        final String outputV = param.get("outV", "vertices.txt");
        final String outputE = param.get("outE", "edges.txt");

        BufferedReader reader = new BufferedReader(new FileReader(path+ File.separator+fileName));
        PrintWriter outV = Utils.getFileWriter(outputV);
        PrintWriter outE = Utils.getFileWriter(outputE);

        String line;

        while((line=reader.readLine())!=null)
        {
            if (line.startsWith("vertex insert:")){
                //Add to vertex file
                //vertex insert: v112, labelP = 2, 554
                line = line.replace("vertex insert:", "");
                //v112, labelP = 2, 554
                String vertex = line.split(",")[0].trim().replace("v", "");
                //112
                outV.println(vertex + " v" + vertex);


            } else if (line.startsWith("edge insert:")){
                //add to edge file
                //edge insert: v771, v840, labelP = 2, 1423
                line = line.replace("edge insert:", "");
                //v771, v840, labelP = 2, 1423
                String vertex1 = line.split(",")[0].trim().replace("v", "");
                String vertex2 = line.split(",")[1].trim().replace("v", "");
                //112
                outE.println(vertex1 + " " + vertex2);

            }
        }
        reader.close();
        outE.close();
        outV.close();
    }
}
