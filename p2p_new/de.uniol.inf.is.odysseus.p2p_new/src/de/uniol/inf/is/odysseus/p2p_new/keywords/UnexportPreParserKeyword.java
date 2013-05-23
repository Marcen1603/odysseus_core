package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionary;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class UnexportPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "UNEXPORT";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String sourceToPublish = parameter.trim();
		
		P2PDictionary.getInstance().removeViewExport(sourceToPublish);
		
		return null;
	}

}
