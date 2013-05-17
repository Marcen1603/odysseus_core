package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourcePublisher;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class UnpublishPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "UNPUBLISH";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String sourceToPublish = parameter.trim();
		
		SourcePublisher.getInstance().unpublish(sourceToPublish);
		
		return null;
	}

}
