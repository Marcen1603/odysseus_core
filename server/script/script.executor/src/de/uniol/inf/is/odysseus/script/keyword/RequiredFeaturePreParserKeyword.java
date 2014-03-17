package de.uniol.inf.is.odysseus.script.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class RequiredFeaturePreParserKeyword extends AbstractPreParserKeyword {

	public static final String REQUIRED = "REQUIRED";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		String params[] = getSimpleParameters(parameter);
		String id = params[0];
		if(!FeatureUpdateUtility.isFeatureInstalled(id, caller)){
			if(params[1].trim().equalsIgnoreCase("INSTALL")){
				FeatureUpdateUtility.installFeature(id, caller);
			}else{
				throw new OdysseusScriptException("This script requires feature "+id+" which is not installed!");
			}
		}
		return null;
	}

}
