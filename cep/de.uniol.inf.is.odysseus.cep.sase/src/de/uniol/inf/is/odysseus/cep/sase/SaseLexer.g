lexer grammar SaseLexer;

@lexer::header { package de.uniol.inf.is.odysseus.cep.sase; } 

PATTERN : 'PATTERN'|'pattern'|'Pattern';
WHERE : 'where'|'WHERE'|'Where';
WITHIN	: 'WITHIN'|'within'|'Within';
RETURN 	: 'RETURN'|'Return'|'return';	 
SEQ : 'SEQ'|'seq'|'Seq';
LEFTCURLY : '{';
RIGHTCURLY : '}';	
AND :'AND'|'and'|'And';
FIRST :'[1]';
CURRENT :'[i]';	
PREVIOUS :  '[i-1]' ;	
ALLTOPREVIOUS :	 '[..i-1]';
LAST 	:	 '['NAME'.LEN]';

BBRACKETLEFT :  '[';
BBRACKETRIGHT :  ']';

TIMEUNIT: 'hour'| 'minute' | 'second' | 'day' | 'millisecond' | 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds';

SKIP_METHOD
	: 'skip_till_next_match'|'skip_till_any_match'|'strict_contiguity'|'partition_contiguity';

AGGREGATEOP : 'Avg' | 'Min' | 'Max' | 'Sum' | 'Count'
		;

PLUS	: '+';
MINUS	: '-';
POINT	: '.';
DIVISION: '/';
MULT	: '*';

COMPAREOP : '<='|'>='|'!='|'<'|'>';
SINGLEEQUALS : '=';
EQUALS 	: '==';

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








