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
	ASSIGNMENT;
	IDEXPRESSION;
	AGGREGATION;
	CREATEVIEW;
	QUERY;
	NOT;
	PREV;
	FIRST;
	CURRENT;
	ENDSAT;
}

	
@header { 
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

@rulecatch{
catch(RecognitionException e){
  throw e;
}
}
start 	: createStmt|
	  queryStmt;


createStmt
	: CREATE VIEW NAME queryStmt -> ^(CREATEVIEW NAME queryStmt);
	

queryStmt:  patternPart wherePart? withinPart? endsAtPart? returnPart -> ^(QUERY patternPart wherePart? withinPart? endsAtPart? returnPart)
	;
	
		
withinPart
	: WITHIN NUMBER timeunit? -> ^(WITHIN NUMBER timeunit?)
	;
	
endsAtPart
  : ENDS AT attributeTerm -> ^(ENDSAT attributeTerm)
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
	: PATTERN patternDecl* -> ^(PATTERN patternDecl*)
	;
	
returnPart
	:	 RETURN attributeTerm (COMMA attributeTerm)* (AS NAME)?-> ^(RETURN attributeTerm* NAME?)
	;
	
patternDecl
	: 	seqPatternDecl
	;
	
seqPatternDecl
	: SEQ LBRACKET stateDef (COMMA stateDef)* RBRACKET -> ^(SEQ stateDef*)
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
	:	NAME PLUS kAttributeName
	;

	
parameterList 
	:	attributeName (COMMA attributeName)* -> ^(PARAMLIST attributeName*)
	;
	
attributeName
	:	 kAttributeName -> ^(KATTRIBUTE NAME) 
		|sAttributeName -> ^(ATTRIBUTE NAME)
	;  
	
kAttributeName
	:	NAME  BBRACKETLEFT BBRACKETRIGHT
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
	:	expression ((AND|COMMA) expression)* -> ^(WHEREEXPRESSION expression*)
	;
	
expression
	:	BBRACKETLEFT NAME BBRACKETRIGHT -> ^(IDEXPRESSION NAME) |
		f1=mathExpression (op=SINGLEEQUALS|op=COMPAREOP) f2=mathExpression ->  ^(COMPAREEXPRESSION $f1 $op $f2) |
		sAttributeName ASSIGN mathExpression -> ^(ASSIGNMENT sAttributeName mathExpression)
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
	:	 aggop LBRACKET var=NAME BBRACKETLEFT POINT POINT name=NAME MINUS number=NUMBER BBRACKETRIGHT POINT member=NAME RBRACKET {$name.getText().equalsIgnoreCase("I") && Integer.parseInt($number.getText()) == 1}?
		 -> ^(AGGREGATION aggop $var $member )
		 |aggop LBRACKET var=NAME BBRACKETLEFT BBRACKETRIGHT (POINT member=NAME)? RBRACKET 
		 -> ^(AGGREGATION aggop $var $member?)
		 ; 

aggop 
	: MIN|MAX|SUM|COUNT|AVG
	;
	
value 	:	 NUMBER | BOOLEAN | STRING_LITERAL ;
	
	