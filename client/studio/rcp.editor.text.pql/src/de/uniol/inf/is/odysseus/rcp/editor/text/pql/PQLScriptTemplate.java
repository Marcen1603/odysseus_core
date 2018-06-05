package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class PQLScriptTemplate implements IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "PQL Basic";
	}

	@Override
	public String getDescription() {
		return "Empty query with prepared PQL-Environment";
	}

	@Override
	public String getText() {
		return 	"#PARSER PQL\n" +
				"#RUNQUERY\n" +
				"///Your first pql-query here";		
	}

}
