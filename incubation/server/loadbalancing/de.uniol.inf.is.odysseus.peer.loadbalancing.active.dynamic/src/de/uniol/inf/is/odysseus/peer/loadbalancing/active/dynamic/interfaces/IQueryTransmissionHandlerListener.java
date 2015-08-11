package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.transfer.QueryTransmissionHandler;


public interface IQueryTransmissionHandlerListener {
	public void localLockFailed(QueryTransmissionHandler transmission);
	public void tranmissionFailed(QueryTransmissionHandler transmission);
	public void transmissionSuccessful(QueryTransmissionHandler transmission);
}
