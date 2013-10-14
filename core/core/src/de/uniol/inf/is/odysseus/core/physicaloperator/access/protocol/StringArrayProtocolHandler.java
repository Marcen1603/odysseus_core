package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class StringArrayProtocolHandler<T> extends AbstractProtocolHandler<T> {

	public static final String NAME = "StringArray";

	public StringArrayProtocolHandler() {
	}

	public StringArrayProtocolHandler(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		setDataHandler(dataHandler);
		if (transfer != null) {
			if (transfer instanceof IProvidesStringArray) {
				setTransfer(transfer);
			} else {
				throw new IllegalArgumentException("Illegal transport handler "
						+ transfer);
			}
		}
		setOptionsMap(options);
	}

	@Override
	public void setOptionsMap(Map<String, String> options) {
		super.setOptionsMap(options);
		// Read any further options
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		return new StringArrayProtocolHandler<T>(direction, access, options,
				dataHandler, transfer);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void onConnect(ITransportHandler caller) {
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
	}

	@Override
	public void process(ByteBuffer message) {
	}

	@Override
	public void process(String[] message) {
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	@Override
	public boolean hasNext() throws IOException {
		return ((IProvidesStringArray) getTransportHandler()).hasNext();
	}

	@Override
	public T getNext() throws IOException {
		String[] next = ((IProvidesStringArray) getTransportHandler())
				.getNext();
		if (next != null) {
			return getDataHandler().readData(next);
		} else {
			return null;
		}
	}

}
