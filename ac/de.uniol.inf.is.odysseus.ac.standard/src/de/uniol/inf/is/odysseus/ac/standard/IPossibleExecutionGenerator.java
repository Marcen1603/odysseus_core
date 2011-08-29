package de.uniol.inf.is.odysseus.ac.standard;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IPossibleExecutionGenerator {

	public List<IPossibleExecution> getPossibleExecutions( IAdmissionControl sender, Map<IQuery, ICost> queryCosts, ICost maxCost );
}
