package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.sf.ElementTimeStampSF;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.sf.LastExecutionTimeStampSF;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.sf.QueueSizeSF;

public class StarvationFreedomFactory {
	
	public static final String ELEMENT_TS = "ElementTimeStampSF";
	public static final String LAST_EXEC_TS = "LastExecutionTimeStampSF";
	public static final String QUEUE_SIZE = "QueueSizeSF";
	
	public IStarvationFreedom buildStarvationFreedom(
			String starvationFreedomFuncName, 
			SLARegistryInfo schedData, 
			IPartialPlan plan) {
		if (ELEMENT_TS.equals(starvationFreedomFuncName)) {
			return new ElementTimeStampSF();
		} else if (LAST_EXEC_TS.equals(starvationFreedomFuncName)) {
			return new LastExecutionTimeStampSF(schedData);
		} else if (QUEUE_SIZE.equals(starvationFreedomFuncName)) {
			return new QueueSizeSF(plan);
		} else {
			return null;
		}
	}

}
