package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class TextProtocolHandler<T> extends AbstractProtocolHandler<T>{

	private String charset;
	private String objectDelimiter;
	private Scanner scanner;
	private boolean keepDelimiter;
	
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		this.scanner = new Scanner(getTransportHandler().getInputStream(), charset);
		scanner.useDelimiter(objectDelimiter);		
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
		scanner.close();
	}

	@Override
	public boolean hasNext() throws IOException {
		return scanner.hasNext();
	}

	@Override
	public T getNext() throws IOException {
		if (keepDelimiter) {
			return getDataHandler().readData(scanner.next()+objectDelimiter);
		} else {
			return getDataHandler().readData(scanner.next());
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		TextProtocolHandler<T> instance  = new TextProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);

		instance.charset = options.get("charset");
		instance.objectDelimiter = options.get("delimiter");
		instance.keepDelimiter = Boolean.parseBoolean(options.get("keepdelimiter"));
		return instance;
	}

	@Override
	public String getName() {
		return "Text";
	}

}
