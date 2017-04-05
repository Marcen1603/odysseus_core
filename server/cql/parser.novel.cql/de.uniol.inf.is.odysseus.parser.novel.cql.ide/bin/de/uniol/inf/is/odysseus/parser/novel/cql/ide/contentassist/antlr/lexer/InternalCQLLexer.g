/*
 * generated by Xtext 2.10.0
 */
lexer grammar InternalCQLLexer;

@header {
package de.uniol.inf.is.odysseus.parser.novel.cql.ide.contentassist.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;
}

NO_LAZY_CONNECTION_CHECK : ('N'|'n')('O'|'o')'_'('L'|'l')('A'|'a')('Z'|'z')('Y'|'y')'_'('C'|'c')('O'|'o')('N'|'n')('N'|'n')('E'|'e')('C'|'c')('T'|'t')('I'|'i')('O'|'o')('N'|'n')'_'('C'|'c')('H'|'h')('E'|'e')('C'|'c')('K'|'k');

INTERSECTION : ('I'|'i')('N'|'n')('T'|'t')('E'|'e')('R'|'r')('S'|'s')('E'|'e')('C'|'c')('T'|'t')('I'|'i')('O'|'o')('N'|'n');

DATAHANDLER : ('D'|'d')('A'|'a')('T'|'t')('A'|'a')('H'|'h')('A'|'a')('N'|'n')('D'|'d')('L'|'l')('E'|'e')('R'|'r');

CONNECTION : ('C'|'c')('O'|'o')('N'|'n')('N'|'n')('E'|'e')('C'|'c')('T'|'t')('I'|'i')('O'|'o')('N'|'n');

DIFFERENCE : ('D'|'d')('I'|'i')('F'|'f')('F'|'f')('E'|'e')('R'|'r')('E'|'e')('N'|'n')('C'|'c')('E'|'e');

IDENTIFIED : ('I'|'i')('D'|'d')('E'|'e')('N'|'n')('T'|'t')('I'|'i')('F'|'f')('I'|'i')('E'|'e')('D'|'d');

PARTITION : ('P'|'p')('A'|'a')('R'|'r')('T'|'t')('I'|'i')('T'|'t')('I'|'i')('O'|'o')('N'|'n');

TRANSPORT : ('T'|'t')('R'|'r')('A'|'a')('N'|'n')('S'|'s')('P'|'p')('O'|'o')('R'|'r')('T'|'t');

UNBOUNDED : ('U'|'u')('N'|'n')('B'|'b')('O'|'o')('U'|'u')('N'|'n')('D'|'d')('E'|'e')('D'|'d');

DATABASE : ('D'|'d')('A'|'a')('T'|'t')('A'|'a')('B'|'b')('A'|'a')('S'|'s')('E'|'e');

DISTINCT : ('D'|'d')('I'|'i')('S'|'s')('T'|'t')('I'|'i')('N'|'n')('C'|'c')('T'|'t');

PASSWORD : ('P'|'p')('A'|'a')('S'|'s')('S'|'s')('W'|'w')('O'|'o')('R'|'r')('D'|'d');

PROTOCOL : ('P'|'p')('R'|'r')('O'|'o')('T'|'t')('O'|'o')('C'|'c')('O'|'o')('L'|'l');

TRUNCATE : ('T'|'t')('R'|'r')('U'|'u')('N'|'n')('C'|'c')('A'|'a')('T'|'t')('E'|'e');

ADVANCE : ('A'|'a')('D'|'d')('V'|'v')('A'|'a')('N'|'n')('C'|'c')('E'|'e');

CHANNEL : ('C'|'c')('H'|'h')('A'|'a')('N'|'n')('N'|'n')('E'|'e')('L'|'l');

CONTEXT : ('C'|'c')('O'|'o')('N'|'n')('T'|'t')('E'|'e')('X'|'x')('T'|'t');

OPTIONS : ('O'|'o')('P'|'p')('T'|'t')('I'|'i')('O'|'o')('N'|'n')('S'|'s');

WRAPPER : ('W'|'w')('R'|'r')('A'|'a')('P'|'p')('P'|'p')('E'|'e')('R'|'r');

ATTACH : ('A'|'a')('T'|'t')('T'|'t')('A'|'a')('C'|'c')('H'|'h');

CREATE : ('C'|'c')('R'|'r')('E'|'e')('A'|'a')('T'|'t')('E'|'e');

EXISTS : ('E'|'e')('X'|'x')('I'|'i')('S'|'s')('T'|'t')('S'|'s');

