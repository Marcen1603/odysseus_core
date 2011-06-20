package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public interface ICostFunction {
	
	public double oc(double conformance, SLA sla);
	
	public double mg(double conformance, SLA sla);

}
