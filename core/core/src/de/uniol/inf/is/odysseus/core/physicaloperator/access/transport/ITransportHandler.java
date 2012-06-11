package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Map;

public interface ITransportHandler  {
	
	public void addListener(ITransportHandlerListener listener);
	public void removeListener(ITransportHandlerListener listener);
	
	public void open() throws UnknownHostException, IOException;
	public void close() throws IOException;
	
	public void send(byte[] message) throws IOException;
	
	public ITransportHandler createInstance(Map<String, String> options);
	
	public InputStream getInputStream();
	
	//TODO: Handle cases where ITransport pushes data! 
	
	String getName();
}
