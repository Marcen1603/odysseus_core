package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;


public interface ITemporalSweepArea<T> extends ISweepArea<T> {
	public void purgeElementsBefore(PointInTime time);
	public Iterator<T> extractElementsBefore(PointInTime time);
	public ITemporalSweepArea<T> clone();
}
