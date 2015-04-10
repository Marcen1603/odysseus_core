package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

@Deprecated
public class ExportPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "EXPORT";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		if( !ServerExecutorService.isBound()) {
			throw new OdysseusScriptException("Cannot export: Server Executor is not bound!");
		}
	}
	
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		String sourceToPublish = parameter.trim();

		P2PNetworkManagerService.waitForStart();
		try {
			if( !P2PDictionary.getInstance().isExported(sourceToPublish) ) {
				P2PDictionary.getInstance().exportSource(sourceToPublish);
			}
		} catch (PeerException e) {
			throw new OdysseusScriptException("Could not export " + sourceToPublish, e);
		}
		
		return null;
	}

}
