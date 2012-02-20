package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.sf.ElementTimeStampSF;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.sf.LastExecutionTimeStampSF;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.sf.QueueSizeSF;

/**
 * Factory for building implementations of {@link IStarvationFreedom} objects.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class StarvationFreedomFactory {

	public static final String ELEMENT_TS = "ElementTimeStampSF";
	public static final String LAST_EXEC_TS = "LastExecutionTimeStampSF";
	public static final String QUEUE_SIZE = "QueueSizeSF";

	/**
	 * Builds a new omject that implements {@link IStarvationFreedom}
	 * 
	 * @param starvationFreedomFuncName
	 *            the name of the implementation as defined in the constants of
	 *            this class
	 * @param schedData
	 *            the scheduling data of the given plan
	 * @param plan
	 *            the plan the constructed object is used for
	 * @return a new Object implementing {@link IStarvationFreedom}, or null if
	 *         the given starvation freedom function name is invalid
	 */
	public IStarvationFreedom buildStarvationFreedom(
			String starvationFreedomFuncName, SLARegistryInfo schedData,
			IPhysicalQuery query) {
		if (ELEMENT_TS.equals(starvationFreedomFuncName)) {
			return new ElementTimeStampSF(query);
		} else if (LAST_EXEC_TS.equals(starvationFreedomFuncName)) {
			return new LastExecutionTimeStampSF(schedData);
		} else if (QUEUE_SIZE.equals(starvationFreedomFuncName)) {
			return new QueueSizeSF(query);
		} else {
			return null;
		}
	}

}
