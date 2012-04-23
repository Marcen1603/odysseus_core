package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * This operator is only needed as the top operator, if the output of
 * a query should be renamed (not needed in cases of views and not
 * between two operators)
 * 
 * @author Marco Grawunder
 *	
 * Which type to read and to write
 * @param <R>
 */

public class RenamePO<R> extends AbstractPipe<R, R> {

	public RenamePO() {
	}

	public RenamePO(RenamePO<R> pipe) {
		super(pipe);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new RenamePO<R>(this);
	}

}
