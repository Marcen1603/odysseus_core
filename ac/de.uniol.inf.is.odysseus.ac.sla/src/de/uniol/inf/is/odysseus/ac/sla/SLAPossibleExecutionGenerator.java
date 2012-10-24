package de.uniol.inf.is.odysseus.ac.sla;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class SLAPossibleExecutionGenerator implements
		IPossibleExecutionGenerator {

	@Override
	public List<IPossibleExecution> getPossibleExecutions(
			IAdmissionControl sender, Map<IPhysicalQuery, ICost> queryCosts,
			ICost maxCost) {
		// TODO Auto-generated method stub
		return null;
	}

}
