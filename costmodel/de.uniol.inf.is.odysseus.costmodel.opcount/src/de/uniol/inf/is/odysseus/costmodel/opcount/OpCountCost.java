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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Kosten im Kostenmodell nach Operatorzahl. Die Kosten werden durch die Zahl
 * der Operatoren bestimmt.
 * 
 * @author Timo Michelsen
 * 
 */
public class OpCountCost implements ICost {

	private Collection<IPhysicalOperator> operators;
	private final double opCount;

	/**
	 * Konstruktor. Erstellt eine neue {@link OpCountCost}-Instanz mit gegebenen
	 * Operatorliste. Die Kosten lassen sich aus der Größe der Liste ableiten.
	 * 
	 * @param operators
	 *            Liste der Operatoren, die in der Kostenschätzung im
	 *            Kostenmodell berücksichtigt wurden.
	 */
	public OpCountCost(Collection<IPhysicalOperator> operators) {
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
		this.opCount = operators.size();
		operators = new ArrayList<IPhysicalOperator>();
	}

	@Override
	public int compareTo(ICost o) {
		if (!(o instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);

		OpCountCost cost = (OpCountCost) o;
		if (opCount < cost.opCount)
			return -1;
		if (opCount == cost.opCount)
			return 0;
		return 1;
	}

	@Override
	public OpCountCost merge(ICost otherCost) {
		if (otherCost == null)
			return new OpCountCost(operators);

		if (!(otherCost instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);

		OpCountCost cost = (OpCountCost) otherCost;

		return new OpCountCost(opCount + cost.opCount);
	}

	@Override
	public OpCountCost substract(ICost otherCost) {
		if (otherCost == null)
			return new OpCountCost(opCount);

		if (!(otherCost instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);

		OpCountCost cost = (OpCountCost) otherCost;

		return new OpCountCost(opCount - cost.opCount);
	}

	@Override
	public Collection<IPhysicalOperator> getOperators() {
		return operators;
	}

	@Override
	public ICost getCostOfOperator(IPhysicalOperator operator) {
		return new OpCountCost(1.0 / opCount);
	}

}
