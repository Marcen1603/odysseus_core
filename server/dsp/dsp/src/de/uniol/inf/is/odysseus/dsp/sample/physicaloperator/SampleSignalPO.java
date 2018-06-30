package de.uniol.inf.is.odysseus.dsp.sample.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class SampleSignalPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	private T lastObject;
	private int sampleInterval;
	private InterpolationMethod<T> interpolationMethod;

	public SampleSignalPO(final int sampleInterval, final InterpolationMethod<T> interpolationMethod) {
		super();
		this.sampleInterval = sampleInterval;
		this.interpolationMethod = interpolationMethod;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {
		if (this.lastObject != null) {
			interpolate(object);
		}
		this.lastObject = object;
	}

	private void interpolate(T newObject) {
		PointInTime from = getNextSampleTime(this.lastObject.getMetadata().getStart());
		PointInTime to = newObject.getMetadata().getStart();

		for (PointInTime sampleTime = from; PointInTime.before(sampleTime, to); sampleTime = sampleTime.plus(this.sampleInterval)) {
			// todo interpolation window
			transfer(this.interpolationMethod.interpolate(sampleTime, this.lastObject, newObject));
		}
	}

	private PointInTime getNextSampleTime(final PointInTime pointInTime) {
		if (pointInTime.getMainPoint() % this.sampleInterval == 0) {
			return pointInTime;
		} else {
			return pointInTime.plus(this.sampleInterval).minus(pointInTime.getMainPoint() % this.sampleInterval);
		}
	}
}