HAVING : ('H'|'h')('A'|'a')('V'|'v')('I'|'i')('N'|'n')('G'|'g');

REVOKE : ('R'|'r')('E'|'e')('V'|'v')('O'|'o')('K'|'k')('E'|'e');

SELECT : ('S'|'s')('E'|'e')('L'|'l')('E'|'e')('C'|'c')('T'|'t');

SINGLE : ('S'|'s')('I'|'i')('N'|'n')('G'|'g')('L'|'l')('E'|'e');

STREAM : ('S'|'s')('T'|'t')('R'|'r')('E'|'e')('A'|'a')('M'|'m');

TENANT : ('T'|'t')('E'|'e')('N'|'n')('A'|'a')('N'|'n')('T'|'t');

ALTER : ('A'|'a')('L'|'l')('T'|'t')('E'|'e')('R'|'r');

FALSE : ('F'|'f')('A'|'a')('L'|'l')('S'|'s')('E'|'e');

GRANT : ('G'|'g')('R'|'r')('A'|'a')('N'|'n')('T'|'t');

GROUP : ('G'|'g')('R'|'r')('O'|'o')('U'|'u')('P'|'p');

MULTI : ('M'|'m')('U'|'u')('L'|'l')('T'|'t')('I'|'i');

STORE : ('S'|'s')('T'|'t')('O'|'o')('R'|'r')('E'|'e');

TABLE : ('T'|'t')('A'|'a')('B'|'b')('L'|'l')('E'|'e');

TUPLE : ('T'|'t')('U'|'u')('P'|'p')('L'|'l')('E'|'e');

UNION : ('U'|'u')('N'|'n')('I'|'i')('O'|'o')('N'|'n');

WHERE : ('W'|'w')('H'|'h')('E'|'e')('R'|'r')('E'|'e');

DROP : ('D'|'d')('R'|'r')('O'|'o')('P'|'p');

EACH : ('E'|'e')('A'|'a')('C'|'c')('H'|'h');

FILE : ('F'|'f')('I'|'i')('L'|'l')('E'|'e');

FROM : ('F'|'f')('R'|'r')('O'|'o')('M'|'m');

JDBC : ('J'|'j')('D'|'d')('B'|'b')('C'|'c');

ROLE : ('R'|'r')('O'|'o')('L'|'l')('E'|'e');

SINK : ('S'|'s')('I'|'i')('N'|'n')('K'|'k');

SIZE : ('S'|'s')('I'|'i')('Z'|'z')('E'|'e');

TIME : ('T'|'t')('I'|'i')('M'|'m')('E'|'e');

TRUE : ('T'|'t')('R'|'r')('U'|'u')('E'|'e');

USER : ('U'|'u')('S'|'s')('E'|'e')('R'|'r');

VIEW : ('V'|'v')('I'|'i')('E'|'e')('W'|'w');

WITH : ('W'|'w')('I'|'i')('T'|'t')('H'|'h');

AND : ('A'|'a')('N'|'n')('D'|'d');

NOT : ('N'|'n')('O'|'o')('T'|'t');

ExclamationMarkEqualsSign : '!''=';

LessThanSignEqualsSign : '<''=';

GreaterThanSignEqualsSign : '>''=';

AS : ('A'|'a')('S'|'s');

AT : ('A'|'a')('T'|'t');

BY : ('B'|'b')('Y'|'y');

IF : ('I'|'i')('F'|'f');

IN : ('I'|'i')('N'|'n');

ON : ('O'|'o')('N'|'n');

OR : ('O'|'o')('R'|'r');

TO : ('T'|'t')('O'|'o');

LeftParenthesis : '(';

RightParenthesis : ')';

Asterisk : '*';

PlusSign : '+';

Comma : ',';

HyphenMinus : '-';

FullStop : '.';

Solidus : '/';

Colon : ':';

Semicolon : ';';

LessThanSign : '<';

EqualsSign : '=';

GreaterThanSign : '>';

LeftSquareBracket : '[';

RightSquareBracket : ']';

CircumflexAccent : '^';

// Rules duplicated to allow inter-rule references

RULE_ID : ('a'..'z'|'A'..'Z'|'_'|':'|'$'|'{'|'}') ('a'..'z'|'A'..'Z'|'_'|':'|'$'|'{'|'}'|'0'..'9')*;

RULE_FLOAT : RULE_INT '.' RULE_INT;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' .|~(('\\'|'"')))* '"'|'\'' ('\\' .|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;
