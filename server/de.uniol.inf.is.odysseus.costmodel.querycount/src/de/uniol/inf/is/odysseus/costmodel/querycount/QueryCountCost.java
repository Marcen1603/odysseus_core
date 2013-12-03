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
package de.uniol.inf.is.odysseus.costmodel.querycount;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Kosten im Kostenmodell nach Anfragezahl
 * 
 * @author Timo Michelsen
 * 
 */
public class QueryCountCost<T> implements ICost<T> {

	private double value;
	private Collection<T> operators;

	/**
	 * Konstruktor. Erstellt eine neue {@link QueryCountCost}-Instanz mit den
	 * gegebenen physischen Operatoren.
	 * 
	 * @param operators
	 *            Physische Operatoren
	 */
	public QueryCountCost(Collection<T> operators) {
		value = 1;
		this.operators = operators;
	}

	/**
	 * Konstruktor. Erstellt eine neue {@link QueryCountCost}-Instanz mit
	 * gegebenen Kostenwert. Wird haupsächlich beim Kopieren von Instanzen
	 * genutzt.
	 * 
	 * @param value Kostenwert
	 */
	QueryCountCost(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "QueryCount = " + String.valueOf(value);
	}

	@Override
	public int compareTo(ICost<T> o) {
		QueryCountCost<T> cost = (QueryCountCost<T>) o;
		if (value < cost.value)
			return -1;
		if (value == cost.value)
			return 0;
		return 1;
	}

	@Override
	public ICost<T> merge(ICost<T> otherCost) {
		if (otherCost == null)
			return new QueryCountCost<T>(value);

		QueryCountCost<T> cost = (QueryCountCost<T>) otherCost;
		return new QueryCountCost<T>(value + cost.value);
	}

	@Override
	public ICost<T> substract(ICost<T> otherCost) {
		if (otherCost == null)
			return new QueryCountCost<T>(value);

		QueryCountCost<T> cost = (QueryCountCost<T>) otherCost;
		return new QueryCountCost<T>(value - cost.value);
	}

	@Override
	public Collection<T> getOperators() {
		return operators;
	}

	@Override
	public ICost<T> getCostOfOperator(T operator) {
		return new QueryCountCost<T>(value / operators.size());
	}

	@Override
	public ICost<T> fraction(double factor) {
		return new QueryCountCost<T>(value * factor);
	}

}
