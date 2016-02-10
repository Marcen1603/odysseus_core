package de.uniol.inf.is.odysseus.rcp.editor.script;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class OdysseusScriptBlock {

	private final String keyword;
	private final String text;
	
	public OdysseusScriptBlock( String keyword, String text ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(keyword), "keyword must not be null or empty!");
		Preconditions.checkNotNull(text, "text must not be null!");

		this.keyword = keyword;
		this.text = text;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public String getText() {
		return text;
	}
	
}
