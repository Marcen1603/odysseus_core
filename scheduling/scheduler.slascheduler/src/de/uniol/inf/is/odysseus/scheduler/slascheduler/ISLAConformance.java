package de.uniol.inf.is.odysseus.scheduler.slascheduler;

/**
 * Interface for sla conformance objects
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
	
}
