package de.uniol.inf.is.odysseus.dsp.sample.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class ConstantInterpolation<T extends Tuple<ITimeInterval>> implements InterpolationMethod<T> {
	@SuppressWarnings("unchecked")
	@Override
	public T interpolate(PointInTime pointInTime, T lastObject, T newObject) {
		T clone = (T) lastObject.clone();
		clone.setMetadata((ITimeInterval) lastObject.getMetadata().clone());
		clone.getMetadata().setStartAndEnd(pointInTime, PointInTime.INFINITY);
		return clone;
	}
}
