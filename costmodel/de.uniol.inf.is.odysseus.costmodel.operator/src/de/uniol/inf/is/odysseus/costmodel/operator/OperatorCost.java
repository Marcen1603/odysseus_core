package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Repräsentiert die Kosten des Kostenmodells nach Operatoreigenschaften.
 * Darunter fallen Speicher- sowie Prozessorkosten.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorCost implements ICost {

	private Map<IPhysicalOperator, OperatorEstimation> estimations = null;
	private double memCost;
	private double cpuCost;

	/**
	 * Konstruktor. Erstellt eine neue {@link OperatorCost}-Instanz mit
	 * gegebenen Abschätzungen der Operatoren sowie den aggregierten Speicher-
	 * und Prozessorkosten.
	 * 
	 * @param estimations
	 *            Abschätzungen aller beteiltigen Operatoren
	 * @param memCost
	 *            Aggregierte Speicherkosten
	 * @param cpuCost
	 *            Aggregierte Prozessorkosten
	 */
	public OperatorCost(Map<IPhysicalOperator, OperatorEstimation> estimations, double memCost, double cpuCost) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.estimations = estimations;
	}

	/**
	 * Interner Konstruktor. Erstellt eine neue {@link OperatorCost}-Instanz mit
	 * gegebenen Speicher- und Prozessorkosten. Abschätzungen der Operatoren
	 * sind nicht verfügbar. Dieser Konstruktor wird hauptsächlich zum Kopieren
	 * von Instanzen genutzt.
	 * 
	 * @param memCost
	 *            Aggregierte Speicherkosten
	 * @param cpuCost
	 *            Aggregierte Prozessorkosten
	 */
	OperatorCost(double memCost, double cpuCost) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.estimations = new HashMap<IPhysicalOperator, OperatorEstimation>();
	}

	/**
	 * Liefert die Zuordnung zwischen Operator und Abschätzung.
	 * 
	 * @return Zuordnung zwischen Operator und Abschätzung
	 */
	public Map<IPhysicalOperator, OperatorEstimation> getOperatorEstimations() {
		return estimations;
	}

	@Override
	public int compareTo(ICost o) {
		OperatorCost cost = (OperatorCost) o;

		if (memCost > cost.memCost || cpuCost > cost.cpuCost)
			return 1;
		if (memCost < cost.memCost && cpuCost < cost.cpuCost)
			return -1;

		return 0;
	}

	@Override
	public ICost merge(ICost otherCost) {
		if (otherCost == null)
			return new OperatorCost(memCost, cpuCost);

		OperatorCost cost = (OperatorCost) otherCost;

		return new OperatorCost(memCost + cost.memCost, cpuCost + cost.cpuCost);
	}

	@Override
	public ICost substract(ICost otherCost) {
		if (otherCost == null)
			return new OperatorCost(memCost, cpuCost);

		OperatorCost cost = (OperatorCost) otherCost;

		return new OperatorCost(memCost - cost.memCost, cpuCost - cost.cpuCost);
	}

	@Override
	public String toString() {
		return String.format("%-10.6f; %-10.6f", memCost, cpuCost);
	}

	@Override
	public Collection<IPhysicalOperator> getOperators() {
		return estimations.keySet();
	}

	@Override
	public ICost getCostOfOperator(IPhysicalOperator operator) {
		OperatorEstimation est = estimations.get(operator);
		if (est == null)
			return new OperatorCost(0, 0);

		return new OperatorCost(est.getDetailCost().getMemoryCost(), est.getDetailCost().getProcessorCost());
	}

	/**
	 * Liefert die Speicherkosten zurück.
	 * 
	 * @return Speicherkosten
	 */
	public double getMemCost() {
		return memCost;
	}

	/**
	 * Liefert die Prozessorkosten zurück
	 * 
	 * @return Prozessorkosten
	 */
	public double getCpuCost() {
		return cpuCost;
	}
}
