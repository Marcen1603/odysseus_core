package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator assures that every n time elements there will be a heartbeat on
 * the garantees, that no element (heartbeat or streamobject) is send, that is
 * older than the last send hearbeat (i.e. the generated heartbeats are in order
 * and indicate time progress). Heartbeats can be send periodically
 * (sendAlwaysHeartbeats = true) or only if no other stream elements indicate
 * time progess (e.g. in out of order scenarios) independent if a new element
 * has been received or not.
 * 
 * @author Marco Grawunder
 * 
 * @param <R>
 *            The type of the StreamObject (must have ITimeInterval as metadata)
 */

public class AssureHeartbeatPO<R extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<R, R> {

	static final Logger LOG = LoggerFactory.getLogger(AssureHeartbeatPO.class);
	
	private long realTimeDelay;
	private long applicationTimeDelay;
	private boolean sendAlwaysHeartbeat = false;
	private boolean allowOutofOrder = false;

	private PointInTime _watermark = PointInTime.getZeroTime();

	public AssureHeartbeatPO() {
		super();
	}
	public AssureHeartbeatPO(boolean startAtCurrentTime) {
		super();
		if(startAtCurrentTime) {
			this._watermark = PointInTime.currentPointInTime();
		}
	}
	
	private class Runner extends Thread {

		private boolean terminated = false;
		private boolean restart = false;

		void terminate() {
			terminated = true;
			restart();
		}

		synchronized void restart() {
			restart = true;
			notifyAll();
		}

		@Override
		public void run() {
			while (!terminated) {
				try {
					restart = false;
					synchronized (this) {
						wait(realTimeDelay);
					}
					if (!restart) {
						PointInTime punctuation = getWatermark().sum(
								applicationTimeDelay);
						AssureHeartbeatPO.this.sendPunctuation(Heartbeat.createNewHeartbeat(punctuation));
					}
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	};

	private Runner generateHeartbeat = new Runner();
	private boolean startTimerAfterFirstElement = false;

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		if (!startTimerAfterFirstElement){
			generateHeartbeat.start();
		}
	}

	@Override
	protected void process_next(R object, int port) {
		if (startTimerAfterFirstElement){
			if (!generateHeartbeat.isAlive()){
				generateHeartbeat.start();
			}
		}

		PointInTime marker = object.getMetadata().getStart();
		if (marker.afterOrEquals(getWatermark())) {
			transfer(object);
			if (!allowOutofOrder) {
				setWatermark(marker);
			}
			restartTimer();
		} else {
			LOG.warn("Obejct removed because out of order "+object);
			transfer(object, 99);
		}
	}

	@Override
	protected void process_close() {
		generateHeartbeat.terminate();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		PointInTime marker = punctuation.getTime();
		if (marker.afterOrEquals(getWatermark())){
			sendPunctuation(punctuation);
			if (!allowOutofOrder){
				setWatermark(marker);
			}
			restartTimer();
		}else{
			LOG.warn("Punctuation removed because out of order "+punctuation);
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		PointInTime timestamp = punctuation.getTime();
		if (timestamp.afterOrEquals(getWatermark())) {
			super.sendPunctuation(punctuation);
			setWatermark(timestamp);
			restartTimer();
		}
	}

	private void restartTimer() {
		// Restart timer only if heartbeats should be suppressed in
		// case of receiving elements
		if (!sendAlwaysHeartbeat) {
			generateHeartbeat.restart();
		}
	}

	public synchronized PointInTime getWatermark() {
		return _watermark;
	}

	public synchronized void setWatermark(PointInTime watermark) {
		this._watermark = watermark;
	}

	public void setRealTimeDelay(long realTimeDelay, TimeUnit timeUnit) {
		this.realTimeDelay = timeUnit.toMillis(realTimeDelay);
		;
	}

	public void setApplicationTimeDelay(long applicationTimeDelay) {
		this.applicationTimeDelay = applicationTimeDelay;
	}

	public void setSendAlwaysHeartbeat(boolean sendAlwaysHeartbeat) {
		this.sendAlwaysHeartbeat = sendAlwaysHeartbeat;
	}
	
	public void setAllowOutOfOrder(boolean allowOutofOrder) {
		this.allowOutofOrder = allowOutofOrder;
	}

	@Override
	public AbstractPipe<R, R> clone() {
		throw new IllegalArgumentException("Clone not supported");
	}
	/**
	 * @param startTimerAfterFirstElement
	 */
	public void setStartTimerAfterFirstElement(
			boolean startTimerAfterFirstElement) {
		this.startTimerAfterFirstElement  = startTimerAfterFirstElement;
	}

}
