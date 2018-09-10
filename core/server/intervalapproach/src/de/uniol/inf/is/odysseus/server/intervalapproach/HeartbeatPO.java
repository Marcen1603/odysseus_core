package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator assures that every n time elements there will be a heartbeat on
 * the guarantees that no element (heartbeat or streamobject) is send that is
 * older than the last send hearbeat (i.e. the generated heartbeats are in order
 * and indicate time progress). Heartbeats can be send periodically
 * (sendAlwaysHeartbeats = true) or only if no other stream elements indicate
 * time progress (e.g. in out of order scenarios) independent if a new element
 * has been received or not.
 *
 * @author Marco Grawunder
 *
 * @param <R>
 *            The type of the StreamObject (must have ITimeInterval as metadata)
 */

public class HeartbeatPO<R extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<R, R> {

	static final Logger LOG = LoggerFactory.getLogger(HeartbeatPO.class);

	private long realTimeDelay;
	private long applicationTimeDelay;
	private boolean sendAlwaysHeartbeat = false;
	private boolean restartTimerForEveryInput = false;

	private PointInTime _watermark = PointInTime.getZeroTime();

	public HeartbeatPO() {
		super();
	}

	public HeartbeatPO(boolean startAtCurrentTime) {
		super();
		if (startAtCurrentTime) {
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
			LOG.trace("Starting heartbeat generator "+HeartbeatPO.this.toString());
			while (!terminated) {
				try {
					restart = false;
					synchronized (this) {
						wait(realTimeDelay);
					}
					if (!restart) {
						PointInTime punctuation = getWatermark().sum(applicationTimeDelay);
						HeartbeatPO.this.sendPunctuation(Heartbeat.createNewHeartbeat(punctuation));
					}
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	};

	// Thread for creating heartbeats
	private Runner generateHeartbeat;
	private boolean startTimerAfterFirstElement = false;

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		generateHeartbeat = new Runner();
		if (!startTimerAfterFirstElement) {
			generateHeartbeat.start();
		}
	}

	@Override
	protected synchronized void process_next(R object, int port) {
		if (startTimerAfterFirstElement) {
			if (!generateHeartbeat.isAlive()) {
				generateHeartbeat.start();
			}
		}

		PointInTime marker = object.getMetadata().getStart();
		if (marker.afterOrEquals(getWatermark())) {
			transfer(object);
			restartTimer();
		} else {
			LOG.warn("Removed out of order object: " + object +" for watermark "+marker);
			if (isRestartTimerForEveryInput()) {
				restartTimer();
			}
			transfer(object, 99);
		}
	}

	@Override
	protected void process_close() {
		generateHeartbeat.terminate();
		generateHeartbeat = null;
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		PointInTime marker = punctuation.getTime();
		if (marker.afterOrEquals(getWatermark())) {
			sendPunctuation(punctuation);
			restartTimer();
		} else {
			LOG.warn("Punctuation removed because out of order " + punctuation);
		}
	}

	@Override
	public synchronized void sendPunctuation(IPunctuation punctuation) {
		PointInTime timestamp = punctuation.getTime();
		if (timestamp.afterOrEquals(getWatermark())) {
			LOG.trace("SEND PUNCTUATION "+punctuation+" oldWatermark="+getWatermark());
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
	}

	public void setApplicationTimeDelay(long applicationTimeDelay) {
		this.applicationTimeDelay = applicationTimeDelay;
	}

	public void setSendAlwaysHeartbeat(boolean sendAlwaysHeartbeat) {
		this.sendAlwaysHeartbeat = sendAlwaysHeartbeat;
	}

	/**
	 * @param startTimerAfterFirstElement
	 */
	public void setStartTimerAfterFirstElement(boolean startTimerAfterFirstElement) {
		this.startTimerAfterFirstElement = startTimerAfterFirstElement;
	}

	public boolean isRestartTimerForEveryInput() {
		return restartTimerForEveryInput;
	}

	public void setRestartTimerForEveryInput(boolean restartTimerForEveryInput) {
		this.restartTimerForEveryInput = restartTimerForEveryInput;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof HeartbeatPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		HeartbeatPO<R> po = (HeartbeatPO<R>) ipo;

		if (this.realTimeDelay != po.realTimeDelay) {
			return false;
		}

		if (this.applicationTimeDelay != po.applicationTimeDelay) {
			return false;
		}

		if (this.sendAlwaysHeartbeat != po.sendAlwaysHeartbeat) {
			return false;
		}
		
		if (!Objects.equals(this.restartTimerForEveryInput, po.restartTimerForEveryInput)) {
			return false;
		}

		return true;
	}

}
