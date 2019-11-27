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
    'vertex' updateType ':' identifier ','
    ;

edgeUpdate:
    'edge' updateType ':' edgeIdentifier ','
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


edgeIdentifier:
    identifier ',' identifier
    ;

identifier:
    litterals
    ;

timestamp:
    Number
    ;

value:
    litterals
    | '[' (litterals ',')* litterals ']'
    ;

litterals :
    ( Alphanumerical | Number )+  {litterals = Al}
    ;

//  Flexer

Alphanumerical : ( [a-z] | [A-Z] )+ ;

Number: [0-9]+ ;

Skip : [ \t\n]+ -> skip;