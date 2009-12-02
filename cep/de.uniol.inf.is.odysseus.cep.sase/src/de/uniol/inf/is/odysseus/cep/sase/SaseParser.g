parser grammar SaseParser;

options { 
	output = AST;
	tokenVocab = SaseLexer;
}

tokens{
	STATE;
	KTYPE;
	TYPE;
	WHERESTRAT;
	WHEREEXPRESSION;
	EXPRESSION;
	ATTRIBUTE;
	KATTRIBUTE;
	PARAMLIST;
	MEMBERACCESS;
}

	
@header { 
	package de.uniol.inf.is.odysseus.cep.sase; 
}

query	:  (fromPart)? patternPart (wherePart)? (withinPart)?
	;
	
fromPart: FROM NAME (COMMA NAME)* 
	;
	
withinPart
	: WITHIN NUMBER TIMEUNIT -> ^(WITHIN NUMBER TIMEUNIT)
	;
	
wherePart
	: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^(WHERE wherePart1 whereExpressions) |
	  WHERE	whereExpressions -> ^(WHERE whereExpressions)
	;
	
patternPart 
	: PATTERN patternDecl -> ^(PATTERN patternDecl)
	;
	
	
patternDecl
	:	SEQ LBRACKET pItem (COMMA pItem)* RBRACKET -> ^(SEQ pItem*)
	;
		
	
pItem 	:	(NOT)? LBRACKET?  type=typeName variable=attributeName RBRACKET? -> ^(STATE $type $variable NOT?) 
	;
	
typeName:	NAME op=PLUS -> ^(KTYPE NAME $op) | NAME -> ^(TYPE NAME)
	;
	
wherePart1
	:	SKIP_METHOD LBRACKET parameterList RBRACKET -> ^(WHERESTRAT SKIP_METHOD parameterList)
	;
	
parameterList 
	:	attributeName(COMMA attributeName)* -> ^(PARAMLIST attributeName*)
	;
	
attributeName
	:	 kAttributeName|sAttributeName
	;
	
kAttributeName
	:	NAME  BBRACKETLEFT BBRACKETRIGHT  -> ^(KATTRIBUTE NAME) 
	;
	
kAttributeUsage
	: 	NAME BBRACKETLEFT BBRACKETRIGHT|
		NAME CURRENT|
		NAME PREVIOUS
	;
sAttributeName
	:	NAME -> ^(ATTRIBUTE NAME)
	;
	
whereExpressions
	:	expression (AND expression)* -> ^(WHEREEXPRESSION AND expression*)
	;
	
expression
	:	term COMPAREOP term -> ^(EXPRESSION term COMPAREOP term) | term COMPAREOP value -> ^(EXPRESSION term COMPAREOP value)
	;

term	:	sAttributeName POINT NAME -> ^(MEMBERACCESS sAttributeName NAME)|
		kAttributeName POINT NAME-> ^(MEMBERACCESS kAttributeName NAME)
	;
	
value 	:	 NUMBER | STRING_LITERAL;
	