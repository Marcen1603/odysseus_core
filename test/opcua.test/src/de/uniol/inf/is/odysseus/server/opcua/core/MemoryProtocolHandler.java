package de.uniol.inf.is.odysseus.server.opcua.core;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.NoProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class MemoryProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends NoProtocolHandler<T>
		implements IProtocolHandler<T> {

	private static final Logger log = LoggerFactory.getLogger(MemoryProtocolHandler.class);

	private final Queue<IStreamObject<?>> queue;

	public MemoryProtocolHandler() {
		queue = new LinkedList<IStreamObject<?>>();
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return ITransportExchangePattern.InOut;
	}

	@Override
	public ITransportDirection getDirection() {
		return ITransportDirection.INOUT;
	}

	@Override
	public void process(T message) {
		processInternal(message);
	}

	private void processInternal(IStreamObject<?> m) {
		log.info("process => {}", m);
		queue.offer(m);
	}

	public Queue<IStreamObject<?>> getQueue() {
		return queue;
	}
}