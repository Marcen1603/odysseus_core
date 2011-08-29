package de.uniol.inf.is.odysseus.ac;

import java.util.Collection;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IPossibleExecution {

	public Collection<IQuery> getRunningQueries();
	public Collection<IQuery> getStoppingQueries();
	public ICost getCostEstimation();
	
}
