package de.uniol.inf.is.odysseus.peer.distribute.modify.replication.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.ReplicationParameterHelper;

/**
 * Stream operators can not be replicated.
 * 
 * @author Michael Brand
 *
 */
public class StreamReplicationRule implements IReplicationRule<StreamAO> {

	@Override
	public boolean canOperatorBeReplicated(StreamAO operator,
			ReplicationParameterHelper helper) {

		return false;

	}

	@Override
	public Class<StreamAO> getOperatorClass() {

		return StreamAO.class;

	}

}