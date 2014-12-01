package de.uniol.inf.is.odysseus.p2p_new.data;

import net.jxta.peer.PeerID;

public interface ITransmissionReceiver {

	public void addListener( ITransmissionReceiverListener listener);
	public void removeListener( ITransmissionReceiverListener listener);
	
	public void sendOpen() throws DataTransmissionException;
	public void sendClose() throws DataTransmissionException;
	
	public void open();
	public void close();
	
	/**
	 * Change the peerId. E.g., if the sender changes due to recovery.
	 * @param peerId The id of the new peer
	 * @throws DataTransmissionException 
	 */
	public void setPeerId(PeerID peerId) throws DataTransmissionException;
	
	/**
	 * Stops current receiver, so that it can be set to new Peer ID.
	 */
	public void stopReceiving();
}
