package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

public interface IQueryTransmissionHandlerListener {
	public void localLockFailed(QueryTransmissionHandler transmission);
	public void tranmissionFailed(QueryTransmissionHandler transmission);
	public void transmissionSuccessful(QueryTransmissionHandler transmission);
}
