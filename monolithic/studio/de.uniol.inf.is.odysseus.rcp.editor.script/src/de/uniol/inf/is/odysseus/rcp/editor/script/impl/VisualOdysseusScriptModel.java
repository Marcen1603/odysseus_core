package de.uniol.inf.is.odysseus.rcp.editor.script.impl;

import java.util.List;

import com.google.common.base.Preconditions;

public class VisualOdysseusScriptModel {

	public VisualOdysseusScriptModel() {
		// nothing to do
	}
	
	public void parse( List<String> odysseusScriptTextLines ) {
		Preconditions.checkNotNull(odysseusScriptTextLines, "odysseusScriptTextLines must not be null!");
		
		for( String odysseusScriptTextLine : odysseusScriptTextLines ) {
			String line = odysseusScriptTextLine.trim();
			
			
		}
	}
}
