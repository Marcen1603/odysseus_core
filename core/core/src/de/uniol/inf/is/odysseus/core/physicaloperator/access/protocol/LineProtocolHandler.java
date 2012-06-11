package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class LineProtocolHandler<T> extends AbstractProtocolHandler<T> {

	protected BufferedReader reader;
	
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
	}
	
	@Override
	public void close() throws IOException {
		reader.close();
		getTransportHandler().close();
	}
	
	@Override
	public boolean hasNext() throws IOException {
		return getTransportHandler().getInputStream().available() > 0;
	}

	@Override
	public T getNext() throws IOException {
		return getDataHandler().readData(reader.readLine());
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		LineProtocolHandler<T> instance = new LineProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);

		instance.setTransportHandler(transportHandler);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		return instance;
	}
	
	// TODO: Push
	
	@Override
	public String getName() {
		return "Line";
	}

}
