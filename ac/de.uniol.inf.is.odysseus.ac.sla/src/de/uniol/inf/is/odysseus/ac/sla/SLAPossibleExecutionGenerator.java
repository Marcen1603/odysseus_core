package de.uniol.inf.is.odysseus.ac.sla;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.ac.StandardPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;

public class SLAPossibleExecutionGenerator implements
		IPossibleExecutionGenerator {

	@Override
	public List<IPossibleExecution> getPossibleExecutions(
			IAdmissionControl sender, Map<IPhysicalQuery, ICost> queryCosts,
			ICost maxCost) {

		List<IPossibleExecution> result = new ArrayList<>();
		IPhysicalQuery mostExpensiveQuery = null;
		double maxPenalty = -1.0;
		ICost maxQueryCost = null;

		for (IPhysicalQuery q : queryCosts.keySet()) {
			SLA sla = (SLA) q.getParameter(SLA.class.getName());

			if (sla != null) {
				double killPenalty = sla.getQueryKillPenalty().getCost();
				
				if (killPenalty == maxPenalty) {
					ICost queryCost = queryCosts.get(q);
					int compare = queryCost.compareTo(maxQueryCost);
					if (compare > 0) {
						maxQueryCost = queryCost;
						mostExpensiveQuery = q;
					}
				} else if (killPenalty > maxPenalty) {
					maxPenalty = killPenalty;
					mostExpensiveQuery = q;
					maxQueryCost = queryCosts.get(q);
				}
			}
		}
		
		IPossibleExecution possibleExec = StandardPossibleExecution.stopOneQuery(queryCosts, mostExpensiveQuery);
		result.add(possibleExec);
		
		return result;
	}

}
