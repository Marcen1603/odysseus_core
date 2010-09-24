package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.base.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.base.usermanagement.Tenant;
import de.uniol.inf.is.odysseus.base.usermanagement.TenantManagement;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.AbstractDynamicPriorityPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

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
