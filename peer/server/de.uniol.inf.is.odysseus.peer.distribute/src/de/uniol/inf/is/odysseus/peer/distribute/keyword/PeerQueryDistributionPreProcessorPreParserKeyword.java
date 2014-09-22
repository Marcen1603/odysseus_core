package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryDistributionPreProcessorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryDistributionPreProcessorRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class PeerQueryDistributionPreProcessorPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "PEER_PREPROCESSOR";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Query distribution preprocessor name is missing");
		}
		
		String[] splitted = parameter.trim().split(" ");
		String preProcessorName = splitted[0].trim();
		if( !QueryDistributionPreProcessorRegistry.getInstance().contains(preProcessorName)) {
			throw new OdysseusScriptException("Query distribution preprocessor name '" + preProcessorName + "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		
		String[] splitted = parameter.trim().split(" ");
		List<String> parameters = KeywordHelper.generateParameterList(splitted);
		
		Optional<QueryDistributionPreProcessorParameter> optParameter = KeywordHelper.getQueryBuildSettingOfType( settings, QueryDistributionPreProcessorParameter.class);
		if( optParameter.isPresent() ) {
			optParameter.get().add(splitted[0], parameters);
		} else {
			settings.add(new QueryDistributionPreProcessorParameter(splitted[0], parameters));
		}
		
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return QueryDistributionPreProcessorRegistry.getInstance().getNames();
	}

}
