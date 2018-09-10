package de.uniol.inf.is.odysseus.rcp.editor.text.completion;

public class Terminal {

	private final String name;
	private final boolean deprecated;
	
	public Terminal( String name, boolean deprecated ) {
		this.name = name;
		this.deprecated = deprecated;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDeprecated() {
		return deprecated;
	}
}
