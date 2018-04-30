package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SampleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

public class TimeSamplePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private final TimeValueItem timeValue;
	private TimeValueItem lastTimeSent;
	private final TimeUnit baseTimeUnit;

	public TimeSamplePO(SampleAO sampleAO) {

		this.baseTimeUnit = sampleAO.getBaseTimeUnit();
		this.timeValue = new TimeValueItem(
				this.baseTimeUnit.convert(sampleAO.getTimeValue().getTime(), sampleAO.getTimeValue().getUnit()),
				this.baseTimeUnit);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		final TimeValueItem currentTime = new TimeValueItem(
				((ITimeInterval) object.getMetadata()).getStart().getMainPoint(), this.baseTimeUnit);

		if (this.lastTimeSent == null) {
			transfer(object, currentTime);
		} else if ((currentTime.getTime() >= this.lastTimeSent.getTime() + this.timeValue.getTime())) {
			transfer(object, currentTime);
		} else {
			this.transfer(object, 1);
		}

	}

	private void transfer(T object, final TimeValueItem currentTime) {
		this.lastTimeSent = currentTime;
		this.transfer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
