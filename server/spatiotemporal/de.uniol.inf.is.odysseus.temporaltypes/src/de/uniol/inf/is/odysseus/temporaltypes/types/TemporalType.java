package de.uniol.inf.is.odysseus.temporaltypes.types;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public interface TemporalType<T> {
	
	public T getValue(PointInTime time);
	
	public T[] getValues(TimeInterval interval);

}
