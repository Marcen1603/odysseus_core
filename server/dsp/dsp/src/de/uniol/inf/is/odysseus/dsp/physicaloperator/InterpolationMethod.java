package de.uniol.inf.is.odysseus.dsp.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface InterpolationMethod<T> {
	boolean canInterpolate(PointInTime pointInTime, T lastObject, T newObject);
	T interpolate(PointInTime pointInTime, T lastObject, T newObject);
}