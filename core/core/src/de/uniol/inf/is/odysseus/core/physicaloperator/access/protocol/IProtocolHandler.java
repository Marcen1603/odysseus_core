package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public interface IProtocolHandler <T> {
	
	public void open() throws UnknownHostException, IOException;
	public void close() throws IOException;
	public boolean hasNext() throws IOException;
	public T getNext() throws IOException;
	
	public IProtocolHandler<T> createInstance(Map<String, String> options, ITransportHandler transportHandler, IDataHandler<T> dataHandler, ITransferHandler<T> transfer);
	
	String getName();
	
}
