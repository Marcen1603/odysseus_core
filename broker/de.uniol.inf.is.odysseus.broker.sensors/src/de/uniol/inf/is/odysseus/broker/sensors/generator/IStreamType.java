package de.uniol.inf.is.odysseus.broker.sensors.generator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IStreamType{
	
	public SDFAttributeList getSchema();
	public RelationalTuple<ITimeInterval> getNextTuple();
	public long getWaitingMillis();
	
}
