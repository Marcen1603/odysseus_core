package de.uniol.inf.is.odysseus.peer.transmission;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public interface ITransmissionReceiverListener {

	public void onReceiveData( ITransmissionReceiver receiver, byte[] data );
	public void onReceivePunctuation(ITransmissionReceiver receiver, IPunctuation punc);
	public void onReceiveDone(ITransmissionReceiver receiver);
	
}
