package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IApplicationTime extends IMetaAttribute, IClone {

	// writing
	public void addApplicationInterval(ITimeInterval interval);

	public void addAllApplicationIntervals(List<ITimeInterval> intervals);

	public void setApplicationIntervals(List<ITimeInterval> intervals);

	// reading
	public ITimeInterval getApplicationInterval(int pos);

	public List<ITimeInterval> getAllApplicationTimeIntervals();

	// removing
	public void removeApplicationInterval(ITimeInterval interval);

	public ITimeInterval removeApplicationInterval(int pos);

	public void clearApplicationIntervals();

	// other
	public boolean isApplictionTimeValid();

	@Override
	public IApplicationTime clone();
}
