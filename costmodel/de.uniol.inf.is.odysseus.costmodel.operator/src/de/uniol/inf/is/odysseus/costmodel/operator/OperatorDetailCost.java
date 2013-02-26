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
 * Standardimplementierung der Operatorkosten.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorDetailCost<T> implements IOperatorDetailCost<T> {

	private final T operator;
	private final double memCost;
	private final double cpuCost;

	/**
	 * Konstruktor. Erstellt eine neue {@link OperatorDetailCost}-Instanz mit
	 * gegegeben Operator, Speicher- und Prozessorkosten.
	 * 
	 * @param operator
	 *            Physischer Operator, dessen Kosten hier gespeichert werden
	 *            sollen
	 * @param memCost
	 *            Speicherkosten des Operators
	 * @param cpuCost
	 *            Prozessorkosten des Operators
	 */
	public OperatorDetailCost(T operator, double memCost, double cpuCost) {
		this.operator = operator;
		this.memCost = memCost;
		this.cpuCost = cpuCost;
	}

	@Override
	public T getOperator() {
		return operator;
	}

	@Override
	public double getMemoryCost() {
		return memCost;
	}

	@Override
	public double getProcessorCost() {
		return cpuCost;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ mem = ").append(memCost).append(", cpu = ").append(cpuCost).append(" }");
		return sb.toString();
	}
}
