package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;

/**
 * Interface for sla conformance objects
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface ISLAConformance {
	/**
	 * @return the sla conformance of a partial plan
	 */
	public double getConformance();

	/**
	 * resets the internal data of the sla conformance object
	 */
	public void reset();

	/**
	 * triggers the check for sla violation
	 */
	public void checkViolation();

	public void setBuffers(List<IBuffer<?>> buffers);
	
	public double predictConformance();
	
	public double getMaxPredictedLatency();
	
	public double getSumPredictedLatency();
	
	public int getNumberOfPredictedLatency();
	
	public int getNumberOfViolationsPredictedLatency();
	
}
