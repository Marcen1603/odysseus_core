package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.billingmodel.BillingHelper;
import de.uniol.inf.is.odysseus.core.server.sla.metric.UpdateRateSink;
import de.uniol.inf.is.odysseus.core.server.sla.metric.UpdateRateSource;

public class SLAViolationBilling implements ISLAViolationEventListener {
	
	public SLAViolationBilling() {}

	@Override
	public void slaViolated(SLAViolationEvent event) {
		if (event.getServiceLevel() != 0) { // if ServiceLevel = 0 no violation occurred
			System.out.println("Violation billing: " + event.getCost());
			if (event.getMetric() instanceof UpdateRateSource)
				BillingHelper.getBillingManager().addPaymentSanction(event.getQuery().getSession().getUser().getId(), event.getQuery().getID(), event.getCost());
			else if (event.getMetric() instanceof UpdateRateSink)
				BillingHelper.getBillingManager().addRevenueSanction(event.getQuery().getSession().getUser().getId(), event.getQuery().getID(), event.getCost());
		}
	}
}
