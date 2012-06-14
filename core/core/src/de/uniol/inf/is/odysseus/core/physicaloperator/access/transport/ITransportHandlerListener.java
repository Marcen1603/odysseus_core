package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;


public interface ITransportHandlerListener<T> {
	
	public void onConnect(ITransportHandler caller);
	public void onDisonnect(ITransportHandler caller);
	

	public void process(T messsage);

}
