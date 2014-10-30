;(function()
{
// CommonJS
typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null;

function Brush()
{

// CQL keywords
var keywords =	'ADD ADVANCE ALTER ALWAYS AND ANY AS ASSIGN AT ATTACH ATTRIBUTE AUTORECONNECT AVG BROKER BY CHANNEL CHECK COMMENT CONNECTION CONTEXT COUNT CREATE DATABASE DATAHANDLER DEFAULT DELETE DETACH DISTINCT DROP EACH EXISTS FILE FROM GRANT GROUP HAVING IDENTIFIED IF IN INFTY INSERT INTERSECT INTO IS JDBC JOIN KEY KILL LAZY_CONNECTION_CHECK LIKE LINEAR_STORE LIST LOGARITHMIC_STORE LOGIN_REQUIRED MATRIX MAX MAX_ADMISSION_COST_FACTOR METADATA METRIC MIN MINUS MULTI MV NATURAL NO_LAZY_CONNECTION_CHECK NOT NULL OF OFFSET ON OPTIONS OR PARTITION PASSWORD PENALTY PRIORITY PROB PROTOCOL QUEUE RANGE RECORD RELATION RELATIVE REMOVE REVOKE ROLE ROWS SCOPE SELECT SENSOR SERVICELEVEL SET SILAB SINGLE SINK SIZE SLA SLIDE SOCKET STORE STREAM SUM TABLE TIME TIMEOUT TIMESENSITIV TO TRANSPORT TRUNCATE TUPLE TYPE UNBOUNDED UNION USE USER VALID VIEW WHERE WITH WRAPPER';

// CQL datatypes
var datatypes = 'ASSOCIATIONRULE BITVECTOR BOOLEAN BYTE CHAR CLASSIFIER COMPLEXNUMBER DATE DOCUMENT DOUBLE FLOAT FREQUENTITEMSET IMAGE IMAGEJCV INTEGER LONG OBJECT PROBABILISTICDOUBLE PROBABILISTICRESULT PROBABILISTICTUPLE RECOMMENDER SHORT SPATIALGEOMETRY STRING TIMESTAMP VECTORPROBABILISTICDOUBLE';

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
