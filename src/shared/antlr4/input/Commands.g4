grammar Commands;

/*
    This grammar will handle inputs to the system.

    Some examples:

    "vertex insert: hello, lab1=4, lab2 = true , 3462",
    "edge delete: hello1, hello2, lab1=4 , 3462",
    "vertex update: hello, 3462",
    "vertex insert: hello, lab1=[4, pluto] , 3462"

*/

clientCommand:
    updateCommand EOF
    ;

updateCommand:
    ( vertexUpdate | edgeUpdate ) ',' (labelValues ',')? timestamp
    ;

vertexUpdate:
    'vertex' updateType ':' identifier
    ;

edgeUpdate:
    'edge' updateType ':' edgeIdentifier
    ;

labelValues:
    identifier '=' value ( ',' identifier '=' value )*
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
    ( Alphanumerical | Number )+
    ;

//  Flexer

Alphanumerical : ( [a-z] | [A-Z] )+ ;

Number: [0-9]+ ;

Skip : [ \t\n]+ -> skip;