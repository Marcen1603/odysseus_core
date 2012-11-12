package de.uniol.inf.is.odysseus.ac.sla;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionQuerySelector;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPhysicalQueryScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SimpleThreadScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLARegistry;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLARegistryInfo;

public class SLAPossibleExecutionGenerator implements IAdmissionQuerySelector {
	
	private SLARegistry slaRegistry;

	@Override
	public ImmutableList<IPhysicalQuery> determineQueriesToStop(IAdmissionControl admissionControl, List<IPhysicalQuery> runningQueries) {
		if( runningQueries.isEmpty() ) {
			return ImmutableList.of();
		}
		
		IPhysicalQuery mostExpensiveQuery = null;
		double maxPenalty = -1.0;

		for (IPhysicalQuery q : runningQueries) {
			SLA sla = (SLA) q.getParameter(SLA.class.getName());
			SLARegistryInfo info = this.slaRegistry.getData(q);
			
			if (sla != null && info != null) {
				double killPenalty = sla.getQueryKillPenalty().getCost();
				double slViolationPenalty = this.getCostOfServiceLevel(info, sla);

				double penaltyDifference = killPenalty - slViolationPenalty;
				
				if (penaltyDifference > maxPenalty) {
					maxPenalty = penaltyDifference;
					mostExpensiveQuery = q;
				}
			}
		}
		
		return ImmutableList.of(mostExpensiveQuery);
	}

	@Override
	public ImmutableList<IPhysicalQuery> determineQueriesToStart(IAdmissionControl admissionControl, List<IPhysicalQuery> stoppedQueries) {
		return null;
	}
	
	private double getCostOfServiceLevel(SLARegistryInfo info, SLA sla) {
		double conformance = info.getConformance().getConformance();
		int slIndex = info.getCostFunction().getCurrentServiceLevelIndex(conformance, sla);
		double slViolationPenalty = sla.getServiceLevel().get(slIndex).getPenalty().getCost();
		
		return slViolationPenalty;
	}
	
	public void bindExecutor(IExecutor executor) {
		if (executor instanceof IServerExecutor) {
			IServerExecutor serverExecutor = (IServerExecutor) executor;
			IScheduler scheduler = serverExecutor.getSchedulerManager().getActiveScheduler();
			if (scheduler instanceof SimpleThreadScheduler) {
				SimpleThreadScheduler stScheduler = (SimpleThreadScheduler) scheduler;
				IPhysicalQueryScheduling[] planScheduling = stScheduler.getPlanScheduling();
				for (IPhysicalQueryScheduling pqs: planScheduling) {
					if (pqs instanceof SLAPartialPlanScheduling) {
						SLAPartialPlanScheduling slaSched = (SLAPartialPlanScheduling)planScheduling[0];
						this.slaRegistry = slaSched.getRegistry();
						break;
					}
				}
				
			}
		}
	}
	
	public void unbindExecutor(IExecutor executor) {
		this.slaRegistry = null;
	}


}
