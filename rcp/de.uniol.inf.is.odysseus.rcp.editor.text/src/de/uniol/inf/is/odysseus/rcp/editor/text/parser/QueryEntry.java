package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

public class QueryEntry {

	private String parserID;
	private String transCfg;
	private String queryText;
	
	public QueryEntry( String parserID, String transCfg, String queryText ) {
		this.parserID = parserID;
		this.transCfg = transCfg;
		this.queryText = queryText;
	}

	public String getParserID() {
		return parserID;
	}

	public String getTransCfg() {
		return transCfg;
	}

	public String getQueryText() {
		return queryText;
	}
	
	
}
