package de.uniol.inf.is.odysseus.core.planmanagement;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * A helper interface to remove dependencies in stream.chart.chart.AbstractCommand 
 * 
 * Should be remvoved later!!
 * 
 * @author Marco Grawunder
 *
 */
public interface IHasRoots {
	
	public List<IPhysicalOperator> getRoots();

}
