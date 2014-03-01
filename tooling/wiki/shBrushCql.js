SyntaxHighlighter.brushes.Sql = function()
{
	// Copyright 2014 Christian Kuka christian@kuka.cc

	// CQL functions
	var functions =	'abs avg ' +
			'count ' +
			'days ' +
			'first ' +
			'last ' +
			'min max median ' +
			'substring sum ';

	// CQL keywords
	var keywords = 	'as ' +
			'by boolean byte ' +
			'create ' +
			'date double ' +
			'from false float ' +
			'group ' +
			'intersect integer interval ' +
			'long ' +
			'minute minutes milliseond milliseconds matrix ' +
			'select second seconds short ' +
			'time tuple timestamp ' +
			'union ' +
			'view vector ' +
			'where ' +
			'year years';

	// CQL datatypes
	var datatypes = 'boolean byte ' +
			'date double ' +
			'float ' +
			'integer interval ' +
			'long ' +
			'matrix ' +
			'short ' +
			'timestamp ' +
			'vector ';

	// CQL operators
	var operators =	'and like not or';

	// Odysseus Script
	var preprocessors = '#ADDQUERY #BUFFERPLACEMENT #CONFIG #DEFINE #DOQUERYSHARING #DOREWRITE #DROPALLQUERIES #DROPALLSINKS #DROPALLSOURCES #ENDIF #IF #IFDEF #IFNDEF #IFSRCDEF #IFSRCNDEF #INPUT #INCLUDE #LOGIN #LOGOUT #LOOP #METADATA #ODYSSEUS_PARAM #PRINT #PARSER #QName #QUERY #RELOADFROMLOG #RUNQUERY #SCHEDULER #SLEEP #STARTQUERIES #STARTSCHEDULER #STOPSCHEDULER #TRANSCFG #UNDEF';

	this.regexList = [
		{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
		{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
		{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
		{ regex: new RegExp(this.getKeywords(preprocessors), 'gm'), css: 'preprocessor' },// Odysseus Script
		{ regex: new RegExp(this.getKeywords(functions), 'gmi'), css: 'color2' },// functions
		{ regex: new RegExp(this.getKeywords(datatypes), 'gmi'), css: 'color1' },// datatypes
		{ regex: new RegExp(this.getKeywords(operators), 'gmi'), css: 'color1' },// operators
		{ regex: new RegExp(this.getKeywords(keywords), 'gmi'),	css: 'keyword' }// keywords
		];
};

SyntaxHighlighter.brushes.Sql.prototype	= new SyntaxHighlighter.Highlighter();
SyntaxHighlighter.brushes.Sql.aliases	= ['CQL', 'cql'];

