package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartModificatorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartModificatorRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class PeerQueryPartModificatorPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "PEER_MODIFICATION";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Query modificator name is missing");
		}
		
		String modificatorName = parameter.trim();
		if( QueryPartModificatorRegistry.getInstance().contains(modificatorName)) {
			throw new OdysseusScriptException("Query Part Modificator name '" + modificatorName + "' is not registered!");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		settings.add(new QueryPartModificatorParameter(parameter.trim()));
		
		return null;
	}

}
