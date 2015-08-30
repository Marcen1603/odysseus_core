package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.transmissionhandler.odyload;



public interface IQueryTransmissionListener {
	public void localLockFailed(QueryTransmission transmission);
	public void tranmissionFailed(QueryTransmission transmission);
	public void transmissionSuccessful(QueryTransmission transmission);
}
