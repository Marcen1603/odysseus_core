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

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Repräsentiert die Kosten des Kostenmodells nach Operatoreigenschaften.
 * Darunter fallen Speicher- sowie Prozessorkosten.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorCost<T> implements ICost<T> {

	private Map<T, OperatorEstimation<T>> estimations = null;
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
	public OperatorCost(Map<T, OperatorEstimation<T>> estimations, double memCost, double cpuCost) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.estimations = estimations;
	}

	/**
	 * Interner Konstruktor. Erstellt eine neue {@link OperatorCost}-Instanz mit
	 * gegebenen Speicher- und Prozessorkosten. Abschätzungen der Operatoren
	 * sind nicht verfügbar. Dieser Konstruktor wird hauptsächlich zum
	 * Kopieren von Instanzen genutzt.
	 * 
	 * @param memCost
	 *            Aggregierte Speicherkosten
	 * @param cpuCost
	 *            Aggregierte Prozessorkosten
	 */
	OperatorCost(double memCost, double cpuCost) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.estimations = Maps.newHashMap();
	}

	/**
	 * Liefert die Zuordnung zwischen Operator und Abschätzung.
	 * 
	 * @return Zuordnung zwischen Operator und Abschätzung
	 */
	public Map<T, OperatorEstimation<T>> getOperatorEstimations() {
		return estimations;
	}
	
	public OperatorEstimation<T> getOperatorEstimation(T operator) {
		return estimations.get(operator);
	}

	@Override
	public int compareTo(ICost<T> o) {
		OperatorCost<T> cost = (OperatorCost<T>) o;

		if (memCost > cost.memCost || cpuCost > cost.cpuCost)
			return 1;
		if (memCost < cost.memCost && cpuCost < cost.cpuCost)
			return -1;

		return 0;
	}

	@Override
	public ICost<T> merge(ICost<T> otherCost) {
		if (otherCost == null)
			return new OperatorCost<T>(memCost, cpuCost);

		OperatorCost<T> cost = (OperatorCost<T>) otherCost;

		return new OperatorCost<T>(memCost + cost.memCost, cpuCost + cost.cpuCost);
	}

	@Override
	public ICost<T> substract(ICost<T> otherCost) {
		if (otherCost == null)
			return new OperatorCost<T>(memCost, cpuCost);

		OperatorCost<T> cost = (OperatorCost<T>) otherCost;

		return new OperatorCost<T>(memCost - cost.memCost, cpuCost - cost.cpuCost);
	}

	@Override
	public String toString() {
		return String.format("m = %-10.3f MB; c = %-10.6f", memCost / 1024 / 1024, cpuCost);
	}

	@Override
	public Collection<T> getOperators() {
		return estimations.keySet();
	}

	@Override
	public ICost<T> getCostOfOperator(T operator) {
		OperatorEstimation<? extends T> est = estimations.get(operator);
		if (est == null) {
			return new OperatorCost<T>(0, 0);
		}

		return new OperatorCost<T>(est.getDetailCost().getMemoryCost(), est.getDetailCost().getProcessorCost());
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

	@Override
	public ICost<T> fraction(double factor) {
		return new OperatorCost<T>(memCost * factor, cpuCost * factor);
	}
}
