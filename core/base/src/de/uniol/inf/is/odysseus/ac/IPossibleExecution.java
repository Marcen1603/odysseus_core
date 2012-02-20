package de.uniol.inf.is.odysseus.ac;

import java.util.Collection;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

/**
 * {@link IPossibleExecution} stellt ein Vorschlag zur Kompensation einer
 * erkannten Überlastung dar. Es beinhaltet die Information, welche Anfragen
 * gestoppt werden sollten und welche Anfragen weiterhin laufen können.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPossibleExecution {

	/**
	 * Liefert alle Anfragen, die ausgeführt werden können.
	 * 
	 * @return Anfragen, die ausgeführt werden können.
	 */
	public Collection<IPhysicalQuery> getRunningQueries();

	/**
	 * Liefert alle Anfragen, die zur Überlastkompensation gestoppt
	 * werden sollten.
	 * 
	 * @return Liste von zu stoppenden Anfragen
	 */
	public Collection<IPhysicalQuery> getStoppingQueries();

	/**
	 * Liefert die Kostenschätzung für den Fall, dass dieser Vorschlag
	 * umgesetzt werden würde.
	 * 
	 * @return Kostenschätzung im Falle einer Umsetzung des Vorschlags.
	 */
	public ICost getCostEstimation();

}
