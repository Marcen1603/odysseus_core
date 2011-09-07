package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Repräsentiert die generellen Kosten im Admission Control. 
 * Die Implementierung ist abhängig vom Kostenmodell.
 * 
 * @author Timo Michelsen
 *
 */
public interface ICost extends Comparable<ICost>{

	/**
	 * Liefert alle Operatoren, die durch die Kosten
	 * berücksichtigt wurden.
	 * 
	 * @return Liste der beteiltigen Operatoren
	 */
	public Collection<IPhysicalOperator> getOperators();
	
	/**
	 * Liefert die Kosten zu einem spezifischen Operator. 
	 * Bei einigen Kostenmodellen ist die Trennung auf Operatorebene
	 * nicht möglich. Darauf sollte geachtet werden.
	 * 
	 * @param operator Physischer Operator, dessen Kosten zurückgegeben werden sollen.
	 * @return Kosten zum gegebenen physischen Operator
	 */
	public ICost getCostOfOperator(IPhysicalOperator operator);
	
	/**
	 * Fasst diese Kosten mit den gegebenen Kosten zusammen und liefert
	 * das Ergebnis als neue Instanz.
	 * 
	 * @param otherCost Andere Kosten
	 * @return Zusammengefasste Kosten
	 */
	public ICost merge( ICost otherCost );
	
	/**
	 * Subtrahiert diese Kosten mit den gegebenen Kosten und liefert
	 * das Ergebnis als neue Instanz.
	 * 
	 * @param otherCost Andere Kosten
	 * @return Subtraierte Kosten
	 */
	public ICost substract( ICost otherCost );
}
