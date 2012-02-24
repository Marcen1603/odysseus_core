package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

/**
 * Repräsentiert die Abschätzung eines Operators. Darunter fallen (angepasste)
 * Histogramme, Selektivität, Datenrate, durchschnittliche Länge der
 * Gültigkeitsintervalle, Prozessor- sowie Speicherkosten. Die Informationen
 * werden außerhalb des Konstruktors mittels getter angegeben. Die Klasse wird
 * hauptsächlich in den Operatorschätzern eingesetzt (s.
 * IOperatorEstimator-Klasse)
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorEstimation {

	private IPhysicalOperator operator;

	private Map<SDFAttribute, IHistogram> histograms = null;
	private IDataStream dataStream = null;

	private IOperatorDetailCost detailCost = null;
	private Double selectivity = null;

	/**
	 * Konstruktor. Erstellt eine neue {@link OperatorEstimation}-Instanz mit
	 * gegebenen physischen Operator.
	 * 
	 * @param estimatedOperator
	 *            Physischer Operator
	 */
	public OperatorEstimation(IPhysicalOperator estimatedOperator) {
		if (estimatedOperator == null)
			throw new IllegalArgumentException("estimatedOperator is null");

		this.operator = estimatedOperator;
	}

	/**
	 * Liefert alle (angepassten) Histogramme in der Abschätzung des Operators.
	 * 
	 * @return Liste der (angepassten) Histogramme
	 */
	public Map<SDFAttribute, IHistogram> getHistograms() {
		return histograms;
	}

	/**
	 * Setzt die Liste der (angepassten) Histogramme
	 * 
	 * @param outputHistograms
	 *            Neue Liste der (angepassten) Histogramme
	 */
	public void setHistograms(Map<SDFAttribute, IHistogram> outputHistograms) {
		this.histograms = outputHistograms;
	}

	/**
	 * Liefert die Charakteristiken des Ausgabestroms des Operators.
	 * 
	 * @return Charakteristiken des Ausgabestroms des Operators
	 */
	public IDataStream getDataStream() {
		return dataStream;
	}

	/**
	 * Setzt die Charakteristiken des Ausgabestroms des Operators
	 * 
	 * @param dataStream
	 *            Neue Charakteristiken des Ausgabestroms des Operators
	 */
	public void setDataStream(IDataStream dataStream) {
		this.dataStream = dataStream;
	}

	/**
	 * Liefert die Kosten (Speicher, Prozessor) des Operators
	 * 
	 * @return Operatorkosten (Speicher, Prozessor)
	 */
	public IOperatorDetailCost getDetailCost() {
		return detailCost;
	}

	/**
	 * Setzt die Operatorkosten auf den gegebenen Wert
	 * 
	 * @param detailCost
	 *            Neue Operatorkosten
	 */
	public void setDetailCost(IOperatorDetailCost detailCost) {
		this.detailCost = detailCost;
	}

	/**
	 * Liefert die Selektivität des Operators
	 * 
	 * @return Selektivität
	 */
	public Double getSelectivity() {
		return selectivity;
	}

	/**
	 * Setzt die Selektivität des Operators auf den gegebenen Wert
	 * 
	 * @param selectivity
	 *            Neue Selektivität
	 */
	public void setSelectivity(Double selectivity) {
		this.selectivity = selectivity;
	}

	/**
	 * Liefert den Operator dieser Abschätzung
	 * 
	 * @return Physischer Operator
	 */
	public IPhysicalOperator getOperator() {
		return operator;
	}

	/**
	 * Gibt <code>true</code> zurück, wenn alle Informationen gefülllt sind,
	 * sonst <code>false</code>.
	 * 
	 * @return <code>true</code>, wenn alle Informationen gefülllt sind, sonst
	 *         <code>false</code>.
	 */
	public boolean check() {
		if (getSelectivity() == null)
			return false;
		if (getOperator() == null)
			return false;
		if (getDataStream() == null)
			return false;
		if (getHistograms() == null)
			return false;
		if (getDataStream() == null)
			return false;
		return true;
	}
}
