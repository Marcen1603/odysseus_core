package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface ILoadSheddingAdmissionStatusComponent extends IAdmissionStatusComponent {
	
	static public final ISession superUser = UserManagementProvider.getUsermanagement(true).getSessionManagement().loginSuperUser(null);
	
	public final int MAX_SHEDDING_FACTOR = 50;
	
	
	/**
	 * Adds the query to the StatusComponent.
	 * @param query
	 */
	public void addQuery(IPhysicalQuery query);
	
	/**
	 * Removes the Query from the StatusComponent.
	 * @param query
	 */
	public void removeQuery(IPhysicalQuery query);
	
	/**
	 * Measure the status of the StatusComponent.
	 */
	public void measureStatus();
	
	/**
	 * Runs the load shedding algorithm.
	 */
	public void runLoadShedding();
	
	/**
	 * Rolls the load shedding algorithm back.
	 */
	public void rollBackLoadShedding();

}
