package de.uniol.inf.is.odysseus.peer.recovery.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.util.KeywordHelper;
import de.uniol.inf.is.odysseus.peer.recovery.parameter.RecoveryStrategyParameter;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class RecoveryStrategyPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "RECOVERY_STRATEGY";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Recovery strategy name is missing");
		}
		
		String[] splitted = parameter.trim().split(" ");
		String strategyName = splitted[0].trim();
		
		if( !RecoveryStrategyRegistry.getInstance().contains(strategyName)) {
			throw new OdysseusScriptException("Recovery strategy name '" + strategyName + "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		
		String[] splitted = parameter.trim().split(" ");
		List<String> parameters = KeywordHelper.generateParameterList(splitted);
		
		Optional<RecoveryStrategyParameter> optParameter = KeywordHelper.getQueryBuildSettingOfType( settings, RecoveryStrategyParameter.class);
		if( optParameter.isPresent() ) {
			optParameter.get().add(splitted[0], parameters);
		} else {
			settings.add(new RecoveryStrategyParameter(splitted[0], parameters));
		}
		
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return RecoveryStrategyRegistry.getInstance().getNames();
	}

}
