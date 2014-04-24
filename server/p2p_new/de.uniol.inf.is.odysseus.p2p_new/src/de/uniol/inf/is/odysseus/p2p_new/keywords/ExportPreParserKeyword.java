package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ExportPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "EXPORT";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		final String transCfgName = (String) variables.get("TRANSCFG");
		
		if( Strings.isNullOrEmpty(transCfgName)) {
			throw new OdysseusScriptException("Cannot export: Name of transformation configuration not set!");
		}
		
		if( !ServerExecutorService.isBound()) {
			throw new OdysseusScriptException("Cannot export: Server Executor is not bound!");
		}
		
		if( ServerExecutorService.getServerExecutor().getQueryBuildConfiguration(transCfgName) == null) {
			throw new OdysseusScriptException("Cannot export: TransformationConfiguration '" + transCfgName + "' not found");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		String sourceToPublish = parameter.trim();

		P2PNetworkManager.waitForStart();
		try {
			P2PDictionary.getInstance().exportSource(sourceToPublish);
		} catch (PeerException e) {
			throw new OdysseusScriptException("Could not export " + sourceToPublish, e);
		}
		
		return null;
	}

}
