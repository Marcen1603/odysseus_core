#!/bin/bash

# Find all logical operator
operator=$(find ../../ -name \*.java -type f |xargs grep "@LogicalOperator" | sed -rn 's/.*name\s*\=\s*\"([a-zA-Z_]*)\".*/\U\1/p' | sort | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D' )

# Find all logical operator parameter
parameter=$(find ../../ -name \*.java -type f |xargs grep "@Parameter" | sed -rn 's/.*name\s*\=\s*\"([a-zA-Z_]*)\".*/\U\1/p'  | sort -fu | sed -r ':a ;$! N; s/\n(.*)/ \1/; ta ; P ; D' )

cat > shBrushPql.js <<- EOF
;(function()
{
// CommonJS
SyntaxHighlighter = SyntaxHighlighter || (typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null);

function Brush()
{	
// PQL Operator
var operators =	'$operator';

// PQL Parameter		
var parameters = '$parameter';
	
this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: /\s*#.*/gm, css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(operators), 'gmi'), css: 'keyword' },// PQL operator
{ regex: new RegExp(this.getKeywords(parameters), 'gmi'), css: 'color2' }// PQL operator parameter
];
this.forHtmlScript(SyntaxHighlighter.regexLib.scriptScriptTags);
};

Brush.prototype	= new SyntaxHighlighter.Highlighter();
Brush.aliases	= ['pql', 'PQL'];
SyntaxHighlighter.brushes.Pql = Brush;
// CommonJS
typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
EOF




