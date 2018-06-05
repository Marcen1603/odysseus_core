package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class SyncWithSystemTimePO<R extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<R, R> {
	
	final private TimeUnit applicationTimeUnit;
	private double factor;	
	
	transient private long lastSystemtime = -1;
	transient  private long lastApplicationTime;
	transient private boolean terminate;
	
	public SyncWithSystemTimePO(TimeUnit applicationTimeUnit, double factor) {
		this.applicationTimeUnit = applicationTimeUnit;
		this.factor = factor;
	}

	public SyncWithSystemTimePO(SyncWithSystemTimePO<R> syncWithSystemTimePO) {
		this.applicationTimeUnit = syncWithSystemTimePO.applicationTimeUnit;
		this.factor = syncWithSystemTimePO.factor;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		lastSystemtime = -1;
		lastApplicationTime = -1;
		this.terminate = false;
	}

	@Override
	protected synchronized void process_close() {
		this.terminate = true;
		this.notifyAll();
	}

	@Override
	protected void process_next(R object, int port) {
		if (lastSystemtime > 0) {
			long currentApplicationTime = object.getMetadata().getStart()
					.getMainPoint();
			long applicationTimeDiff = convertApplicationTimeToMillis(currentApplicationTime-lastApplicationTime);
			long waitUntil = System.currentTimeMillis()+applicationTimeDiff;
			while (System.currentTimeMillis() < waitUntil
					&& !terminate) {
				try {
					synchronized (this) {
						this.wait(waitUntil-System.currentTimeMillis());
					}
				} catch (InterruptedException e) {
				}
			}
		}
		lastSystemtime = System.currentTimeMillis();
		lastApplicationTime = object.getMetadata().getStart().getMainPoint();
		transfer(object);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	private long convertApplicationTimeToMillis(long time) {
		
		if (factor > 0){
			return Math.round(time *factor);
		}else{
			return applicationTimeUnit.toMillis(time);
		}
	}
}
