package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * This sweep area sub class stores grouping attributes for later 
 * evaluation of the partial nest (transforming to a relational tuple) 
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingNestTISweepArea<M extends ITimeInterval & IProbability> 
    extends DefaultTISweepArea<ObjectTrackingPartialNest<M>> {
	
	Object[] groupingValues;

	public ObjectTrackingNestTISweepArea(Object[] groupingValues) {
		super();
		this.groupingValues = groupingValues.clone();
	}

	public ObjectTrackingNestTISweepArea(ObjectTrackingNestTISweepArea<M> toCopy) {
	    super(toCopy);
	    this.groupingValues = toCopy.groupingValues.clone();
	}
	
	public Object[] getGroupingValues() {
		return this.groupingValues;
	}
	
	public ObjectTrackingNestTISweepArea<M> clone() {
	    return new ObjectTrackingNestTISweepArea<M>(this);
	}
}
