# Flowgraph Framework


## Deployment

[AkkaConfigFolder](conf/): modify and put this files in the same directory of jar

[config.properties](src/shared/resources/config.properties): modify it with your settings, Flowgraph will
looking for it in same directory of jar file

[PatternGrammarFolder](src/shared/antlr4/): antlr4 grammars for patterns and inputs 
(try Antlr4 plugin to check the syntax of your your pattern)

[Entry](src/DebugMain.java): entry point of application

[computations](src/shared/resources/computationImpl): VertexCentric computation available.
Adding a new computation requires recompilation of framework


## example of config.properties

patternPath = src/shared/resources/pattern.txt

datasetPath = ./dataset100k.txt


timingLog = true (can be processed by [script](src/shared/resources/LogToIndices.java))

spaceLog = true

outOnFile = true (put output on file instead of console)

performanceLogName = performance


numberOfSlaves = 1 (number of slave instances)

directedEdges = true

numRecordsAsInput = 120 (number of dataset entries used as system input)

autonomousMode = true (use dataset inputs and do not permit user interaction at runtime)

numOfWorkers = 4 (number of workers actor per instance)

appendLog = false (open log field in append mode)

debugLog = true (do not suppress INFO log)


logPath = ./log/ (log folder)

masterIp = 192.168.1.86 

masterPort = 6123

## Some example of partial patterns

>.g().compute(PageRank , $varRet, [ maxIterations = '100', threshold = '0.0000001'] );

>.g().select(label > '10' or lab1 == '5').extractV(label).emit($res);

>.g().SubGraphByV(labelP).compute(PageRank , $varRet, [ maxIterations = '10']);

>.g().SubGraphByV(labelP).select(labelP == '2') \
>.compute(PageRank , $varRet, [ maxIterations = '10']);

>.g().extractV(labelP).emit($varNode); \
>.collect($varNode 10-ms ago).emit($versionedVar);


> .g().compute(OutgoingEdges, $outDegree).emit($graph); \
>     .collect($outDegree).reduce(count).emit($totalEdges); \
>     .collect($totalEdges 10-ms ago, $totalEdges).map(diff f.value, s.value).emit($delta); \
>     .collect($delta).filter(greaterThan10 value).emit($triggerVar); 
>     .trigger($triggerVar).g().compute(PageRank, $rank, [maxIterations = '10']);