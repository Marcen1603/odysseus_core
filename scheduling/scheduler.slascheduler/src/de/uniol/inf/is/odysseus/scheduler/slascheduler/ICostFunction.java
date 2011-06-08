package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public interface ICostFunction {
	
	public int oc(int conformance, SLA sla);
	
	public int mg(int conformance, SLA sla);

}
