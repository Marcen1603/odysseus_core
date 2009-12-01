lexer grammar SaseLexer;

@lexer::header { package de.uniol.inf.is.odysseus.cep.sase; } 
  
PATTERN : 'PATTERN' | 'Pattern';
WHERE : 'WHERE' | 'Where';
WITHIN	: 'WITHIN' | 'Within';
SEQ : 'SEQ' | 'Seq';
EGAL : 'EGAL' | 'egal';

TIMEUNIT: 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds';

SKIP_METHOD
	: 'skip_till_next_match'|'skip_till_any_match';

KLEENEBRACKET
	:  '[]';
	
PLUS	: '+';

NOT	: '~';
	
COMMA	: ',';

LBRACKET 
	: '(';
RBRACKET 
	: ')';


NUMBER	:	 INTEGER | FLOAT;
fragment FLOAT	: INTEGER '.' DIGIT+;
fragment INTEGER : '0' | '1'..'9' DIGIT*;

TYPE	: UPPER (LETTER|DIGIT|'_')*;
NAME	: LOWER (LETTER|DIGIT|'_')*;
STRING_LITERAL : '"' NONCONTROL_CHAR* '"';

fragment NONCONTROL_CHAR : LETTER | DIGIT | SPACE;
fragment LETTER : LOWER|UPPER;
fragment LOWER 	: 'a'..'z';
fragment UPPER 	: 'A'..'Z';
fragment DIGIT  : '0'..'9';	
fragment SPACE 	: ' ' | '\t';

NEWLINE	: ('\r'? '\n')+  {$channel = HIDDEN;};

WHITESPACE : SPACE+ {$channel = HIDDEN;};








