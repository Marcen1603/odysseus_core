package de.uniol.inf.is.odysseus.planmanagement.executor;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationHandler;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

/**
 * IPlanManager bildet mit IPlanScheduling die Grundlage für die
 * Ausführungumgebung von Odysseus. Diese Schnittstelle bietet die Möglichkeit
 * Informationen über registrierte Anfragen abzufragen bzw. zu bearbeiten.
 * 
 * @author wolf
 * 
 */
public interface IPlanManager extends IPlanModificationHandler {
	/**
	 * removeQuery entfernt eine Anfrage aus Odysseus.
	 * 
	 * @param queryID
	 *            ID der Anfrage
	 * @throws PlanManagementException
	 */
	public void removeQuery(int queryID) throws PlanManagementException;

	/**
	 * startQuery startet eine Anfrage.
	 * 
	 * @param queryID
	 *            ID der Anfrage
	 * @throws PlanManagementException
	 */
	public void startQuery(int queryID) throws PlanManagementException;

	/**
	 * stopQuery stoppt eine Anfrage und entfernt sie
	 * 
	 * @param queryID
	 * @throws PlanManagementException
	 */
	public void stopQuery(int queryID) throws PlanManagementException;

	/**
	 * getSealedPlan liefert alle in Odysseus registrierten Anfragen.
	 * 
	 * @return alle in Odysseus registrierten Anfragen
	 * @throws PlanManagementException
	 */
	public IPlan getSealedPlan() throws PlanManagementException;
}