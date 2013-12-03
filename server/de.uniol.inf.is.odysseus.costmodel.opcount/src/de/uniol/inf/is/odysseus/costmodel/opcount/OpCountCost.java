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
package de.uniol.inf.is.odysseus.costmodel.opcount;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Kosten im Kostenmodell nach Operatorzahl. Die Kosten werden durch die Zahl
 * der Operatoren bestimmt.
 * 
 * @author Timo Michelsen
 * 
 */
public class OpCountCost<T> implements ICost<T> {

	private Collection<T> operators;
	private final double opCount;

	/**
	 * Konstruktor. Erstellt eine neue {@link OpCountCost}-Instanz mit gegebenen
	 * Operatorliste. Die Kosten lassen sich aus der Größe der Liste ableiten.
	 * 
	 * @param operators
	 *            Liste der Operatoren, die in der Kostenschätzung im
	 *            Kostenmodell berücksichtigt wurden.
	 */
	public OpCountCost(Collection<T> operators) {
		this.opCount = operators.size();
		this.operators = operators;
	}

	/**
	 * Konstruktor. Erstellt eine neue {@link OpCountCost}-Instanz mit gegeben
	 * Kosten. Dieser Konstruktor wird hauptsächlich beim Kopieren von Instanzen genutzt.
	 * 
	 * @param opCount
	 */
	OpCountCost(double opCount) {
		//this.opCount = operators.size();
		this.opCount = opCount;
		operators = new ArrayList<T>();
	}

	@Override
	public int compareTo(ICost<T> o) {
		if (!(o instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);

		OpCountCost<T> cost = (OpCountCost<T>) o;
		if (opCount < cost.opCount)
			return -1;
		if (opCount == cost.opCount)
			return 0;
		return 1;
	}

	@Override
	public OpCountCost<T> merge(ICost<T> otherCost) {
		if (otherCost == null)
			return new OpCountCost<T>(operators);

		if (!(otherCost instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);

		OpCountCost<T> cost = (OpCountCost<T>) otherCost;

		return new OpCountCost<T>(opCount + cost.opCount);
	}

	@Override
	public OpCountCost<T> substract(ICost<T> otherCost) {
		if (otherCost == null)
			return new OpCountCost<T>(opCount);

		if (!(otherCost instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);

		OpCountCost<T> cost = (OpCountCost<T>) otherCost;

		return new OpCountCost<T>(opCount - cost.opCount);
	}

	@Override
	public Collection<T> getOperators() {
		return operators;
	}

	@Override
	public ICost<T> getCostOfOperator(T operator) {
		return new OpCountCost<T>(1.0 / opCount);
	}

	@Override
	public ICost<T> fraction(double factor) {
		return new OpCountCost<T>(opCount * factor);
	}

	public double getOpCount() {
		return opCount;
	}
	
	@Override
	public String toString() {
		return "" + this.opCount;
	}

}
