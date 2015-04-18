package de.uniol.inf.is.odysseus.peer.transmission;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public interface ITransmissionSender {

	public void sendData( byte[] data ) throws DataTransmissionException;
	public void sendPunctuation(IPunctuation punctuation) throws DataTransmissionException;
	public void sendDone() throws DataTransmissionException;
	
	public void addListener( ITransmissionSenderListener listener );
	public void removeListener( ITransmissionSenderListener listener );

	public void open();
	public void close();
	
	public void flushBuffers();
	
	/**
	 * Resets the list of peers the sender sends to. Necessary for recovery.
	 */
	public void resetPeerList();
}