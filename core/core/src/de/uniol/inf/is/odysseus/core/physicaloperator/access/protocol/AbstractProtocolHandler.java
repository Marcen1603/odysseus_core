package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

abstract public class AbstractProtocolHandler<T> implements IProtocolHandler<T> {

	private ITransportHandler transportHandler;
	private IDataHandler<T> dataHandler;
	private ITransferHandler<T> transfer;

	
	final protected ITransportHandler getTransportHandler() {
		return transportHandler;
	}
	
	final protected void setTransportHandler(ITransportHandler transportHandler) {
		this.transportHandler = transportHandler;
	}
	
	final protected IDataHandler<T> getDataHandler() {
		return dataHandler;
	}
	
	final protected void setDataHandler(IDataHandler<T> dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	final protected ITransferHandler<T> getTransfer() {
		return transfer;
	}
	
	final protected void setTransfer(ITransferHandler<T> transfer) {
		this.transfer = transfer;
	}
	
	@Override
	public boolean hasNext() throws IOException {
		return false;
	}

	@Override
	public T getNext() throws IOException {
		return null;
	}
	
}
