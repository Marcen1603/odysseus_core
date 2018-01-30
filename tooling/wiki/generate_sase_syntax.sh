#!/bin/bash

# Sase keywords
keywords=$(echo "CREATE STREAM VIEW PATTERN WHERE WITHIN ENDS AT RETURN SEQ ALT AND AS LEN WEEK WEEKS DAY DAYS HOUR HOURS MINUTE MINUTES SECOND SECONDS MILLISECOND MILLISECONDS SKIP_TILL_NEXT_MATCH SKIP_TILL_ANY_MATCH STRICT_CONTIGUITY AVG MIN MAX SUM COUNT TRUE FALS" | tr ' ' '\n' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')

# Find all datatypes
datatypes=$(find ../../ -name \*.java -type f |xargs grep -E "public .*static .*SDF[a-zA-Z_]*Datatype" | sed -rn 's/.*new SDF[a-zA-Z_]*Datatype\(\"([a-zA-Z_]*)\"\);.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')

cat > shBrushSase.js <<- EOF
;(function()
{
// CommonJS
SyntaxHighlighter = SyntaxHighlighter || (typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null);

function Brush()
{

// SASE keywords
var keywords =	'$keywords';

// SASE datatypes
var datatypes = '$datatypes';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: /\s*#.*/gm, css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(keywords), 'gmi'), css: 'keyword' },// SASE keywords
{ regex: new RegExp(this.getKeywords(datatypes), 'gmi'), css: 'color2' }// SASE datatypes
];
this.forHtmlScript(SyntaxHighlighter.regexLib.scriptScriptTags);
};

Brush.prototype	= new SyntaxHighlighter.Highlighter();
Brush.aliases	= ['sase', 'SASE'];
SyntaxHighlighter.brushes.Sase = Brush;
// CommonJS
typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
EOF


