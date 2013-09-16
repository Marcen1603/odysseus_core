package de.uniol.inf.is.odysseus.rcp.editor.text.services;

import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

public class OdysseusScriptParserService {

	private static IOdysseusScriptParser boundParser;
	
	// called by OSGi-DS
	public void bind( IOdysseusScriptParser parser ) {
		boundParser = parser;
	}
	
	// called by OSGi-DS
	public void unbind( IOdysseusScriptParser parser ) {
		if( boundParser == parser ) {
			boundParser = null;
		}
	}
	
	public static boolean isBound() {
		return boundParser != null;
	}
	
	public static IOdysseusScriptParser get() {
		return boundParser;
	}
}
