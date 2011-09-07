package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Schnittstelle der Aggregatoren, die die einzelnen Kosten der Operatoren zu
 * einem Kostenwert aggregieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IOperatorDetailCostAggregator {

	/**
	 * Aggregiert die Kosten der gegebenen Operatoren zu einem Kostenwert.
	 * 
	 * @param operatorEstimations
	 *            Kosten der einzelnen Operatoren
	 * @return Aggregierte Kosten aller Operatoren
	 */
	public AggregatedCost aggregate(Map<IPhysicalOperator, OperatorEstimation> operatorEstimations);
}
