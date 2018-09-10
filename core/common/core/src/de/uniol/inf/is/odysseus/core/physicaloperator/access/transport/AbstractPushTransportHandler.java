package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.InputStream;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class AbstractPushTransportHandler extends
		AbstractTransportHandler {

	public AbstractPushTransportHandler() {
		super();
	}

	public AbstractPushTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Not a pulling transport handler");
	}

	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Not a pulling transport handler");
	}

}
