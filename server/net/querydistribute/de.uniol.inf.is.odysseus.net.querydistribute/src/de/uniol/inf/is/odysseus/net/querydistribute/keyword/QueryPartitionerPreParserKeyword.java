package de.uniol.inf.is.odysseus.net.querydistribute.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.querydistribute.parameter.QueryPartitionerParameter;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryPartitionerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class QueryPartitionerPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "NODE_PARTITION";
	public static final String VARIABLE_NAME = "peerQueryPartitionerName";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Partitioner name for query partitioning is missing");
		}
		
		String[] splitted = parameter.trim().split(" ");
		String partitionerName = splitted[0].trim();
		
		if( !QueryPartitionerRegistry.getInstance().contains(partitionerName)) {
			throw new OdysseusScriptException("Query Partitioner name '" + partitionerName + "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		
		String[] splitted = parameter.trim().split(" ");
		List<String> parameters = KeywordHelper.generateParameterList(splitted);
		
		Optional<QueryPartitionerParameter> optParameter = KeywordHelper.getQueryBuildSettingOfType( settings, QueryPartitionerParameter.class);
		if( optParameter.isPresent() ) {
			optParameter.get().add(splitted[0], parameters);
		} else {
			settings.add(new QueryPartitionerParameter(splitted[0], parameters));
		}
		
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return QueryPartitionerRegistry.getInstance().getNames();
	}
}
