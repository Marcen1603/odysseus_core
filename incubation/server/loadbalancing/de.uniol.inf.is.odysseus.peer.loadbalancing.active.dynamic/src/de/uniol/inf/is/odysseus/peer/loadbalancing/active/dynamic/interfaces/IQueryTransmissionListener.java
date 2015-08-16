package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.transfer.QueryTransmission;


public interface IQueryTransmissionListener {
	public void localLockFailed(QueryTransmission transmission);
	public void tranmissionFailed(QueryTransmission transmission);
	public void transmissionSuccessful(QueryTransmission transmission);
}
