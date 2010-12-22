package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;


abstract class AbstractTimebasedSLAScheduler extends AbstractSLAScheduler {

	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	protected long calcMinTimePeriod(IPartialPlan plan) {
		long minTimePeriod = Long.MAX_VALUE;
		for (IQuery q : plan.getQueries()) {
			IServiceLevelAgreement sla = TenantManagement.getInstance()
					.getSLAForUser(q.getUser());
			if (((ITimeBasedServiceLevelAgreement) sla).getTimeperiod() < minTimePeriod) {
				minTimePeriod = ((ITimeBasedServiceLevelAgreement) sla)
						.getTimeperiod();
			}
		}// Query
		return minTimePeriod;
	}
	
	public AbstractTimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
	}
	
	public AbstractTimebasedSLAScheduler(AbstractSLAScheduler simpleSLAScheduler) {
		super(simpleSLAScheduler);
	}

}
