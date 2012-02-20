package de.uniol.inf.is.odysseus.ac.standard;

import java.util.Collection;

import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

/**
 * Repräsentiert die Standardimplementierung einer {@link IPossibleExecution}.
 * Es beinhaltet die Information, welche Anfragen gestoppt werden sollten und
 * welche Anfragen weiterhin laufen können.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardPossibleExecution implements IPossibleExecution {

	private final Collection<IPhysicalQuery> runningQueries;
	private final Collection<IPhysicalQuery> stoppingQueries;
	private final ICost costEstimation;

	/**
	 * Konstruktor. Erstellt eine neue {@link StandardPossibleExecution}-Instanz mit
	 * gegebenen laufenden und stoppenden Anfragen sowie der dazugehörigen Kostenschätzung.
	 *  
	 * @param runningQueries Liste der laufenden Anfragen
	 * @param stoppingQueries Liste der zu stoppenden Anfragen
	 * @param costEstimation Kostenschätzung im Falle der Ausführung des Vorschlags.
	 */
	public StandardPossibleExecution(Collection<IPhysicalQuery> runningQueries, Collection<IPhysicalQuery> stoppingQueries, ICost costEstimation) {
		this.runningQueries = runningQueries;
		this.stoppingQueries = stoppingQueries;
		this.costEstimation = costEstimation;
	}

	@Override
	public Collection<IPhysicalQuery> getRunningQueries() {
		return runningQueries;
	}

	@Override
	public Collection<IPhysicalQuery> getStoppingQueries() {
		return stoppingQueries;
	}

	@Override
	public ICost getCostEstimation() {
		return costEstimation;
	}

}
