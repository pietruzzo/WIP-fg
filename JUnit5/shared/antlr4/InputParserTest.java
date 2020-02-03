package shared.antlr4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.AkkaMessages.modifyGraph.DeleteEdgeMsg;
import shared.AkkaMessages.modifyGraph.UpdateVertexMsg;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class InputParserTest {

    String[] debug;

    @BeforeEach
    void setUp() {

        debug = new String[]{
                "vertex insert: hello, lab1=4, lab2 = true , 3462",
                "edge delete: hello1, hello2, lab1=4 , 3462",
                "vertex update: hello, 3462",
                "vertex insert: hello, lab1=[4, pluto] , 3462"
        };

    }

    @Test
    void parse() {

        List<Serializable> collect = Arrays.stream(debug)
                .map(string -> InputParser.parse(string))
                .collect(Collectors.toList());









    }

    @Test
    void parseTypes() {

        List<Serializable> collect = Arrays.stream(debug)
                .map(string -> InputParser.parse(string))
                .collect(Collectors.toList());

        //Check types
        assert collect.get(0) instanceof UpdateVertexMsg;
        assert collect.get(1) instanceof DeleteEdgeMsg;
        assert collect.get(2) instanceof UpdateVertexMsg;
        assert collect.get(3) instanceof UpdateVertexMsg;

    }

    @Test
    void parseMessageIDs() {

        List<Serializable> collect = Arrays.stream(debug)
                .map(string -> InputParser.parse(string))
                .collect(Collectors.toList());

        //Check vertex/edge IDs
        assert ((UpdateVertexMsg)collect.get(0)).vertexName.equals("hello");
        assert ((DeleteEdgeMsg)collect.get(1)).getSourceName().equals("hello1");
        assert ((DeleteEdgeMsg)collect.get(1)).getDestinationName().equals("hello2");
    }

    @Test
    void parseMultipleSingleValue() {

        Serializable collect = InputParser.parse(debug[0]);


        //Check state multiple single value
        assert ((UpdateVertexMsg)collect).getAttributes().get(0).first().equals("lab1");
        assert ((UpdateVertexMsg)collect).getAttributes().get(1).first().equals("lab2");

        assert ((UpdateVertexMsg)collect).getAttributes().get(0).second()[0].equals("4");
        assert ((UpdateVertexMsg)collect).getAttributes().get(1).second()[0].equals("true");
    }

    @Test
    void parseMultivalue() {

        Serializable collect = InputParser.parse(debug[3]);

        //Check state multivalue
        assert ((UpdateVertexMsg)collect).getAttributes().get(0).first().equals("lab1");
        assert ((UpdateVertexMsg)collect).getAttributes().get(0).second()[0].equals("4");
        assert ((UpdateVertexMsg)collect).getAttributes().get(0).second()[1].equals("pluto");
    }
}