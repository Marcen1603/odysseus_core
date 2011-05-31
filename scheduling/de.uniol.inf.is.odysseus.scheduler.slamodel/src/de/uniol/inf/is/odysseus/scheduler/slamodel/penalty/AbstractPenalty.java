package de.uniol.inf.is.odysseus.scheduler.slamodel.penalty;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Penalty;
import de.uniol.inf.is.odysseus.scheduler.slamodel.ServiceLevel;

public abstract class AbstractPenalty implements Penalty {
	
	private ServiceLevel<?> serviceLevel;

	public AbstractPenalty() {
		super();
	}

	public void setServiceLevel(ServiceLevel<?> serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public ServiceLevel<?> getServiceLevel() {
		return serviceLevel;
	}
	
}
