package de.uniol.inf.is.odysseus.ac.standard;

import java.util.Collection;

import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class StandardPossibleExecution implements IPossibleExecution {

	private final Collection<IQuery> runningQueries;
	private final Collection<IQuery> stoppingQueries;
	private final ICost costEstimation;
	
	public StandardPossibleExecution( Collection<IQuery> runningQueries, Collection<IQuery> stoppingQueries, ICost costEstimation ) {
		this.runningQueries = runningQueries;
		this.stoppingQueries = stoppingQueries;
		this.costEstimation = costEstimation;
	}
	
	@Override
	public Collection<IQuery> getRunningQueries() {
		return runningQueries;
	}

	@Override
	public Collection<IQuery> getStoppingQueries() {
		return stoppingQueries;
	}

	@Override
	public ICost getCostEstimation() {
		return costEstimation;
	}

}
