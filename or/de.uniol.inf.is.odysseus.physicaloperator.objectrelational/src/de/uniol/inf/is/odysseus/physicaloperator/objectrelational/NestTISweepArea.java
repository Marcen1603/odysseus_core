package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

/**
 * 
 * This sweep area sub class stores grouping attributes for later 
 * evaluation of the partial nest (transforming to a relational tuple) 
 * 
 * @author Jendrik Poloczek
 *
 */
public class NestTISweepArea extends 
	DefaultTISweepArea<PartialNest<TimeInterval>> {
	
	Object[] groupingValues;

	public NestTISweepArea(Object[] groupingValues) {
		super();
		this.groupingValues = groupingValues.clone();
	}
	
	public Object[] getGroupingValues() {
		return this.groupingValues;
	}
}
