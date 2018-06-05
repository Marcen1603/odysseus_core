#!/bin/bash

# Find all keywords
parser='../../server/cql/parser.cql/src/de/uniol/inf/is/odysseus/parser/cql/parser/NewSQLParser.jjt'
keywords=$(cat $parser | tr '\n\r' '\n' | grep -E '^\| < K_[A-Z_]+ : \"' | sed -rn 's/.*\| < K_[A-Z_]+ : \"([a-zA-Z_]*)\" >.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')
# Find all datatypes
datatypes=$(find ../../ -name \*.java -type f |xargs grep -E "public .*static .*SDF[a-zA-Z_]*Datatype" | sed -rn 's/.*new SDF[a-zA-Z_]*Datatype\(\"([a-zA-Z_]*)\"\);.*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D')

cat > shBrushCql.js <<- EOF
;(function()
{
// CommonJS
SyntaxHighlighter = SyntaxHighlighter || (typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null);

function Brush()
{

// CQL keywords
var keywords =	'$keywords';

// CQL datatypes
var datatypes = '$datatypes';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: /\s*#.*/gm, css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(keywords), 'gmi'), css: 'keyword' },// CQL keywords
{ regex: new RegExp(this.getKeywords(datatypes), 'gmi'), css: 'color2' }// CQL datatypes
];
this.forHtmlScript(SyntaxHighlighter.regexLib.scriptScriptTags);
};

Brush.prototype	= new SyntaxHighlighter.Highlighter();
Brush.aliases	= ['cql', 'CQL'];
SyntaxHighlighter.brushes.Cql = Brush;
// CommonJS
typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
EOF


