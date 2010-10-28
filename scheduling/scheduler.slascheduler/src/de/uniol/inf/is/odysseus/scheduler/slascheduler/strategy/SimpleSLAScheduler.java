package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.AbstractDynamicPriorityPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class SimpleSLAScheduler extends AbstractDynamicPriorityPlanScheduling {

	
	public SimpleSLAScheduler(SimpleSLAScheduler simpleSLAScheduler) {
		super(simpleSLAScheduler);
	}

	public SimpleSLAScheduler() {
	}
	
	@Override
	protected void updatePriorities(IScheduling current) {
		// Current Plan is irrelevant
		synchronized(queue){
			Map<IQuery, Double> calcedUrg = new HashMap<IQuery, Double>();
			for (IScheduling is:queue){
				// Calc prio for each query
				for (IQuery q:is.getPlan().getQueries()){
					// A query can be part of more than one scheduling
					// and only needs to be calced ones
					if (calcedUrg.containsKey(q)) continue;
					
					IPlanMonitor<Double> monitor = q.getPlanMonitor("SLAMonitor");
					IServiceLevelAgreement sla = TenantManagement.getInstance().getSLAForUser(q.getUser()); 
					double urge = 0.0;
					try {
						urge = sla.getMaxOcMg(monitor.getValue());
					} catch (NotInitializedException e) {
						throw new RuntimeException(e);
					}
					calcedUrg.put(q,urge);
				}
				// Calc Prio for current Scheduling, based
				// on all queries
				// e.g. with Max
				double max = 0;
				for (IQuery q:is.getPlan().getQueries()){
					max = Math.max(max, calcedUrg.get(q));
				}
				// Set max as new prio
				is.getPlan().setCurrentPriority((long)Math.round(max*100));
			}
		}
		
		
		
		// Jetzt für alle zu schedulden Plaene die entsprechenden SLAs
		// identifizieren
		// TODO: STATISCH BERECHNEN
//		synchronized (queue) {
//			for (IScheduling scheduling : queue) {
//				IPartialPlan plan = scheduling.getPlan();
//				List<IPhysicalOperator> roots = plan.getRoots();
//				for (IPhysicalOperator r:roots){
//					// TODO: Ermitteln wie  die currentSLAConformance ist
//					double currentSLAConformance = 0.5; 
//					List<IOperatorOwner> owners = r.getOwner();
//					for (IOperatorOwner owner: owners){
//						if (owner instanceof IQuery){
//							User user = ((IQuery)owner).getUser();
// 							Tenant t = TenantManagement.getInstance().getTenant(user);
//							IServiceLevelAgreement sla = t.getServiceLevelAgreement();
//							try {
//								double urge = sla.getMaxOcMg(currentSLAConformance);
//							} catch (NotInitializedException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}
//		}

	}

	@Override
	public SimpleSLAScheduler clone() {
		return new SimpleSLAScheduler(this);
	}

}
