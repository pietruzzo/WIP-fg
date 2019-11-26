grammar Commands;

/*
    This grammar will handle inputs to the system.

    Some examples:

    vertex update: <vertexid>, <label1=value>, <label2=value>, timestamp

    edge insertion: <vertexid1>, <vertexid2>, <label1=value> ... , timestamp
*/

clientCommand:
    updateCommand EOF
    ;

updateCommand:
    ( vertexUpdate | edgeUpdate ) labelValues ',' timestamp
    ;

vertexUpdate:
    'vertex' updateType ':' identifier1 ','
    ;

edgeUpdate:
    'edge' updateType ':' identifier2 ','
    ;

labelValues:
    identifier '=' value ( ',' identifier '=' value )
    ;

vertexType:
    'vertex'
    |'edge'
    ;

updateType:
    'insert'
    |'delete'
    |'update'
    ;

identifier1:
    identifier
    ;

identifier2:
    identifier ',' identifier
    ;

identifier:
    litterals
    ;

timestamp:
    Number
    ;

value:
    identifier
    ;

litterals :
    ( Alphanumerical | Numbers )+  {litterals = Al}
    ;

//  Flexer

Alphanumerical : ( [a-z] | [A-Z] )+ ;

Number: [0-9]+ ;

Skip : [ \t\n]+ -> skip;