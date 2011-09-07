package de.uniol.inf.is.odysseus.costmodel.operator;

/**
 * Repräsentiert die aggregierten Kosten aller Operatorkosten.
 * 
 * @author Timo Michelsen
 *
 */
public final class AggregatedCost {

	private double cpuCost;
	private double memCost;
	
	/**
	 * Konstruktor. Erstellt eine neue {@link AggregatedCost}-Instanz
	 * mit gegebenen aggregireten Proessor- und Speicherkosten.
	 * 
	 * @param cpuCost Aggregierte Prozessorkosten
	 * @param memCost Aggregierte Speicherkosten
	 */
	public AggregatedCost( double cpuCost, double memCost ) {
		this.cpuCost = cpuCost;
		this.memCost = memCost;
	}

	/**
	 * Liefert die aggregierten Prozessorkosten zurück
	 * 
	 * @return Aggregierte Prozessorkosten
	 */
	public double getCpuCost() {
		return cpuCost;
	}

	/**
	 * Liefert die aggregireten Speicherkosten zurück
	 * 
	 * @return Aggregierte Speicherkosten
	 */
	public double getMemCost() {
		return memCost;
	}
	
	@Override
	public String toString() {
		return String.format("{ %-10.6f, %-10.6f}", memCost, cpuCost);
	}
}
