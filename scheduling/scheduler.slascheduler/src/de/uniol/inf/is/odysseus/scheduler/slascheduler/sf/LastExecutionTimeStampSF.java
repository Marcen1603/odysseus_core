package de.uniol.inf.is.odysseus.scheduler.slascheduler.sf;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IStarvationFreedom;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLARegistryInfo;

public class LastExecutionTimeStampSF implements IStarvationFreedom {
	
	private SLARegistryInfo info;
	
	public LastExecutionTimeStampSF(SLARegistryInfo info) {
		super();
		this.info = info;
	}

	@Override
	public double sf(double decay) {
		//use quadratic function
		long waitingTime = this.info.getLastExecTimeStamp() - System.currentTimeMillis();
		double sf = waitingTime * decay; 
		return sf * sf;
	}

}
