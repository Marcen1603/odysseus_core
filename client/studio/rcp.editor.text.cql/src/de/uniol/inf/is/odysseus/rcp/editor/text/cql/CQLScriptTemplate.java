package de.uniol.inf.is.odysseus.rcp.editor.text.cql;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class CQLScriptTemplate implements IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "CQL Basic";
	}

	@Override
	public String getDescription() {
		return "Empty query with prepared CQL-Environment";
	}

	@Override
	public String getText() {
		return 	"#PARSER CQL\n" +
				"#RUNQUERY\n" +
				"///Your first cql-query here";		
	}

}
