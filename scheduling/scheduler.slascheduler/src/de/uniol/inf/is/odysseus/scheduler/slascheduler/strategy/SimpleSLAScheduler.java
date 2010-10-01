package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.AbstractDynamicPriorityPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.Tenant;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class SimpleSLAScheduler extends AbstractDynamicPriorityPlanScheduling {

	public SimpleSLAScheduler(SimpleSLAScheduler simpleSLAScheduler) {
		super(simpleSLAScheduler);
	}

	@Override
	protected void updatePriorities(IScheduling current) {
		// Der aktuelle interessiert gar nicht
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
