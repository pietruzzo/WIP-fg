# Flowgraph Framework


## Shortcuts

Antlr4 grammar: [PatternGrammar](src/shared/antlr4/pattern/Pattern.g4)

Main Class for debug: [Entry](src/DebugMain.java)

Put your computations here: [computations](src/shared/resources/computationImpl)

Put your patterns here: [InstalledPatterns](src/shared/resources/pattern.txt)

Put your initial graph here: [InitialGraph](src/shared/resources/Graph.txt)

## Some pattern examples

`.g().compute(PageRank , $varRet, [ maxIterations = '100', threshold = '0.0000001'] );`

`.g().select(label > '10' or lab1 == '5').extractV(label).emit($res);`

`.g().GroupV(label).extractV(label).emit($LAB);`