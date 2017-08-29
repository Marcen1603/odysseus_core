package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;

/**
 * Interface for all LoadSheddingAdmissionStatusComponents.
 */
public interface ILoadSheddingAdmissionStatusComponent extends IAdmissionStatusComponent {
	
	/**
	 * Adds the query to the StatusComponent.
	 * @param query
	 */
	public boolean addQuery(int queryID);
	
	/**
	 * Removes the Query from the StatusComponent.
	 * @param query
	 */
	public boolean removeQuery(int queryID);
	
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
	public void rollbackLoadShedding();

	/**
	 * Returns the name of this component.
	 * @return componentName - String
	 */
	public String getComponentName();

}
