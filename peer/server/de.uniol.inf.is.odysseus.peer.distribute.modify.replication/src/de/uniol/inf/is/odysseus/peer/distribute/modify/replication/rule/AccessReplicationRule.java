package de.uniol.inf.is.odysseus.peer.distribute.modify.replication.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.ReplicationParameterHelper;

/**
 * Access operators can not be replicated.
 * 
 * @author Michael Brand
 *
 */
public class AccessReplicationRule implements
		IReplicationRule<AbstractAccessAO> {

	@Override
	public boolean canOperatorBeReplicated(AbstractAccessAO operator,
			ReplicationParameterHelper helper) {

		return false;

	}

	@Override
	public Class<AbstractAccessAO> getOperatorClass() {

		return AbstractAccessAO.class;

	}

}