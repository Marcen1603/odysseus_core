package de.uniol.inf.is.odysseus.dsp.sample.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface InterpolationMethod<T> {
	T interpolate(PointInTime pointInTime, T lastObject, T newObject);
}