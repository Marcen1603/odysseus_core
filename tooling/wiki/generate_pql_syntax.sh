#!/bin/bash

# Find all logical operator
operator=$(find ../../ -name \*.java -type f |xargs grep "@LogicalOperator" | sed -rn 's/.*name\s?\=\s?\"([a-zA-Z]*)\".*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D' )

# Find all logical operator parameter
parameter=$(find ../../ -name \*.java -type f |xargs grep "@Parameter" | sed -rn 's/.*name\s?\=\s?\"([a-zA-Z]*)\".*/\U\1/p'  | sort -fu | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D' )

cat > shBrushPql.js <<- EOF
SyntaxHighlighter.brushes.Pql = function()
{
// Copyright 2014 Christian Kuka christian@kuka.cc
	
// PQL Operator
var operator =	'$operator';

// PQL Parameter		
var parameter = '$parameter';
	
// Odysseus Script
var preprocessors = '#ADDQUERY #BUFFERPLACEMENT #CONFIG #DEFINE #DOQUERYSHARING #DOREWRITE #DROPALLQUERIES #DROPALLSINKS #DROPALLSOURCES #ENDIF #IF #IFDEF #IFNDEF #IFSRCDEF #IFSRCNDEF #INPUT #INCLUDE #LOGIN #LOGOUT #LOOP #METADATA #ODYSSEUS_PARAM #PRINT #PARSER #QName #QUERY #RELOADFROMLOG #RUNQUERY #SCHEDULER #SLEEP #STARTQUERIES #STARTSCHEDULER #STOPSCHEDULER #TRANSCFG #UNDEF';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: new RegExp(this.getKeywords(preprocessors), 'gm'), css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(operators), 'gm'), css: 'color5' },// PQL operator
{ regex: new RegExp(this.getKeywords(parameters), 'gm'), css: 'color4' }// PQL operator parameter
];
};

SyntaxHighlighter.brushes.Processing.prototype = new SyntaxHighlighter.Highlighter();
SyntaxHighlighter.brushes.Processing.aliases = ['PQL', 'pql'];
EOF




