package de.uniol.inf.is.odysseus.wrapper.iec62056.server;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class IEC62056TransportHandler extends AbstractPushTransportHandler {

	public static final Logger log = LoggerFactory.getLogger(IEC62056TransportHandler.class);

	public IEC62056TransportHandler() {
	}

	public IEC62056TransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options)
			throws UnknownHostException {
		throw new NotImplementedException("this transport handler has no implementation!");
	}

	@Override
	public String getName() {
		return "DLMS/COSEM";
	}

	@Override
	public void processInOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processInClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new NotImplementedException(
				"The method send(byte[]) of the IEC62056 transport handler is not implemented! "
						+ "Use the IEC62056 transport handler only with the None protocol handler so "
						+ "that the method send(Object) is called.");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		// TODO Auto-generated method stub
		return null;
	}

}
