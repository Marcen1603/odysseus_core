package de.uniol.inf.is.odysseus.recovery.duplicateelemination.transform;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.recovery.duplicateelemination.DuplicateEliminationRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.duplicateelemination.physicaloperator.SenderWithBackupPO;
import de.uniol.inf.is.odysseus.recovery.duplicateelemination.physicaloperator.SenderWithDuplicateEliminationPO;
import de.uniol.inf.is.odysseus.transform.rules.TSenderAOGenericRule;

/**
 * Transformation rule for {@link AbstractSenderAO}s, if duplicate elimination
 * is used.
 * 
 * @author Michael Brand
 *
 */
public class TSenderAORule extends TSenderAOGenericRule {

	@Override
	protected SenderPO<?> getSenderPO(IProtocolHandler<?> protocolHandler, AbstractSenderAO senderAO) {
		final String OPTION_KEY = DuplicateEliminationRecoveryComponent.DUPLICATEELIMINATION_OPTION_KEY;
		if (senderAO.getOptionsMap().containsKey(OPTION_KEY)) {
			if (Boolean.valueOf(senderAO.getOptionsMap().get(OPTION_KEY))) {
				return new SenderWithDuplicateEliminationPO<>(protocolHandler, senderAO.getSink().getResourceName());
			}
			return new SenderWithBackupPO<>(protocolHandler, senderAO.getSink().getResourceName());
		}
		return super.getSenderPO(protocolHandler, senderAO);
	}
}