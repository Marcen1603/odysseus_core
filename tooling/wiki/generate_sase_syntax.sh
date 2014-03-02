#!/bin/bash

# Sase keywords
keywords=$(echo "CREATE STREAM VIEW PATTERN WHERE WITHIN ENDS AT RETURN SEQ ALT AND AS LEN WEEK WEEKS DAY DAYS HOUR HOURS MINUTE MINUTES SECOND SECONDS MILLISECOND MILLISECONDS SKIP_TILL_NEXT_MATCH SKIP_TILL_ANY_MATCH STRICT_CONTIGUITY AVG MIN MAX SUM COUNT TRUE FALS" | tr ' ' '\n' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')

# Find all datatypes
datatypes=$(find ../../ -name \*.java -type f |xargs grep -E "public .*static .*SDF[a-zA-Z_]*Datatype" | sed -rn 's/.*new SDF[a-zA-Z_]*Datatype\(\"([a-zA-Z_]*)\"\);.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')

cat > shBrushSase.js <<- EOF
SyntaxHighlighter.brushes.Sase = function()
{
// Copyright 2014 Christian Kuka christian@kuka.cc
	
// SASE keywords
var keywords =	'$keywords';

// SASE datatypes
var datatypes = '$datatypes';

// Odysseus Script
var preprocessors = '#ADDQUERY #BUFFERPLACEMENT #CONFIG #DEFINE #DOQUERYSHARING #DOREWRITE #DROPALLQUERIES #DROPALLSINKS #DROPALLSOURCES #ENDIF #IF #IFDEF #IFNDEF #IFSRCDEF #IFSRCNDEF #INPUT #INCLUDE #LOGIN #LOGOUT #LOOP #METADATA #ODYSSEUS_PARAM #PRINT #PARSER #QName #QUERY #RELOADFROMLOG #RUNQUERY #SCHEDULER #SLEEP #STARTQUERIES #STARTSCHEDULER #STOPSCHEDULER #TRANSCFG #UNDEF';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: new RegExp(this.getKeywords(preprocessors), 'gm'), css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(keywords), 'gm'), css: 'color5' },// SASE keywords
{ regex: new RegExp(this.getKeywords(datatyps), 'gm'), css: 'color2' }// SASE datatypes
];
};

SyntaxHighlighter.brushes.Processing.prototype = new SyntaxHighlighter.Highlighter();
SyntaxHighlighter.brushes.Processing.aliases = ['Sase', 'sase'];
EOF


