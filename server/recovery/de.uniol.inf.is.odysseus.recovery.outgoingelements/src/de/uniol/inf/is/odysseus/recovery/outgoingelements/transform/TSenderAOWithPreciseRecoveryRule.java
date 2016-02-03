package de.uniol.inf.is.odysseus.recovery.outgoingelements.transform;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterRecoveryConfiguration;
import de.uniol.inf.is.odysseus.recovery.outgoingelements.physicaloperator.SenderWithPreciseRecoveryExtensionPO;
import de.uniol.inf.is.odysseus.transform.rules.TSenderAOGenericRule;

/**
 * Transformation rule for {@link AbstractSenderAO}s, if precise recovery is
 * used.
 * 
 * @author Michael Brand
 *
 */
public class TSenderAOWithPreciseRecoveryRule extends TSenderAOGenericRule {

	@Override
	public boolean isExecutable(AbstractSenderAO operator, TransformationConfiguration config) {
		final String PARAMETER_NAME = ParameterRecoveryConfiguration.class.getName();
		final String EXECUTOR_NAME = "PreciseRecovery";
		return super.isExecutable(operator, config) && config.hasOption(PARAMETER_NAME)
				&& ((ParameterRecoveryConfiguration) config.getOption(PARAMETER_NAME)).getRecoveryExecutor()
						.equalsIgnoreCase(EXECUTOR_NAME);
	}

	@Override
	protected SenderPO<?> getSenderPO(IProtocolHandler<?> protocolHandler, AbstractSenderAO senderAO) {
		final String OPTION_KEY = "recovery";
		boolean recoveryMode = false;
		if (senderAO.getOptionsMap().containsKey(OPTION_KEY)) {
			recoveryMode = Boolean.valueOf(senderAO.getOptionsMap().get(OPTION_KEY)).booleanValue();
		}
		return new SenderWithPreciseRecoveryExtensionPO<>(protocolHandler, senderAO, recoveryMode);
	}

}