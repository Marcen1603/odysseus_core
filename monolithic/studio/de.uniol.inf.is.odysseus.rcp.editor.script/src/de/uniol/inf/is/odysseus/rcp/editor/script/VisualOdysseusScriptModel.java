package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;

public class VisualOdysseusScriptModel {

	public VisualOdysseusScriptModel() {
		// nothing to do
	}
	
	public void parse( List<String> odysseusScriptTextLines ) throws OdysseusScriptException {
		Preconditions.checkNotNull(odysseusScriptTextLines, "odysseusScriptTextLines must not be null!");
		
		List<PreParserStatement> parserStatements = ServicesBinder.getOdysseusScriptParser().parseScript(odysseusScriptTextLines.toArray(new String[0]),OdysseusRCPPlugIn.getActiveSession(), Context.empty(), ServicesBinder.getServerExecutor());
		
	}
}
