;(function()
{
// CommonJS
typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null;

function Brush()
{

// SASE keywords
var keywords =	'ALT AND AS AT AVG COUNT CREATE DAY DAYS ENDS FALS HOUR HOURS LEN MAX MILLISECOND MILLISECONDS MIN MINUTE MINUTES PATTERN RETURN SECOND SECONDS SEQ SKIP_TILL_ANY_MATCH SKIP_TILL_NEXT_MATCH STREAM STRICT_CONTIGUITY SUM TRUE VIEW WEEK WEEKS WHERE WITHIN';

// SASE datatypes
var datatypes = 'ASSOCIATIONRULE BITVECTOR BOOLEAN BYTE CHAR CLASSIFIER COMPLEXNUMBER DATE DOCUMENT DOUBLE FLOAT FREQUENTITEMSET GM_POINT HEXSTRING IMAGE IMAGEJCV INTEGER LONG OBJECT PROBABILISTICDOUBLE PROBABILISTICRESULT PROBABILISTICTUPLE RECOMMENDER SHORT SPATIALGEOMETRY STRING TIMESTAMP VECTORPROBABILISTICDOUBLE';

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
