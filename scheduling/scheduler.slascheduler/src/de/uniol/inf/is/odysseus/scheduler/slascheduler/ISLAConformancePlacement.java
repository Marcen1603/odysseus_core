package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

/**
 * Interface for placing/removing sla conformance operators in/from partial
 * plans
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface ISLAConformancePlacement {

	/**
	 * places the given conformance in the given partial plan
	 * 
	 * @param plan
	 *            the partial plan
	 * @param conformance
	 *            the sla conformance operator
	 * @return the operator from the given plan, where the sla conformance
	 *         operator is appended to
	 */
	public ISubscribable<?, ?> placeSLAConformance(IPartialPlan plan,
			ISLAConformance conformance);

	/**
	 * removes the given sla conformance operator from the given physical
	 * operator it is connected to
	 * 
	 * @param connectionPoint
	 *            the physical operator the sla conformance operator is
	 *            connected to
	 * @param conformance the sla conformance operator to remove
	 */
	public void removeSLAConformance(ISubscribable<?, ?> connectionPoint,
			ISLAConformance conformance);

}
