package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.Objects;

public final class OdysseusScriptBlock {

	private final String keyword;
	private final String text;
	
	public OdysseusScriptBlock( String keyword, String text ) {
		Objects.requireNonNull(keyword, "keyword must not be null!");
		Objects.requireNonNull(text, "text must not be null!");

		this.keyword = keyword.toUpperCase();
		this.text = text;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{").append(keyword).append(":").append(text).append("}");
		
		return sb.toString();
	}
}
