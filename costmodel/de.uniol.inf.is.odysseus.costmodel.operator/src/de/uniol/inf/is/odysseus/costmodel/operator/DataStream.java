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
