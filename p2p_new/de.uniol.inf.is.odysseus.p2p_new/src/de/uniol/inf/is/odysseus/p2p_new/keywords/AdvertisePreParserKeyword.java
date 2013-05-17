package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourcePublisher;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class AdvertisePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "ADVERTISE";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		if( !DataDictionaryService.isBound() ) {
			throw new OdysseusScriptException("DataDictionary is not bound!");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String sourceToAdvertise = parameter.trim();
		
		try {
			SourcePublisher.getInstance().advertise(sourceToAdvertise, caller);
		} catch (PeerException e) {
			throw new OdysseusScriptException(e);
		}
		
		return null;
	}

}
