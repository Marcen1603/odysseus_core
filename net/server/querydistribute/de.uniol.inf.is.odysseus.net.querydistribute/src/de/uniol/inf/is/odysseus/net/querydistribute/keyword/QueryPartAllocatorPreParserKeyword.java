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
import de.uniol.inf.is.odysseus.net.querydistribute.parameter.QueryPartAllocatorParameter;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryPartAllocatorRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class QueryPartAllocatorPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "NODE_ALLOCATION";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Query part allocator name is missing");
		}
		
		String[] splitted = parameter.trim().split(" ");
		String allocatorName = splitted[0].trim();
		
		if( !QueryPartAllocatorRegistry.getInstance().contains(allocatorName)) {
			throw new OdysseusScriptException("Query Part Allocator name '" + allocatorName + "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		
		String[] splitted = parameter.trim().split(" ");
		List<String> parameters = KeywordHelper.generateParameterList(splitted);
		
		Optional<QueryPartAllocatorParameter> optParameter = KeywordHelper.getQueryBuildSettingOfType( settings, QueryPartAllocatorParameter.class);
		if( optParameter.isPresent() ) {
			optParameter.get().add(splitted[0], parameters);
		} else {
			settings.add(new QueryPartAllocatorParameter(splitted[0], parameters));
		}
		
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return QueryPartAllocatorRegistry.getInstance().getNames();
	}

}
