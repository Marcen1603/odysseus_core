package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.billingmodel.BillingManager;

public class SLAViolationBilling implements ISLAViolationEventListener {
	
	public SLAViolationBilling() {}

	@Override
	public void slaViolated(SLAViolationEvent event) {
		System.out.println("Violtion billing: " + event.getCost());
		BillingManager.addSanction(event.getQuery().getSession().getUser().getId(), event.getQuery().getID(), event.getCost());
	}

}
