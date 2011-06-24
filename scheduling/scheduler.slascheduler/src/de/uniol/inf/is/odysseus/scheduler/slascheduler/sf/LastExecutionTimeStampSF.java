package de.uniol.inf.is.odysseus.scheduler.slascheduler.sf;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IStarvationFreedom;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLARegistryInfo;

/**
 * Starvation freedom function based on the timestamp of the last execution. The
 * older the timestamp, the higher the cost calculated by starvation freedom
 * function.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class LastExecutionTimeStampSF implements IStarvationFreedom {
	/**
	 * reference to the scheduling data of the related partial plan
	 */
	private SLARegistryInfo info;

	/**
	 * creates a new last execution timestamp-based starvation freedom function
	 * 
	 * @param info
	 *            scheduling data for the related partial plan
	 */
	public LastExecutionTimeStampSF(SLARegistryInfo info) {
		super();
		this.info = info;
	}

	/**
	 * quadratic function for starvation freedom
	 */
	@Override
	public double sf(double decay) {
		// use quadratic function
		long waitingTime = this.info.getLastExecTimeStamp()
				- System.currentTimeMillis();
		double sf = waitingTime * decay;
		return sf * sf;
	}

}
