package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class UnexportPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "UNEXPORT";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		final String sourceToUnpublish = parameter.trim();
		
		P2PNetworkManager.waitForStart();
		P2PDictionary.getInstance().removeSourceExport(sourceToUnpublish);
		
		return null;
	}

}
