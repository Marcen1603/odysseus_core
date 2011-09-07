package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Repräsentiert die Kosten eines einzelnen Operators, bestehend aus
 * Speicher- und Prozessorkosten.
 * 
 * @author Timo Michelsen
 *
 */
public interface IOperatorDetailCost {

	/**
	 * Physischer Operator, dessen Kosten hier beschrieben sind.
	 * 
	 * @return Physischer Operator
	 */
	public IPhysicalOperator getOperator();
	
	/**
	 * Liefert die Speicherkosten des Operators zurück.
	 * 
	 * @return Speicherkosten des Operators
	 */
	public double getMemoryCost();
	
	/**
	 * Liefert die Prozessorkosten des Operators zurück.
	 * 
	 * @return Prozessorkosten des Operators
	 */
	public double getProcessorCost();
	
}
