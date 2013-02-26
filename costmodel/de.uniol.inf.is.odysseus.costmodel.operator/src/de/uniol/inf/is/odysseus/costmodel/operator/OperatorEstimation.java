/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Map;

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
public class OperatorEstimation<T> {

	private T operator;

	private Map<SDFAttribute, IHistogram> histograms = null;
	private IDataStream<T> dataStream = null;

	private IOperatorDetailCost<T> detailCost = null;
	private Double selectivity = null;

	/**
	 * Konstruktor. Erstellt eine neue {@link OperatorEstimation}-Instanz mit
	 * gegebenen physischen Operator.
	 * 
	 * @param estimatedOperator
	 *            Physischer Operator
	 */
	public OperatorEstimation(T estimatedOperator) {
		if (estimatedOperator == null) {
			throw new IllegalArgumentException("estimatedOperator is null");
		}
		
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
	public IDataStream<T> getDataStream() {
		return dataStream;
	}

	/**
	 * Setzt die Charakteristiken des Ausgabestroms des Operators
	 * 
	 * @param dataStream
	 *            Neue Charakteristiken des Ausgabestroms des Operators
	 */
	public void setDataStream(IDataStream<T> dataStream) {
		this.dataStream = dataStream;
	}

	/**
	 * Liefert die Kosten (Speicher, Prozessor) des Operators
	 * 
	 * @return Operatorkosten (Speicher, Prozessor)
	 */
	public IOperatorDetailCost<T> getDetailCost() {
		return detailCost;
	}

	/**
	 * Setzt die Operatorkosten auf den gegebenen Wert
	 * 
	 * @param detailCost
	 *            Neue Operatorkosten
	 */
	public void setDetailCost(IOperatorDetailCost<T> detailCost) {
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
	public T getOperator() {
		return operator;
	}
}
