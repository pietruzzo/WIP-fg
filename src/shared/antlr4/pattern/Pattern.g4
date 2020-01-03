grammar Pattern;

patternEntry
    :   (temporalPattern ';')* EOF
    ;

temporalPattern
    :   triggerComputation? graphProcessing emission? //Can emit Graph Variable only
    |   triggerComputation? graphProcessing extractStreamProcessing
    |   triggerComputation? collectStreamProcessing
    ;

graphProcessing
    :    '.g()' (computation | selection | partition)*
    |    temporalVariable (computation | selection | partition)*
    ;

collectStreams
    :   '.collect(' temporalVariable (',' temporalVariable)* ')'
    ;

extractStreamProcessing
    :   extraction (operation)* (emission | evaluation)
    ;

collectStreamProcessing
    :   (collectStreams | extraction) (operation)* (emission | evaluation)
    ;

computation
    :   '.compute(' computationFunction ')'
    ;

selection
    :   '.select(' selectionFunction ')'
    ;

partition
    :   '.GroupV(' partitionFunction ')'
    |   '.GroupE(' partitionFunction ')'
    ;

extraction
    :   '.extractV(' label (',' label)* ')'
    |   '.extractE(' label (',' label)* ')'
    ;

evaluation
    :   '.evaluate(' Operator ',' value ',' fireEvent ')'
    ;

operation
    :   '.' operationFunction
    ;

computationFunction
    :   functionName ',' computationReturnVariables ( computationParameters )?
    ;

computationReturnVariables
    :   variable (',' variable)*
    ;

computationParameters
    :   ', [' ( aliasedParameter ',')* aliasedParameter ']'
    ;

aliasedParameter
    :   Litterals '=' operands
    ;

//.......................................................

selectionFunction
    :   logicalExpression edgeSelection
    |
    ;

edgeSelection
    :   'EDGE[' (logicalExpression | ) ']'
    |
    ;

logicalExpression
    :    booleanAndExpression ( OR booleanAndExpression )*
    ;

booleanAndExpression
    :    unaryExpression ( AND unaryExpression )*
    ;

unaryExpression
    :    NOT? primaryExpression
    ;

primaryExpression
    :    '(' logicalExpression ')'
    |    boolPredicate
    ;

boolPredicate
    :   leftOp=operands Operator rightOp=operands
    ;

operands
    : (label | temporalVariable | value)
    ;

//..............................................

partitionFunction
    :   ((temporalVariable|label) (',' (temporalVariable|label))*)?
    ;

operationFunction
    :   ( 'map' | 'flatmap' | 'reduce' | 'filter' ) '(' functionName (tupleField (',' tupleField)*)? ')'
    |   ('groupby' | 'Merge') '(' tupleField (',' tupleField)* ')'
    |   'collect'
    |   oneFieldOperationAlias ('(' tupleField ')')?
    ;

oneFieldOperationAlias
    :   'avg' | 'max' | 'min' | 'count' | 'select'
    ;

triggerComputation
    :   triggerInput
    |   triggerTemporal
    |   triggerSensitivity
    ;

triggerInput
    :   '.trigger(' ('edge' | 'vertex') ( 'addition' | 'deletion' | 'update' ) 'as' variable ('[' boolPredicate* ']')? ')'
    ;

triggerTemporal
    :   '.trigger(' Timeunit ')'
    ;

triggerSensitivity
    :   '.trigger(' variable (',' variable)* ')'
    ;

emission
    :   '.emit(' variable ')'
    ;

temporalVariable
    :   variable
    |   variable Timeunit 'ago'
    |   variable 'every'? 'within' Timeunit
    ;

functionName
    :   Litterals
    ;

label
    :   Litterals
    ;

value
    :   '\''Litterals '\''
    ;

variable
    :   '$' Litterals
    ;

fireEvent
    :   '"' Litterals '"'
    ;

tupleField
    :   Litterals ('.' Litterals)*
    ;

//lexer

Operator : ('<'| '>'| '=' | '<=' | '>=') ;

//BinBoolOperator : 'and' | 'or' ;
NOT : 'not' ;
AND : 'and' ;
OR : 'or' ;


//UnaryBoolOperator: 'not' ;

Timeunit : ([0-9]+ ('s' | 'm' | 'h'))+ ;

Litterals : ( [a-z] | [A-Z] | [0-9] )+ ;

Skip : [ \t\n]+ -> skip;