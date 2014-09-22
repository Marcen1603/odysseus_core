package de.uniol.inf.is.odysseus.p2p_new.data;

public interface ITransmissionReceiver {

	public void addListener( ITransmissionReceiverListener listener);
	public void removeListener( ITransmissionReceiverListener listener);
	
	public void sendOpen() throws DataTransmissionException;
	public void sendClose() throws DataTransmissionException;
	
	public void open();
	public void close();
}
