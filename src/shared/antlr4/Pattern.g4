grammar Pattern;

temporalPattern :
    ( basicPattern ( (collectStreams|temporalVariable) (operation)+ (emission | evaluation) ';')* )+ EOF
    ;

collectStreams : '.collect(' temporalVariable (',' temporalVariable)* ')'; //EVERY is meaningless

basicPattern :  '.g()' triggerComputation?  (computation | selection)* extraction (operation)* emission ';';

computation : '.compute(' computationFunction ')' ;

selection : '.selectPartition(' selectionFunction ')' ;

extraction : '.extract(' ( 'EDGE'? (label ',')* label)? ')' ;

evaluation : '.evaluate(' Operator ',' value ',' fireEvent ')' ';' ;

operation : '.' operationFunction ;

computationFunction : functionName ',' label (', [' variable+ ']' )?;

/*selectionFunction :
    boolPredicate
    | ( label = freeVariable)+
	| ( selectionFunction BinBoolOperator selectionFunction )
    | ( UnaryBoolOperator selectionFunction )
	| ( '(' selectionFunction  ')' )
	| ( selectionFunction? 'EDGE [' selectionFunction ']')
	;*/

selectionFunction
    : boolPredicate selRecursion
    | (label '=' freeVariable (',' label '=' freeVariable)*)
    | UnaryBoolOperator selectionFunction selRecursion
    | '(' selectionFunction  ')' selRecursion
    ;
selRecursion
    : BinBoolOperator selectionFunction selRecursion
    | 'EDGE[' selectionFunction ']' selRecursion
    |
    ;

boolPredicate : (label | temporalVariable) Operator ( value | temporalVariable | label) ;

freeVariable
    : '$FREE'
    | variable '.' label
    ;

operationFunction
    : ( 'map' | 'flatmap' | 'reduce' | 'filter' ) '(' functionName')'
    | ('groupby' | 'collect') '(' label ')'
    | 'avg' | 'max' | 'min' | 'count'
    ;

triggerComputation :
    triggerInput
    | triggerTemporal
    | triggerSensitivity
    ;

triggerInput : '.trigger(' ('edge' | 'vertex') ( 'addition' | 'deletion' | 'update' ) 'as' variable ('[' boolPredicate* ']')? ')' ;

triggerTemporal : '.trigger(' Timeunit ')' ;

triggerSensitivity : '.trigger(' variable ')' ;

emission : '.emit(' variable ')' ;

temporalVariable
    : variable
    | variable Timeunit 'ago'
    | variable 'every'? 'within' Timeunit ;

functionName : Litterals ;

label : Litterals ;

value : '\''Litterals '\'' ;

variable : '$' Litterals ;

fireEvent : '\"' Litterals '\"' ;

// todo: Definire la precedenza degli operatori

//Flexer

Operator : ('<'| '>'| '=') ;

BinBoolOperator : 'and' | 'or' ;

UnaryBoolOperator: 'not' ;

Timeunit : [0-9]+ ('s' | 'm' | 'h') ;

Litterals : ( [a-z] | [A-Z] | [0-9] )+ ;

Skip : [ \t\n]+ -> skip;