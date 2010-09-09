package de.uniol.inf.is.odysseus.planmanagement.executor;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionHandler;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

/**
 * IPlanScheduling beschreibt die Möglichkeit das Scheduling innerhalb von
 * Odysseus zu beeinflussen. Und Informationenen darüber abzurufen. Dies ist
 * neben IPlanManager eine der beiden Grundlagen für die Ausführungsumgebung.
 * 
 * @author wolf
 * 
 */
public interface IPlanScheduling extends IPlanExecutionHandler {

	/**
	 * getSealedExecutionPlan liefert den aktuellen physischen Ausführungsplan.
	 * @return den aktuellen physischen Ausführungsplan
	 */
	public IExecutionPlan getExecutionPlan();

	/**
	 * startExecution startet die Ausführung von Odysseus.
	 * @throws PlanManagementException
	 */
	public void startExecution() throws PlanManagementException;

	/**
	 * stopExecution stopped die Ausführung von Odysseus.
	 * @throws PlanManagementException
	 */
	public void stopExecution() throws PlanManagementException;

	/**
	 * isRunning gibt an, ob die Ausführung innerhalb von Odysseus läuft.
	 * @return true: Ausführung läuft. false: Ausführung läuft nicht.
	 * @throws PlanManagementException
	 */
	public boolean isRunning() throws PlanManagementException;
}
