package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import org.eclipse.jface.text.Document;

public class OdysseusScriptDocument extends Document {
	
	private static final String QDYSSEUS_SCRIPT = "qry";
	private String parserType = QDYSSEUS_SCRIPT;

	public OdysseusScriptDocument(String parserType){
		super();
		this.parserType = parserType.toUpperCase();
	}
	
	public boolean isOdysseusScript(){
		return parserType.equalsIgnoreCase(QDYSSEUS_SCRIPT);
	}
	
	public String getParserType() {
		return parserType;
	}

	public void setParserType(String parserType) {
		this.parserType = parserType;
	}
	
	

}
