package de.uniol.inf.is.odysseus.wrapper.inertiacube.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class InertiaCubeProtocolHandler extends AbstractByteBufferHandler<Tuple<?>> {
	/**
	 * Default constructor.
	 */
	public InertiaCubeProtocolHandler() {
		super();
	}
	
	/**
     * Constructor.
     * @param direction
     * Direction of the stream.
     * @param access
     * Access pattern.
     */
	public InertiaCubeProtocolHandler(ITransportDirection direction,
            IAccessPattern access) {
		super (direction, access);
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
	public IProtocolHandler<Tuple<?>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options, IDataHandler<Tuple<?>> dataHandler,
			ITransferHandler<Tuple<?>> transfer) {
		InertiaCubeProtocolHandler instance = new InertiaCubeProtocolHandler(direction, access);
		instance.setOptionsMap(options);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		return instance;
	}

	@Override
	public String getName() {
		return "InertiaCube";
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		// Transport handler will do the job
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// Transport handler will do the job
	}

    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }

	@Override
	public void process(ByteBuffer message) {
		super.getTransfer().transfer(getDataHandler().readData(message));
	}

	@Override
	public void process(String[] message) {
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof InertiaCubeProtocolHandler)) {
			return false;
		} else {
			// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
			return true;
		}
	}

}
