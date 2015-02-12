/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.learner;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner;
import de.uniol.inf.is.odysseus.machine_learning.learner.Learner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;

/**
 * @author Cornelius Ludmann
 *
 * @see {@link Learner}, {@link AbstractLearner}
 */
public abstract class AbstractTupleBasedRecommendationLearner<T extends Tuple<M>, M extends IMetaAttribute, U, I, P>
		extends AbstractLearner<T, M, P> implements
		RecommendationLearner<T, M, U, I, P> {

	private final int userAttributeIndex, itemAttributeIndex,
			ratingAttributeIndex;

	/**
	 * @param inputschema
	 *            The schema of the input tuples.
	 * @param userAttribute
	 *            The attribute that denotes the user id.
	 * @param itemAttribute
	 *            The attribute that denotes the item id.
	 * @param ratingAttribute
	 *            The attribute that denotes the rating.
	 */
	public AbstractTupleBasedRecommendationLearner(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute) {
		this.userAttributeIndex = inputschema.indexOf(userAttribute);
		this.itemAttributeIndex = inputschema.indexOf(itemAttribute);
		this.ratingAttributeIndex = inputschema.indexOf(ratingAttribute);

		if (this.userAttributeIndex == -1) {
			throw new IllegalArgumentException("User attribute not found.");
		}
		if (this.itemAttributeIndex == -1) {
			throw new IllegalArgumentException("Item attribute not found.");
		}
		if (this.ratingAttributeIndex == -1) {
			throw new IllegalArgumentException("Rating attribute not found.");
		}
	}

	/**
	 * Returns the user attribute in the passed tuple.
	 *
	 * @param tuple
	 *            The tuple.
	 * @return The user.
	 */
	protected U getUserInTuple(final T tuple) {
		return tuple.getAttribute(this.userAttributeIndex);
	}

	/**
	 * Returns the item attribute in the passed tuple.
	 *
	 * @param tuple
	 *            The tuple.
	 * @return The item.
	 */
	protected I getItemInTuple(final T tuple) {
		return tuple.getAttribute(this.itemAttributeIndex);
	}

	/**
	 * Returns the rating attribute in the passed tuple.
	 *
	 * @param tuple
	 *            The tuple.
	 * @return The rating.
	 */
	protected P getRatingInTuple(final T tuple) {
		return tuple.getAttribute(this.ratingAttributeIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#getModel
	 * ()
	 */
	@Override
	public RatingPredictor<T, M, U, I, P> getModel() {
		return getModel(true);
	}
}
