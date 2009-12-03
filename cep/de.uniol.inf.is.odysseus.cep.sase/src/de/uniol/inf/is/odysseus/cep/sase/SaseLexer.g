lexer grammar SaseLexer;

@lexer::header { package de.uniol.inf.is.odysseus.cep.sase; } 
  
HAVING	: 'HAVING' ;
PATTERN : 'PATTERN';
WHERE : 'WHERE' ;
WITHIN	: 'WITHIN';
SEQ : 'SEQ';
LEFTCURLY : '{';
RIGHTCURLY : '}';	
AND :'AND'|'and';
FIRST :'[1]';
CURRENT :'[i]';	
PREVIOUS :  '[i-1]' ;	
ALLTOPREVIOUS :	 '[..i-1]' ;

TIMEUNIT: 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds';

SKIP_METHOD
	: 'skip_till_next_match'|'skip_till_any_match';

AGGREGATEOP : 'avg' | 'min' | 'max' | 'sum' | 'count'| 'AVG'|'MIN'|'MAX'|'SUM'|'COUNT' 
		;

BBRACKETLEFT :  '[';
BBRACKETRIGHT :  ']';

PLUS	: '+';
MINUS	: '-';
POINT	: '.';
MULT	: '*'; 	
COMPAREOP : '='|'<='|'>='|'!='|'<'|'>';

NOT	: '~';
	
COMMA	: ',';

LBRACKET 
	: '(';
RBRACKET 
	: ')';


NUMBER	:	 INTEGER | FLOAT;
fragment FLOAT	: INTEGER '.' DIGIT+;
fragment INTEGER : '0' | '1'..'9' ('0'..'9')*;

NAME	: LETTER (LETTER|DIGIT|'_'|':')*;
STRING_LITERAL : '"' NONCONTROL_CHAR* '"';

fragment NONCONTROL_CHAR : LETTER | DIGIT | SPACE;
fragment LETTER : LOWER|UPPER;
fragment LOWER 	: 'a'..'z';
fragment UPPER 	: 'A'..'Z';
fragment DIGIT  : '0'..'9';	
fragment SPACE 	: ' ' | '\t';

NEWLINE	: ('\r'? '\n')+  {$channel = HIDDEN;};

WHITESPACE : SPACE+ {$channel = HIDDEN;};








