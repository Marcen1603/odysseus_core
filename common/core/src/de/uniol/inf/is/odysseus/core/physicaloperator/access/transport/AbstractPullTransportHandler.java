package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class AbstractPullTransportHandler extends
		AbstractTransportHandler {

	public AbstractPullTransportHandler() {
		super();
	}

	public AbstractPullTransportHandler(IProtocolHandler<?> protocolHandler, Map<String,String> options) {
		super(protocolHandler, options);
	}

}
