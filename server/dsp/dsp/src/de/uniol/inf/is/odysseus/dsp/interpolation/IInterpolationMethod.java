package de.uniol.inf.is.odysseus.dsp.interpolation;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface IInterpolationMethod<T> {
	boolean canInterpolate(PointInTime pointInTime, T lastObject, T newObject);
	T interpolate(PointInTime pointInTime, T lastObject, T newObject);
	boolean canInterpolateWithoutNewObject();
}