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
