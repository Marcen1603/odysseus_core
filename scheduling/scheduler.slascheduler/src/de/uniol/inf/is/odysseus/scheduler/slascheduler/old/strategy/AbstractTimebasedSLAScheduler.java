/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scheduler.slascheduler.old.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;


abstract class AbstractTimebasedSLAScheduler extends AbstractSLAScheduler {

	private final long sla_history_size = OdysseusDefaults.getLong(
			"sla_history_size", 1000);
	
	private Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

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
	
	protected ScheduleMeta drainHistory(IScheduling is) {
		ScheduleMeta scheduleMeta = is.getPlan().getScheduleMeta();
		// Drain
		scheduleMeta.drainHistory(sla_history_size);
		return scheduleMeta;
	}
	
	@Override
	public void removePlan(IScheduling plan) {
		super.removePlan(plan);
		minTime.remove(plan);
	}
	
	protected void calcMinTime(IScheduling is) {
		if (minTime.get(is) == null) {
			long minTimeP = calcMinTimePeriod(is.getPlan());
			minTime.put(is, minTimeP);
			//logger.debug("MinTime for "+is+" set to "+minTimeP);
		}		
	}

	
	public AbstractTimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
	}
	
	public AbstractTimebasedSLAScheduler(AbstractSLAScheduler simpleSLAScheduler) {
		super(simpleSLAScheduler);
	}
	
	protected IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		ScheduleMeta meta = toSchedule.getPlan().getScheduleMeta();
		meta.scheduleDone(minTime.get(toSchedule));
		return toSchedule;
	}

}
