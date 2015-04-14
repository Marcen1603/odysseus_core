package de.uniol.inf.is.odysseus.peer.transmission;

public interface ITransmissionSenderListener {

	public void onReceiveOpen( ITransmissionSender sender );
	public void onReceiveClose( ITransmissionSender sender );
	
}
