package de.uniol.inf.is.odysseus.p2p_new.data;

public interface ITransmissionSenderListener {

	public void onReceiveOpen( ITransmissionSender sender );
	public void onReceiveClose( ITransmissionSender sender );
	
}
