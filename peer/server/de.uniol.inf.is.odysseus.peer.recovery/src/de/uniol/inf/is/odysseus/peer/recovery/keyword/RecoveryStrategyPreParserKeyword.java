package de.uniol.inf.is.odysseus.peer.recovery.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyManagerRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class RecoveryStrategyPreParserKeyword extends AbstractPreParserKeyword {

	
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyPreParserKeyword.class);

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
		IRecoveryStrategy strategy = RecoveryStrategyRegistry.getInstance().get(parameter);
		LOG.debug("Selected recovery strategy "+strategy.getName());
		for (IRecoveryStrategyManager strategyManager : RecoveryStrategyManagerRegistry.getInstance().getInterfaceContributions()) {
			strategyManager.setRecoveryStrategy(strategy);
		}
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return RecoveryStrategyRegistry.getInstance().getNames();
	}

}
