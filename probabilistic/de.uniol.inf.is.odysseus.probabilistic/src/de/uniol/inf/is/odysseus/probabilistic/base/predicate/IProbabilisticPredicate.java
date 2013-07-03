/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticResult;

/**
 * Probabilistic predicate to perform evaluation of predicate boolean expressions.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public interface IProbabilisticPredicate extends IPredicate<ProbabilisticTuple<?>> {
	/**
	 * Initialize the predicate with the input schemas.
	 * 
	 * @param leftSchema
	 *            The {@link SDFSchema schema} from the left stream
	 * @param rightSchema
	 *            The {@link SDFSchema schema} from the right stream
	 */
	void init(SDFSchema leftSchema, SDFSchema rightSchema);

	/**
	 * Replace the current attribute with the new attribute.
	 * 
	 * @param curAttr
	 *            The current attribute
	 * @param newAttr
	 *            The new attribute
	 */
	void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr);

	/**
	 * Evaluates the predicate on the attributes from the two input tuples.
	 * 
	 * @param left
	 *            The left input tuple
	 * @param right
	 *            The right input tuple
	 * @return The probability and values that the attributes from the two input tuples holds the predicate
	 */
	ProbabilisticResult probabilisticEvaluate(ProbabilisticTuple<?> left, ProbabilisticTuple<?> right);

	/**
	 * Evaluates the predicate on the attributes from the input tuple.
	 * 
	 * @param input
	 *            The input tuple
	 * @return The probability that the attributes from the input tuple holds the predicate
	 */
	ProbabilisticResult probabilisticEvaluate(ProbabilisticTuple<?> input);

}
