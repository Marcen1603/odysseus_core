package de.uniol.inf.is.odysseus.dsp.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class ConstantInterpolation<T extends Tuple<ITimeInterval>> implements InterpolationMethod<T> {
	@Override
	public boolean canInterpolate(PointInTime pointInTime, T lastObject, T newObject) {
		return PointInTime.before(pointInTime, lastObject.getMetadata().getEnd());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T interpolate(PointInTime pointInTime, T lastObject, T newObject) {
		return (T) SampleSignalPO.createCloneWithNewTime(lastObject, pointInTime);
	}
}
