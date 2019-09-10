package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

/**
 * 
 * @author Marco Grawunder
 *
 */
public class OutputConnectorPO<R extends IStreamObject<?>> extends AbstractPipe<R, R> {
	
	private final int port;
	
	public OutputConnectorPO(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected synchronized void process_next(R object, int port) {
		transfer(object);
	}
	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
}
