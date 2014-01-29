package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.DoForceLocalParameter;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

@Deprecated
public class DoForceLocalPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "PEER_DOFORCELOCAL";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		if ("FALSE".equals(parameter.toUpperCase())) {
			addSettings.add(DoForceLocalParameter.FALSE);
		} else {
			addSettings.add(DoForceLocalParameter.TRUE);
		}
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return Lists.newArrayList("true", "false");
	}
}
