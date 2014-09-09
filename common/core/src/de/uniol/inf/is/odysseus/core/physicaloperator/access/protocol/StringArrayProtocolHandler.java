package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
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
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler, options);
	}

	@Override
	public void setTransfer(ITransferHandler<T> transfer) {
		if (transfer != null) {
			if (!(transfer instanceof IIteratable)) {
				throw new IllegalArgumentException("Illegal transport handler "
						+ transfer);
			}
		}
		super.setTransfer(transfer);
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
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		return new StringArrayProtocolHandler<T>(direction, access, options,
				dataHandler);
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
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasNext() throws IOException {
		return ((IIteratable<String[]>) getTransportHandler()).hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getNext() throws IOException {
		String[] next = ((IIteratable<String[]>) getTransportHandler())
				.getNext();
		if (next != null) {
			return getDataHandler().readData(next);
		} else {
			return null;
		}
	}

}
