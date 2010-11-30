package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.util.Map;


public class PreParserStatement {

	private IPreParserKeyword keyword;
	private String keywordText;
	private String parameter;
	
	public PreParserStatement( String keywordText, IPreParserKeyword keyword, String parameter ) {
		this.keyword = keyword;
		this.keywordText = keywordText;
		this.parameter = parameter;
	}
	
	public void validate( Map<String, String> variables ) throws QueryTextParseException {
		keyword.validate(variables, parameter);
	}
	
	public Object execute( Map<String, String> variables ) throws QueryTextParseException {
		return keyword.execute(variables, parameter);
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public String getKeywordText() {
		return keywordText;
	}
	
	public IPreParserKeyword getKeyword(){
		return keyword;
	}
}
