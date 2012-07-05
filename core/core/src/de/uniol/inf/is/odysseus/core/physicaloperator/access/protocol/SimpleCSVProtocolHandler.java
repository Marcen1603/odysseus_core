package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class SimpleCSVProtocolHandler<T> extends LineProtocolHandler<T> {

	private String delimiter;

	@Override
	public T getNext() throws IOException {
		delay();
		return getDataHandler().readData(reader.readLine().split(delimiter));
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		SimpleCSVProtocolHandler<T> instance = new SimpleCSVProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.delimiter = options.get("delimiter");
		return instance;
	}

	@Override
	public String getName() {
		return "SimpleCSV";
	}
	
}
