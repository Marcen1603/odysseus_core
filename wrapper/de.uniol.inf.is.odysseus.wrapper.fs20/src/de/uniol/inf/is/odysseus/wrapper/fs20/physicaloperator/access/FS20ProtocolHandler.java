package de.uniol.inf.is.odysseus.wrapper.fs20.physicaloperator.access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Protocol handler for the FS20 and FHT communication protocol. The
 * implementation is based on http://fhz4linux.info/tiki-index.php
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class FS20ProtocolHandler<T> extends AbstractProtocolHandler<T> {

	protected BufferedReader reader;
	protected BufferedWriter writer;

	public FS20ProtocolHandler() {
		super();
	}

	public FS20ProtocolHandler(ITransportDirection direction,
			IAccessPattern access) {
		super(direction, access);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccess().equals(IAccessPattern.PULL))
					|| (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
				reader = new BufferedReader(new InputStreamReader(
						getTransportHandler().getInputStream()));
			}
		} else {
			writer = new BufferedWriter(new OutputStreamWriter(
					getTransportHandler().getOutputStream()));
		}
	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN)) {
			if (reader != null) {
				reader.close();
			}
		} else {
			writer.close();
		}
		getTransportHandler().close();
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		FS20ProtocolHandler<T> instance = new FS20ProtocolHandler<T>(direction,
				access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		throw new RuntimeException("Not implemented yet");
		// return instance;
	}

	@Override
	public String getName() {
		return "FS20";
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasNext() throws IOException {
		return reader.ready();
	}

	@Override
	public T getNext() throws IOException {
		throw new RuntimeException("Not implemented yet");
		// if (reader.ready()) {
		// return getDataHandler().readData(reader.readLine());
		// } else {
		// return null;
		// }
	}

	@Override
	public void write(T object) throws IOException {
		writer.write(object.toString());
	}

	@Override
	public void process(ByteBuffer message) {
		// getTransfer().transfer(getDataHandler().readData(message));
		throw new RuntimeException("Not implemented yet");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
