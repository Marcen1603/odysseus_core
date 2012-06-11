package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.nio.ByteBuffer;

public interface ITransportHandlerListener {
	
	public void onConnect(ITransportHandler caller);
	public void onDisonnect(ITransportHandler caller);
	

	public void process(ByteBuffer messsage);

}
