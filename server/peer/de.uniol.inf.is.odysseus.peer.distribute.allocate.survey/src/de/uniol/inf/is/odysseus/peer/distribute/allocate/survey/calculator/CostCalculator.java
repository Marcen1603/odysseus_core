package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.calculator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;

public interface CostCalculator {
	/**
	 * Bestimmt die Ausführungskosten der einzelnen Operatoren.
	 * 
	 * @param plan
	 * @param transCfgName
	 * @param copyPlan
	 *            Bestimmt, dass der übergebene Plan nicht verändert werden
	 *            darf.
	 * @return
	 */
	public Map<String, CostSummary> calcCostsProOperator(ILogicalQuery plan, String transCfgName, boolean discardPlan);

	/**
	 * Bestimmt die Ausführungskosten für den gesamten Plan.
	 * 
	 * @param plan
	 * @param transCfgName
	 * @return
	 */
	public CostSummary calcCostsForPlan(ILogicalQuery query, String transCfgName);

	public CostSummary calcBearableCostsInPercentage(CostSummary cost);

	public double calcBid(ILogicalOperator plan, CostSummary cost);

	public double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts, boolean respectSourceAvailibility);
}
