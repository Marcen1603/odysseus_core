parser grammar SaseParser;

options { 
	output = AST;
	tokenVocab = SaseLexer;
	backtrack = true;
	ASTLabelType=CommonTree;
}

tokens{
	KSTATE;
	STATE;
	KTYPE;
	TYPE;
	WHEREEXPRESSION;
	MATHEXPRESSION;
	TERM;
	ATTRIBUTE;
	KATTRIBUTE;
	PARAMLIST;
	KMEMBER;
	MEMBER;
	COMPAREEXPRESSION;
	IDEXPRESSION;
	AGGREGATION;
	CREATESTREAM;
	QUERY;
	NOT;
	PREV;
	FIRST;
	CURRENT;
}

	
@header { 
	package de.uniol.inf.is.odysseus.cep.sase; 
}

start 	: createStmt|
	  queryStmt;


createStmt
	: CREATE STREAM NAME queryStmt -> ^(CREATESTREAM NAME queryStmt);
	

queryStmt:  patternPart wherePart? withinPart? returnPart? -> ^(QUERY patternPart wherePart? withinPart? returnPart?)
	;
	
		
withinPart
	: WITHIN NUMBER timeunit? -> ^(WITHIN NUMBER timeunit?)
	;
	
timeunit: WEEK|DAY|HOUR|MINUTE|SECOND|MILLISECOND;
	
wherePart
	: WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^(WHERE skipPart whereExpressions) |
	  WHERE	whereExpressions -> ^(WHERE whereExpressions)
	;

skipPart
	: skipMethod LBRACKET parameterList RBRACKET 
	-> ^(skipMethod parameterList)
	;

skipMethod:
  SKIP_TILL_NEXT_MATCH
  |SKIP_TILL_ANY_MATCH
  |STRICT_CONTIGUITY
  |PARTITION_CONTIGUITY;
  	
patternPart 
	: PATTERN patternDecl -> ^(patternDecl)
	;
	
returnPart
	:	 RETURN attributeTerm (COMMA attributeTerm)* -> ^(RETURN attributeTerm*)
	;
	
patternDecl
	:	SEQ LBRACKET stateDef (COMMA stateDef)* RBRACKET -> ^(SEQ stateDef*)
	;
		
	
stateDef :	(NOTSIGN)? ktypeDefinition  -> ^(KSTATE ktypeDefinition NOTSIGN?)
		| (NOTSIGN)? typeDefinition  -> ^(STATE typeDefinition NOTSIGN?)  
		| (NOTSIGN)? LBRACKET ktypeDefinition RBRACKET -> ^(KSTATE ktypeDefinition NOTSIGN?)
		| (NOTSIGN)? LBRACKET typeDefinition RBRACKET -> ^(STATE typeDefinition NOTSIGN?)  
	; 

typeDefinition
	:	NAME sAttributeName
	;

ktypeDefinition
	:	NAME PLUS! kAttributeName
	;

	
parameterList 
	:	attributeName (COMMA attributeName)* -> ^(PARAMLIST attributeName*)
	;
	
attributeName
	:	 kAttributeName -> ^(KATTRIBUTE NAME) 
		|sAttributeName -> ^(ATTRIBUTE NAME)
	;  
	
kAttributeName
	:	NAME  BBRACKETLEFT! BBRACKETRIGHT!   
	;

sAttributeName
	:	NAME 
	;
	
kAttributeUsage
	: NAME  current  -> ^(NAME CURRENT)
	 |NAME first -> ^(NAME FIRST)
	 |NAME last -> ^(NAME PREV)
	 |NAME len -> ^(NAME LEN)
	;
	
current	:	
	BBRACKETLEFT name=NAME BBRACKETRIGHT {$name.getText().equalsIgnoreCase("I")}?
	;
	
first	:	
	BBRACKETLEFT number=NUMBER BBRACKETRIGHT {Integer.parseInt($number.getText()) == 1}?
	;

last	:
	BBRACKETLEFT name=NAME MINUS number=NUMBER BBRACKETRIGHT  {$name.getText().equalsIgnoreCase("I") && Integer.parseInt($number.getText()) == 1}?	
	;
	
len	:
	BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT	
	;
	
whereExpressions
	:	expression (AND expression)* -> ^(WHEREEXPRESSION expression*)
	;
	
expression
	:	BBRACKETLEFT NAME BBRACKETRIGHT -> ^(IDEXPRESSION NAME) |
		f1=mathExpression (op=SINGLEEQUALS|op=COMPAREOP) f2=mathExpression ->  ^(COMPAREEXPRESSION $f1 $op $f2)
	;
	
mathExpression:	mult ((PLUS^|MINUS^) mult)* 
	;

mult 	:	term ((MULT^|DIVISION^) term)* 
;	


term:	attributeTerm |
		value|
		LBRACKET! mathExpression RBRACKET! 
	;
	
attributeTerm: aggregation |
    kAttributeUsage POINT NAME -> ^(KMEMBER kAttributeUsage NAME)|
    aName=NAME POINT member=NAME -> ^(MEMBER $aName $member)
	;
	
aggregation
	:	 saveaggop LBRACKET var=NAME BBRACKETLEFT POINT POINT name=NAME MINUS number=NUMBER BBRACKETRIGHT POINT member=NAME RBRACKET {$name.getText().equalsIgnoreCase("I") && Integer.parseInt($number.getText()) == 1}?
		 -> ^(AGGREGATION saveaggop $var $member )
		 | AVG LBRACKET var=NAME BBRACKETLEFT POINT POINT name=NAME MINUS number=NUMBER BBRACKETRIGHT POINT member=NAME RBRACKET   {$name.getText().equalsIgnoreCase("I") && Integer.parseInt($number.getText()) == 1}?
		 -> ^(DIVISION ^(AGGREGATION SUM $var $member) ^(AGGREGATION COUNT $var $member) ) 
		 |saveaggop LBRACKET var=NAME BBRACKETLEFT BBRACKETRIGHT (POINT member=NAME)? RBRACKET 
		 -> ^(AGGREGATION saveaggop $var $member?)
		 | AVG LBRACKET var=NAME BBRACKETLEFT BBRACKETRIGHT (POINT member=NAME)? RBRACKET 
		 -> ^(DIVISION ^(AGGREGATION SUM $var $member?) ^(AGGREGATION COUNT $var $member?) )     
		 ; 

saveaggop 
	: MIN|MAX|SUM|COUNT	
	;
	
value 	:	 NUMBER | STRING_LITERAL;
	
	