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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Schnittstelle der Aggregatoren, die die einzelnen Kosten der Operatoren zu
 * einem Kostenwert aggregieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IOperatorDetailCostAggregator {

	/**
	 * Aggregiert die Kosten der gegebenen Operatoren zu einem Kostenwert.
	 * 
	 * @param operatorEstimations
	 *            Kosten der einzelnen Operatoren
	 * @return Aggregierte Kosten aller Operatoren
	 */
	public AggregatedCost aggregate(Map<IPhysicalOperator, OperatorEstimation> operatorEstimations);
}
