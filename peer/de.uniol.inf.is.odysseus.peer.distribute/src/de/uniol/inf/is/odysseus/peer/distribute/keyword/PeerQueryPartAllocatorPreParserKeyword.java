package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartAllocatorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartModificatorRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class PeerQueryPartAllocatorPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "PEER_ALLOCATE";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Query part allocator name is missing");
		}
		
		String allocatorName = parameter.trim();
		if( QueryPartModificatorRegistry.getInstance().contains(allocatorName)) {
			throw new OdysseusScriptException("Query Part Allocator name '" + allocatorName + "' is not registered!");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		settings.add(new QueryPartAllocatorParameter(parameter.trim()));
		
		return null;
	}

}
