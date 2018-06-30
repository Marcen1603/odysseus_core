package de.uniol.inf.is.odysseus.dsp.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class ConstantInterpolation<T extends Tuple<ITimeInterval>> implements InterpolationMethod<T> {
	@Override
	public boolean canInterpolate(final PointInTime pointInTime, final T lastObject, final T newObject) {
		return PointInTime.before(pointInTime, lastObject.getMetadata().getEnd());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T interpolate(final PointInTime pointInTime, final T lastObject, final T newObject) {
		return (T) SampleSignalPO.createCloneWithNewTime(lastObject, pointInTime);
	}
}
