package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class SyncWithSystemTimePO<R extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<R, R> {

	private long lastSystemtime = -1;
	private long lastApplicationTime;
	final private TimeUnit applicationTimeUnit;
	private boolean terminate;

	public SyncWithSystemTimePO(TimeUnit applicationTimeUnit) {
		this.applicationTimeUnit = applicationTimeUnit;
	}

	public SyncWithSystemTimePO(SyncWithSystemTimePO<R> syncWithSystemTimePO) {
		this.applicationTimeUnit = syncWithSystemTimePO.applicationTimeUnit;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		lastSystemtime = -1;
		this.terminate = false;
	}

	@Override
	protected synchronized void process_close() {
		this.terminate = true;
		this.notifyAll();
	}

	@Override
	protected synchronized void process_next(R object, int port) {
		if (lastSystemtime > 0) {
			long currentApplicationTime = object.getMetadata().getStart()
					.getMainPoint();
			long applicationTimeDiff = applicationTimeUnit.toMillis(currentApplicationTime
					- lastApplicationTime);
			long waitUntil = System.currentTimeMillis()+applicationTimeDiff;
			while (System.currentTimeMillis() < waitUntil
					&& !terminate) {
				try {
					this.wait(waitUntil-System.currentTimeMillis());
				} catch (InterruptedException e) {
				}
			}
		}
		lastSystemtime = System.currentTimeMillis();
		lastApplicationTime = object.getMetadata().getStart().getMainPoint();
		transfer(object);
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new SyncWithSystemTimePO<R>(this);
	}

}
