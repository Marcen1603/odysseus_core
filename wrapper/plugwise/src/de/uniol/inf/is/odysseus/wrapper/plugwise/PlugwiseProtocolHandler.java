package de.uniol.inf.is.odysseus.wrapper.plugwise;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class PlugwiseProtocolHandler<T> extends AbstractProtocolHandler<T> {

	public static final String NAME = "Plugwise";
	
	public PlugwiseProtocolHandler() {
	}
	
	public PlugwiseProtocolHandler(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		super(direction, access);
		init(options);
		setDataHandler(dataHandler);
		setTransfer(transfer);
	}
	
	
	
	private void init(Map<String, String> options) {
		// TODO:		
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		// Init
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		return new PlugwiseProtocolHandler<>(direction, access, options, dataHandler,transfer);

	}

	@Override
	public String getName() {
		return NAME;
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
	public void process(ByteBuffer message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(String[] message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}

}
