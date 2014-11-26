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
import de.uniol.inf.is.odysseus.peer.recovery.parameter.RecoveryStrategyManagerParameter;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyManagerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class RecoveryStrategyManagerPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "RECOVERY_STRATEGY_MANAGER";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		
		if( Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Recovery strategy manager name is missing");
		}
		
		String[] splitted = parameter.trim().split(" ");
		String strategyManagerName = splitted[0].trim();
		
		if( !RecoveryStrategyManagerRegistry.getInstance().contains(strategyManagerName)) {
			throw new OdysseusScriptException("Recovery strategy manager name '" + strategyManagerName + "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		
		String[] splitted = parameter.trim().split(" ");
		List<String> parameters = KeywordHelper.generateParameterList(splitted);
		
		Optional<RecoveryStrategyManagerParameter> optParameter = KeywordHelper.getQueryBuildSettingOfType( settings, RecoveryStrategyManagerParameter.class);
		if( optParameter.isPresent() ) {
			optParameter.get().add(splitted[0], parameters);
		} else {
			settings.add(new RecoveryStrategyManagerParameter(splitted[0], parameters));
		}
		
		return null;

	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return RecoveryStrategyManagerRegistry.getInstance().getNames();
	}

}
