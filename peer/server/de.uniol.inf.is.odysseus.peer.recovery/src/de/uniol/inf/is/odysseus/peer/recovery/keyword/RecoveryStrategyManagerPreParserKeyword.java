package de.uniol.inf.is.odysseus.peer.recovery.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.advertisement.RecoveryStrategyManagerAdvertisementSender;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryCommunicatorRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyManagerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class RecoveryStrategyManagerPreParserKeyword extends AbstractPreParserKeyword {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyManagerPreParserKeyword.class);

	public static final String KEYWORD = "RECOVERY_STRATEGY_MANAGER";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
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
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		IRecoveryStrategyManager strategyManager = RecoveryStrategyManagerRegistry.getInstance().get(parameter);		
		LOG.debug("Selected recovery strategy manager "+strategyManager.getName());
		for (IRecoveryCommunicator communicator : RecoveryCommunicatorRegistry.getRecoveryCommunicators()) {
			communicator.setRecoveryStrategyManager(strategyManager);
		}
		RecoveryStrategyManagerAdvertisementSender.publish(parameter);
		return null;

	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return RecoveryStrategyManagerRegistry.getInstance().getNames();
	}

}
