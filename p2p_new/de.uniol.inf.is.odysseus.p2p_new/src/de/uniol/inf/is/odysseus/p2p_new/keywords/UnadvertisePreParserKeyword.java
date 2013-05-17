package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourcePublisher;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class UnadvertisePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "UNADVERTISE";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String sourceToAdvertise = parameter.trim();
		
		SourcePublisher.getInstance().unadvertise(sourceToAdvertise);
		
		return null;
	}

}
