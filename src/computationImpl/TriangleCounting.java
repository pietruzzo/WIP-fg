package computationImpl;

import akka.japi.Pair;
import shared.AkkaMessages.StepMsg;
import shared.computation.Computation;
import shared.computation.Vertex;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of triangle counting
 *
 * Parameters:
 *      0: "cyclic" for cyclic triangles, acyclic otherwise
 *
 * Return value label:
 *      0: for resultLabel
 */
public class TriangleCounting implements Computation {

    private boolean cyclic;
    private String returnLabel;

    private ConcurrentHashMap<String, ArrayList<ArrayList<String>>> results;



    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {


        //If acyclic -> STOP at iteration step 2
        if (!cyclic && iterationStep == 2){

            ArrayList<ArrayList<String>> collectedBefore = results.get(vertex.getNodeId());
            ArrayList<ArrayList<String>> triangles = results.get(vertex.getNodeId());

            incoming
                    .forEach(stepMessage -> {
                        ((ArrayList<ArrayList<String>>)stepMessage.computationValues)
                                .forEach(singleTriangle -> {

                                    for (ArrayList<String> oneElementList: collectedBefore) {
                                        if (singleTriangle.get(0).equals(oneElementList.get(0))) {
                                            ArrayList<String> completeTriangle = new ArrayList<>();
                                            completeTriangle.add(vertex.getNodeId());
                                            completeTriangle.addAll(singleTriangle);
                                            triangles.add(completeTriangle);
                                            break;
                                        }
                                    }

                                });
                    });

            results.put(vertex.getNodeId(), triangles);
            return null;
        }

        //If cyclic -> STOP at iteration step 3
        if (cyclic && iterationStep == 3){

            ArrayList<ArrayList<String>> triangles = new ArrayList<>();

            incoming
                    .forEach(stepMessage -> {
                        ((ArrayList<ArrayList<String>>)stepMessage.computationValues)
                                .forEach(singleTriangle -> {
                                    if (singleTriangle.get(0).equals(vertex.getNodeId())) {
                                        triangles.add(singleTriangle);
                                    }
                                });
                    });

            results.put(vertex.getNodeId(), triangles);
            return null;
        }

        //Collect elements
        ArrayList<ArrayList<String>> collected = new ArrayList<>();

        incoming
                .forEach(stepMsg -> {
            collected.addAll((ArrayList<ArrayList<String>>) stepMsg.computationValues);
        });

        //If acyclic -> store collected
        results.computeIfAbsent(vertex.getNodeId(), value -> new ArrayList<>());
        ArrayList<ArrayList<String>> currentState = results.get(vertex.getNodeId());
        currentState.addAll(collected);

        //Prepare outgoing message
        List<StepMsg> outgoingMsgs = new ArrayList<>();

        //Append myNodeId
        ArrayList<ArrayList<String>> toSend = new ArrayList<>();
        collected.forEach(list -> {
            ArrayList<String> newSingleList = new ArrayList<>();
            newSingleList.addAll(list);
            newSingleList.add(vertex.getNodeId());
            toSend.add(newSingleList);
        });

        //Send message
        for (String destination: vertex.getEdges()) {
            outgoingMsgs.add( new StepMsg(destination, vertex.getNodeId(), toSend) );
        }
        return outgoingMsgs;

    }

    @Override
    public List<StepMsg> firstIterate(Vertex vertex) {

        //Initialize results
        results = new ConcurrentHashMap<>();

        //Flood first message
        List<StepMsg> outgoingMsgs = new ArrayList<>();

        ArrayList<ArrayList<String>> elementToSend = new ArrayList<>();
        ArrayList<String> initializationValue = new ArrayList<>();
        initializationValue.add(vertex.getNodeId());
        elementToSend.add(initializationValue);

        for (String destination: vertex.getEdges()) {
            outgoingMsgs.add( new StepMsg(destination, vertex.getNodeId(), elementToSend) );
        }
        return outgoingMsgs;

    }

    @Override
    public List<Pair<String, String[]>> computeResults(Vertex vertex) {

        List<Pair<String, String[]>> returnResults = new ArrayList<>();

        Set<String> triangles = new HashSet<>();

        results.get(vertex.getNodeId()).forEach(triangle -> {

            String triangleString = triangle
                    .stream()
                    .reduce((e1, e2) -> e1.compareTo(e2) < 0 ? e1 + " " + e2 : e2 + " " + e1)
                    .get();

            triangles.add(triangleString);
        });

        Pair<String, String[]> pair = new Pair<>(this.returnLabel, (String[])returnResults.toArray());
        returnResults.add(pair);
        return returnResults;
    }

    /**
     * @param parameters none
     * @param resultLabelsNames 1 paramenter that define name or returned value
     */
    @Override
    public void preInitialize(String[] parameters, String[] resultLabelsNames) {

        if (parameters[0].equals("cyclic"))
            cyclic = true;

        returnLabel = resultLabelsNames[0];

    }

}
