package de.uniol.inf.is.odysseus.dsp.interpolation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.dsp.physicaloperator.SampleSignalPO;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

public class LinearInterpolation<T extends Tuple<ITimeInterval>> implements InterpolationMethod<T> {
	@Override
	public boolean canInterpolate(final PointInTime pointInTime, final T lastObject, final T newObject) {
		return PointInTime.before(newObject.getMetadata().getStart(), lastObject.getMetadata().getEnd());
	}

	@SuppressWarnings("unchecked")
	@Override
	public T interpolate(final PointInTime pointInTime, final T lastObject, final T newObject) {
		final T clone = (T) SampleSignalPO.createCloneWithNewTime(lastObject, pointInTime);

		final long startPoint = lastObject.getMetadata().getStart().getMainPoint();
		final long endPoint = newObject.getMetadata().getStart().getMainPoint();
		final double[] arguments = new double[] { startPoint, endPoint };

		for (int i = 0; i < lastObject.size(); i++) {
			final double[] values = new double[] { lastObject.getAttribute(i), newObject.getAttribute(i) };
			final double interpolatedValue = new LinearInterpolator().interpolate(arguments, values)
					.value(pointInTime.getMainPoint());
			clone.setAttribute(i, interpolatedValue);
		}

		return clone;
	}

	@Override
	public boolean canInterpolateWithoutNewObject() {
		return false;
	}
}
