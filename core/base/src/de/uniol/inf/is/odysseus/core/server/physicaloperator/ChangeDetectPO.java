package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

/**
 * This operator can reduce traffic. It lets an event pass if its different than
 * the last event. A heartbeat generation strategie can be used.
 * 
 * @author Marco Grawunder
 * 
 * @param <R>
 *            The type of the objects to filter
 */
public class ChangeDetectPO<R> extends AbstractPipe<R, R> {

	R lastElement;
	private IHeartbeatGenerationStrategy<R> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<R>();
	private boolean deliverFirstElement = false;
	
	public ChangeDetectPO() {
	}

	public ChangeDetectPO(AbstractPipe<R, R> pipe) {
		super(pipe);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected synchronized void process_next(R object, int port) {
		if (lastElement == null) {
			lastElement = object;
			if (deliverFirstElement){
				transfer(object);
			}
		} else {
			if (object != null && areDifferent(object, lastElement)) {
				lastElement = object;
				transfer(object);
			}else{
				heartbeatGenerationStrategy.generateHeartbeat(object, this);
			}
		}
		
	}

	protected boolean areDifferent(R object, R lastElement) {
		return !object.equals(lastElement);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.lastElement = null;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new ChangeDetectPO<R>(this);
	}
	
	public IHeartbeatGenerationStrategy<R> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(
			IHeartbeatGenerationStrategy<R> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}
	
	public void setDeliverFirstElement(boolean deliverFirstElement) {
		this.deliverFirstElement = deliverFirstElement;
	}

}
