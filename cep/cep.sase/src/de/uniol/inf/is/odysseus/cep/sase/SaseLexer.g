lexer grammar SaseLexer;

@lexer::header { 
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.cep.sase;
} 

CREATE 	: ('C'|'c')('R'|'r')('E'|'e')('A'|'a')('T'|'t')('E'|'e');
STREAM  : ('S'|'s')('T'|'t')('R'|'r')('E'|'e')('A'|'a')('M'|'m');
VIEW: ('V'|'v')('I'|'i')('E'|'e')('W'|'w');
PATTERN : ('P'|'p')('A'|'a')('T'|'t')('T'|'t')('E'|'e')('R'|'r')('N'|'n');
WHERE : ('W'|'w')('H'|'h')('E'|'e')('R'|'r')('E'|'e');
WITHIN	:('W'|'w')('I'|'i')('T'|'t')('H'|'h')('I'|'i')('N'|'n');
RETURN 	: ('R'|'r')('E'|'e')('T'|'t')('U'|'u')('R'|'r')('N'|'n');	 
SEQ : ('S'|'s')('E'|'e')('Q'|'q');
ALT :('A'|'a')('L'|'l')('T'|'t');
LEFTCURLY : '{';
RIGHTCURLY : '}';	
AND : ('A'|'a')('N'|'n')('D'|'d');	
AS : ('A'|'a')('S'|'s');
LEN 	: ('L'|'l')('E'|'e')('N'|'n');

BBRACKETLEFT :  '[';
BBRACKETRIGHT :  ']';

WEEK: ('W'|'w')('E'|'e')('E'|'e')('K'|'k')('S'|'s')?;
DAY: ('D'|'d')('A'|'a')('Y'|'y')('S'|'s')?;
HOUR: ('H'|'h')('O'|'o')('U'|'u')('R'|'r')('S'|'s')?; 
MINUTE: ('M'|'m')('I'|'i')('N'|'n')('U'|'u')('T'|'t')('E'|'e')('S'|'s')?;
SECOND:	('S'|'s')('E'|'e')('C'|'c')('O'|'o')('N'|'n')('D'|'d')('S'|'s')?;  
MILLISECOND:('M'|'m')('I'|'i')('L'|'l')('L'|'l')('I'|'i')('S'|'s')('E'|'e')('C'|'c')('O'|'o')('N'|'n')('D'|'d')('S'|'s')? ;


SKIP_TILL_NEXT_MATCH: ('S'|'s')('K'|'k')('I'|'i')('P'|'p')('_')('T'|'t')('I'|'i')('L'|'l')('L'|'l')('_')('N'|'n')('E'|'e')('X'|'x')('T'|'t')('_')('M'|'m')('A'|'a')('T'|'t')('C'|'c')('H'|'h');
SKIP_TILL_ANY_MATCH:	  ('S'|'s')('K'|'k')('I'|'i')('P'|'p')('_')('T'|'t')('I'|'i')('L'|'l')('L'|'l')('_')('A'|'a')('N'|'n')('Y'|'y')('_')('M'|'m')('A'|'a')('T'|'t')('C'|'c')('H'|'h');
STRICT_CONTIGUITY:	  ('S'|'s')('T'|'t')('R'|'r')('I'|'i')('C'|'c')('T'|'t')('_')('C'|'c')('O'|'o')('N'|'n')('T'|'t')('I'|'i')('G'|'g')('U'|'u')('I'|'i')('T'|'t')('Y'|'y');
PARTITION_CONTIGUITY:	  ('P'|'p')('A'|'a')('R'|'r')('T'|'t')('I'|'i')('T'|'t')('I'|'i')('O'|'o')('N'|'n')('_')('C'|'c')('O'|'o')('N'|'n')('T'|'t')('I'|'i')('G'|'g')('U'|'u')('I'|'i')('T'|'t')('Y'|'y');

AVG : ('A'|'a')('V'|'v')('G'|'g');
MIN : ('M'|'m')('I'|'i')('N'|'n');
MAX : ('M'|'m')('A'|'a')('X'|'x');
SUM :	('S'|'s')('U'|'u')('M'|'m');
COUNT : ('C'|'c')('O'|'o')('U'|'u')('N'|'n')('T'|'t');


PLUS	: '+';
MINUS	: '-';
POINT	: '.';
DIVISION: '/';
MULT	: '*';

COMPAREOP : '<='|'>='|'!='|'<'|'>';
SINGLEEQUALS : '=';
EQUALS 	: '==';
ASSIGN  : ':=';

NOTSIGN	: '~';
	
COMMA	: ',';

LBRACKET 
	: '(';
RBRACKET 
	: ')';

BOOLEAN: TRUE|FALSE;
fragment TRUE : ('T'|'t')('R'|'r')('U'|'u')('E'|'e');
fragment FALSE: ('F'|'f')('A'|'a')('L'|'l')('S'|'s')('E'|'e');

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








