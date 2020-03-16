package shared.resources;

import org.apache.flink.api.java.utils.ParameterTool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExtractVarEmission {

    public static void main(String[] args) {

        ParameterTool parParser = ParameterTool.fromArgs(args);

        String keep = parParser.get("keep", "finished GC: _AFTER_EMISSION_MEM_");
        String path = parParser.get("path", "/home/pietro/Logs/output172.31.28.239.txt");
        int oneEvery = parParser.getInt("oneEvery", 2);

        List<String> linesFromFile = getLinesFromFile(path, keep);

        linesFromFile = selectOneEvery(linesFromFile, oneEvery);

        System.out.println("Total Survived Lines= " + linesFromFile.size());
        System.out.println("________________________________________");
        printLines(linesFromFile);


    }


    public static ArrayList<String> getLinesFromFile (String path, String keep) {
        ArrayList<String> result = new ArrayList<>();

        try (Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> {
                if (s.contains(keep)) {
                    result.add(s.replace(keep, "").trim());
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> selectOneEvery(List<String> input, int oneEvery) {

        ArrayList<String> filtered = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            if ( ( i % oneEvery ) == 0)
                filtered.add(input.get(i));
        }
        return filtered;

    }

    public static void printLines (List<String> list){
        list.forEach(System.out::println);

    }

}
