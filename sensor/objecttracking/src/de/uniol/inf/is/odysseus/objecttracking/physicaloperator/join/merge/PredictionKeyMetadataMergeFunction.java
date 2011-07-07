/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge;

import de.uniol.inf.is.odysseus.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * This MergeFunction merges metadata according to the prediction based stream
 * processing. The metadata must contain a TimeInterval, a Probability and a
 * Prediction- Function.
 * 
 * @author Andre Bolles
 * 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class PredictionKeyMetadataMergeFunction<M extends IPredictionFunctionKey<IPredicate>>
		implements IInlineMetadataMergeFunction<M> {

	public PredictionKeyMetadataMergeFunction() {
	}

	public PredictionKeyMetadataMergeFunction(
			PredictionKeyMetadataMergeFunction<M> original) {
		super();
	}

	@Override
	public PredictionKeyMetadataMergeFunction<M> clone() {
		return new PredictionKeyMetadataMergeFunction<M>(this);
	}

	/**
	 * This method returns a new AndPredicate, that has the prediction function
	 * key from the left as left predicate and the key from the right as right
	 * predicate.
	 */
	@Override
	public void mergeInto(M res, M left, M right) {
		IPredicate newPred = ComplexPredicateHelper.createAndPredicate(
				left.getPredictionFunctionKey(),
				right.getPredictionFunctionKey());

		res.setPredictionFunctionKey(newPred);
	}

}
