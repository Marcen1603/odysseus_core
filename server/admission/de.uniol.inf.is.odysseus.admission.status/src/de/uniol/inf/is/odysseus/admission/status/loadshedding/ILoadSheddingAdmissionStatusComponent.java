package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Interface for all LoadSheddingAdmissionStatusComponents.
 * 
 * @author Jannes
 *
 */
public interface ILoadSheddingAdmissionStatusComponent extends IAdmissionStatusComponent {
	
	static public final ISession superUser = UserManagementProvider.getUsermanagement(true).getSessionManagement().loginSuperUser(null);
	
	/**
	 * Adds the query to the StatusComponent.
	 * @param query
	 */
	public void addQuery(int queryID);
	
	/**
	 * Removes the Query from the StatusComponent.
	 * @param query
	 */
	public void removeQuery(int queryID);
	
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

	/**
	 * Returns the name of this component.
	 * @return
	 */
	public String getComponentName();

}
