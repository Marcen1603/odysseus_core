package de.uniol.inf.is.odysseus.costmodel.querycount;

import java.util.Collection;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Kosten im Kostenmodell nach Anfragezahl
 * 
 * @author Timo Michelsen
 * 
 */
public class QueryCountCost implements ICost {

	private double value;
	private Collection<IPhysicalOperator> operators;

	/**
	 * Konstruktor. Erstellt eine neue {@link QueryCountCost}-Instanz mit den
	 * gegebenen physischen Operatoren.
	 * 
	 * @param operators
	 *            Physische Operatoren
	 */
	public QueryCountCost(Collection<IPhysicalOperator> operators) {
		value = 1;
		this.operators = operators;
	}

	/**
	 * Konstruktor. Erstellt eine neue {@link QueryCountCost}-Instanz mit
	 * gegebenen Kostenwert. Wird haupsächlich beim Kopieren von Instanzen
	 * genutzt.
	 * 
	 * @param value Kostenwert
	 */
	QueryCountCost(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "QueryCount = " + String.valueOf(value);
	}

	@Override
	public int compareTo(ICost o) {
		QueryCountCost cost = (QueryCountCost) o;
		if (value < cost.value)
			return -1;
		if (value == cost.value)
			return 0;
		return 1;
	}

	@Override
	public ICost merge(ICost otherCost) {
		if (otherCost == null)
			return new QueryCountCost(value);

		QueryCountCost cost = (QueryCountCost) otherCost;
		return new QueryCountCost(value + cost.value);
	}

	@Override
	public ICost substract(ICost otherCost) {
		if (otherCost == null)
			return new QueryCountCost(value);

		QueryCountCost cost = (QueryCountCost) otherCost;
		return new QueryCountCost(value - cost.value);
	}

	@Override
	public Collection<IPhysicalOperator> getOperators() {
		return operators;
	}

	@Override
	public ICost getCostOfOperator(IPhysicalOperator operator) {
		return new QueryCountCost(value / operators.size());
	}

}
