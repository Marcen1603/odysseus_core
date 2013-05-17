package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourcePublisher;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class PublishPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "PUBLISH";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String transCfgName = (String) variables.get("TRANSCFG");
		
		if( Strings.isNullOrEmpty(transCfgName)) {
			throw new OdysseusScriptException("Cannot publish: Name of transformation configuration not set!");
		}
		
		if( !DataDictionaryService.isBound() ) {
			throw new OdysseusScriptException("Cannot publish: DataDictionary is not bound!");
		}
		
		if( !ServerExecutorService.isBound()) {
			throw new OdysseusScriptException("Cannot publish: Server Executor is not bound!");
		}
		
		if( ServerExecutorService.get().getQueryBuildConfiguration(transCfgName) == null) {
			throw new OdysseusScriptException("Cannot publish: TransformationConfiguration '" + transCfgName + "' not found");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String sourceToPublish = parameter.trim();
		final String transCfgName = (String) variables.get("TRANSCFG");

		try {
			SourcePublisher.getInstance().publish(sourceToPublish, transCfgName, caller);
		} catch (PeerException e) {
			throw new OdysseusScriptException(e);
		}
		
		return null;
	}

}
