package de.uniol.inf.is.odysseus.ac.sla;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.ac.StandardPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPhysicalQueryScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SimpleThreadScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLARegistry;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLARegistryInfo;

public class SLAPossibleExecutionGenerator implements IPossibleExecutionGenerator {
	
	private SLARegistry slaRegistry;

	@Override
	public List<IPossibleExecution> getPossibleExecutions(
			IAdmissionControl sender, Map<IPhysicalQuery, ICost> queryCosts,
			ICost overallCost, ICost maxCost) {
	
		List<IPossibleExecution> result = new ArrayList<>();
		IPhysicalQuery mostExpensiveQuery = null;
		double maxPenalty = -1.0;

		for (IPhysicalQuery q : queryCosts.keySet()) {
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

		IPossibleExecution possibleExec = StandardPossibleExecution.stopOneQuery(queryCosts, mostExpensiveQuery);
		result.add(possibleExec);

		return result;
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
