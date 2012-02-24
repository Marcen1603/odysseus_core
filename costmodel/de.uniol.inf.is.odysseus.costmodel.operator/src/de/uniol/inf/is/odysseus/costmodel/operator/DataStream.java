package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Standardimplementierung der Schnittstelle {@link IDataStream}.
 * 
 * @author Timo Michelsen
 * 
 */
public class DataStream implements IDataStream {

	private final IPhysicalOperator operator;
	private final double dataRate;
	private final double intervalLength;

	/**
	 * Konstruktor. Erstellt eine neue {@link DataStream}-Instanz mit gegebener
	 * Datenrate und durchschnittlicher Länge der Gültigkeitsintervalle.
	 * 
	 * @param operator
	 *            Operator, dessen Ausgabestrom hier beschrieben wird
	 * @param dataRate
	 *            Datenrate des Datenstroms
	 * @param intervalLength
	 *            Durchschnittliche Länge des Gültigkeitsintervalls
	 */
	public DataStream(IPhysicalOperator operator, double dataRate, double intervalLength) {
		this.operator = operator;
		this.dataRate = dataRate;
		this.intervalLength = intervalLength;
	}

	@Override
	public IPhysicalOperator getOperator() {
		return operator;
	}

	@Override
	public double getDataRate() {
		return dataRate;
	}

	@Override
	public double getIntervalLength() {
		return intervalLength;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ r = ");
		sb.append(dataRate);
		sb.append(", g = ");
		sb.append(intervalLength);
		sb.append(" }");
		return sb.toString();
	}

}
