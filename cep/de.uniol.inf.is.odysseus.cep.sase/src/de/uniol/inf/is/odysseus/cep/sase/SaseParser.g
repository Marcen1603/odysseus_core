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
	KMEMBER;
	MEMBER;
	COMPAREEXPRESSION;
	IDEXPRESSION;
	AGGREGATION;
}

	
@header { 
	package de.uniol.inf.is.odysseus.cep.sase; 
}

query	:  patternPart (wherePart)? (withinPart)?
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
	
typeName:	NAME op=PLUS? -> {$op != null}? ^(KTYPE NAME $op) 
			     -> ^(TYPE NAME)
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
	

sAttributeName
	:	NAME -> ^(ATTRIBUTE NAME)
	;
kAttributeUsage
	: 	NAME CURRENT |
		NAME FIRST|
		NAME PREVIOUS|
		NAME LAST
	;
	
whereExpressions
	:	expression (AND expression)* -> ^(WHEREEXPRESSION AND? expression*)
	;
	
expression
	:	 BBRACKETLEFT NAME BBRACKETRIGHT -> ^(IDEXPRESSION NAME) | f1=term SINGLEEQUALS f2=term ->  ^(COMPAREEXPRESSION $f1 EQUALS $f2) | f1=term COMPAREOP f2=term -> ^(COMPAREEXPRESSION $f1 COMPAREOP $f2)
	;

term	:	aggregation |
		kAttributeUsage POINT NAME -> ^(KMEMBER kAttributeUsage NAME)|
		aName=NAME POINT member=NAME -> ^(MEMBER $aName $member)|
		value		
	;
	
aggregation
	:	AGGREGATEOP LBRACKET var=NAME ALLTOPREVIOUS POINT member=NAME RBRACKET -> ^(AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member )
	;
	
value 	:	 NUMBER | STRING_LITERAL;
	