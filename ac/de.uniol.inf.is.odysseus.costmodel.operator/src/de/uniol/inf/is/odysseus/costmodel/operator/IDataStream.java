package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Repäsentiert die Charakteristika eines Datenstroms.
 * Darunter fallen Datenrate sowie durchschnittliche Länge
 * der Gültigkeitsintervalle.
 * 
 * @author Timo Michelsen
 *
 */
public interface IDataStream {

	/**
	 * Liefert den Operator zurück, dessen Ausgabestrom in dieser
	 * Instanz charakterisiert wird.
	 * 
	 * @return Physischer Operator
	 */
	public IPhysicalOperator getOperator();
	
	/**
	 * Liefert die Datenrate
	 * 
	 * @return Datenrate
	 */
	public double getDataRate();
	
	/**
	 * Liefert die durchschnittliche Länge der Gültigkeitsintervalle
	 * 
	 * @return Durchschnittliche Länge der Gültigkeitsintervalle
	 */
	public double getIntervalLength();
	
}
