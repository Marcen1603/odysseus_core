package de.uniol.inf.is.odysseus.dsp.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class SampleSignalPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	private T lastObject;
	private final int sampleInterval;
	private final InterpolationMethod<T> interpolationMethod;
	private final boolean fillWithZeros;

	public SampleSignalPO(final int sampleInterval, final InterpolationMethod<T> interpolationMethod,
			final boolean fillWithZeros) {
		super();
		this.sampleInterval = sampleInterval;
		this.interpolationMethod = interpolationMethod;
		this.fillWithZeros = fillWithZeros;
	}

	@Override
	public void processPunctuation(final IPunctuation punctuation, final int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(final T object, final int port) {
		if (this.lastObject != null) {
			interpolate(object);
		}
		this.lastObject = object;
	}

	@SuppressWarnings("unchecked")
	private void interpolate(final T newObject) {
		final PointInTime from = getNextSampleTime(this.lastObject.getMetadata().getStart());
		final PointInTime to = newObject.getMetadata().getStart();

		for (PointInTime sampleTime = from; PointInTime.before(sampleTime,
				to); sampleTime = sampleTime.plus(this.sampleInterval)) {
			if (this.interpolationMethod.canInterpolate(sampleTime, this.lastObject, newObject)) {
				transfer(this.interpolationMethod.interpolate(sampleTime, this.lastObject, newObject));
			}
			else if (this.fillWithZeros) {
				final T clone = (T) createCloneWithNewTime(this.lastObject, sampleTime);
				for (int i = 0; i < clone.size(); i++) {
					clone.setAttribute(i, 0);
				}
				transfer(clone);
			}
		}
	}

	private PointInTime getNextSampleTime(final PointInTime pointInTime) {
		if (pointInTime.getMainPoint() % this.sampleInterval == 0) {
			return pointInTime;
		} else {
			return pointInTime.plus(this.sampleInterval).minus(pointInTime.getMainPoint() % this.sampleInterval);
		}
	}

	public static Tuple<ITimeInterval> createCloneWithNewTime(final Tuple<ITimeInterval> toBeCloned,
			final PointInTime pointInTime) {
		final Tuple<ITimeInterval> clone = toBeCloned.clone();
		clone.setMetadata((ITimeInterval) toBeCloned.getMetadata().clone());
		clone.getMetadata().setStartAndEnd(pointInTime, PointInTime.INFINITY);
		return clone;
	}
}
