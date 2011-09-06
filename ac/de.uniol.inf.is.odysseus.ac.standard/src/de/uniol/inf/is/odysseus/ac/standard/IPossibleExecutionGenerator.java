package de.uniol.inf.is.odysseus.ac.standard;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * Schnittstelle für alle Algorithmen, die aus einer Menge von Anfragen (mit
 * Kostenschätzungen) Vorschläge zur Überlastkompensation generieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPossibleExecutionGenerator {

	/**
	 * Generiert eine Liste von möglichen Vorschlägen, um eine Überlastung
	 * aufzuheben. Ist keine Überlastung festgestellt worden, sollte in der
	 * Implementierung genau ein Vorschlag - alle Anfragen auszuführen -
	 * zurückgegeben werden.
	 * 
	 * @param sender
	 *            {@link IAdmissionControl}, welches die Generierung von
	 *            Vorschlägen auslöst.
	 * @param queryCosts
	 *            Aktuelle Zuordnung Anfrage - Kostenschätzung
	 * @param maxCost
	 *            Maximal zulässige Kosten
	 * @return Liste an möglichen Vorschlägen zur Überlastkompensation.
	 */
	public List<IPossibleExecution> getPossibleExecutions(IAdmissionControl sender, Map<IQuery, ICost> queryCosts, ICost maxCost);
}
