package de.uniol.inf.is.odysseus.script.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class RequiredAndInstallFeaturePreParserKeyword extends AbstractPreParserKeyword {

	public static final String REQUIRED_AND_INSTALL = "REQUIRED_INSTALL";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {

	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		String id = parameter;
		if (!FeatureUpdateUtility.isFeatureInstalled(id, caller)) {
			FeatureUpdateUtility.installFeature(id, caller);
		}
		return null;
	}

}
