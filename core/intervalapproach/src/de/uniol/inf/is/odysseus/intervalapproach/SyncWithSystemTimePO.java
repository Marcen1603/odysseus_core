package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class SyncWithSystemTimePO<R extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<R, R> {

	private long lastSystemtime = -1;
	private long lastApplicationTime;
	final private TimeUnit applicationTimeUnit;	

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
	}
	
	
	@Override
	protected void process_next(R object, int port) {
		if (lastSystemtime > 0){
			long currentApplicationTime = object.getMetadata().getStart().getMainPoint();
			long applicationTimeDiff = currentApplicationTime - lastApplicationTime;
			if (applicationTimeDiff > 0){
				try {
					applicationTimeUnit.sleep(applicationTimeDiff);
				} catch (InterruptedException e) {
				}
			}
		}else{
			lastSystemtime = System.currentTimeMillis();
			lastApplicationTime = object.getMetadata().getStart().getMainPoint();
		}
		transfer(object);
	}

	@Override
	public AbstractPipe<R,R> clone() {
		return new SyncWithSystemTimePO<R>(this);
	}

}
