#!/bin/bash

# Find all keywords
parser='../../server/cql/parser.cql/src/de/uniol/inf/is/odysseus/parser/cql/parser/NewSQLParser.jjt'
keywords=$(cat $parser | tr '\n\r' '\n' | grep -E '^\| < K_[A-Z_]+ : \"' | sed -rn 's/.*\| < K_[A-Z_]+ : \"([a-zA-Z_]*)\" >.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')
datatypes=$(find ../../ -name \*.java -type f |xargs grep -E "public .*static .*SDF[a-zA-Z_]*Datatype" | sed -rn 's/.*new SDF[a-zA-Z_]*Datatype\(\"([a-zA-Z_]*)\"\);.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')

cat > shBrushCql.js <<- EOF
SyntaxHighlighter.brushes.Cql = function()
{
// Copyright 2014 Christian Kuka christian@kuka.cc
	
// CQL keywords
var keywords =	'$keywords';

// CQL datatypes
var datatypes = '$datatypes';

// Odysseus Script
var preprocessors = '#ADDQUERY #BUFFERPLACEMENT #CONFIG #DEFINE #DOQUERYSHARING #DOREWRITE #DROPALLQUERIES #DROPALLSINKS #DROPALLSOURCES #ENDIF #IF #IFDEF #IFNDEF #IFSRCDEF #IFSRCNDEF #INPUT #INCLUDE #LOGIN #LOGOUT #LOOP #METADATA #ODYSSEUS_PARAM #PRINT #PARSER #QName #QUERY #RELOADFROMLOG #RUNQUERY #SCHEDULER #SLEEP #STARTQUERIES #STARTSCHEDULER #STOPSCHEDULER #TRANSCFG #UNDEF';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: new RegExp(this.getKeywords(preprocessors), 'gm'), css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(keywords), 'gm'), css: 'color5' },// CQL keywords
{ regex: new RegExp(this.getKeywords(keywords), 'gm'), css: 'color2' }// CQL datatypes
];
};

SyntaxHighlighter.brushes.Processing.prototype = new SyntaxHighlighter.Highlighter();
SyntaxHighlighter.brushes.Processing.aliases = ['CQL', 'cql'];
EOF


