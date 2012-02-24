package de.uniol.inf.is.odysseus.core.server.costmodel;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Repräsentiert ein Kostenmodell. Mit dieser lassen sich zu gegebenen
 * Operatormengen Kostenschätzungen durchführen.
 * 
 * @author Timo Michelsen
 * 
 */
public interface ICostModel {

	/**
	 * Führt eine Kostenschätzung mit den gegebenen physischen Operatoren durch
	 * und liefert das Ergebnis als {@link ICost} zurück.
	 * 
	 * @param operators
	 *            Liste der physischen Operatoren
	 * @param onUpdate
	 *            Gibt an, ob eine Aktualisierung der Kostenschätzung
	 *            durchgeführt wird, oder nicht. 
	 * @return Kostenschätzung
	 */
	public ICost estimateCost(List<IPhysicalOperator> operators, boolean onUpdate);

	/**
	 * Liefert die Maximalekosten
	 * 
	 * @return Maximalkosten
	 */
	public ICost getMaximumCost();

	/**
	 * Liefert eine Instanz der Kosten des Kostenmodells mit 0.
	 * 
	 * @return Null-Kosten
	 */
	public ICost getZeroCost();

}
