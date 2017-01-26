package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
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

	public AbstractPullTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	@Override
	public void processInStart() {

	}
}
