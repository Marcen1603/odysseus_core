package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartitionerParameter;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartitionerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class PeerQueryPartitionerPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "PEER_PARTITION";
	public static final String VARIABLE_NAME = "peerQueryPartitionerName";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Partitioner name for query partitioning is missing");
		}
		
		String partitionerName = parameter.trim();
		if( QueryPartitionerRegistry.getInstance().contains(partitionerName)) {
			throw new OdysseusScriptException("Query Partitioner name '" + partitionerName + "' is not registered!");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		settings.add(new QueryPartitionerParameter(parameter.trim()));
		
		return null;
	}

}
