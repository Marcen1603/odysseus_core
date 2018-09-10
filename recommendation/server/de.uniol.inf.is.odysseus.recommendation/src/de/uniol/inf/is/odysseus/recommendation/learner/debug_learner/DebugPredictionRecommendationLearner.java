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
package de.uniol.inf.is.odysseus.recommendation.learner.debug_learner;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.AbstractTupleBasedRecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.debug_rating_predictor.DebugRatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.debug_rating_predictor.DebugRatingPredictor.UserItemRating;

/**
 * @author Cornelius Ludmann
 *
 */
public class DebugPredictionRecommendationLearner<T extends Tuple<M>, M extends IMetaAttribute, U, I>
extends AbstractTupleBasedRecommendationLearner<T, M, U, I, Double>
implements RecommendationLearner<T, M, U, I, Double> {

	/**
	 * @param inputschema
	 * @param userAttribute
	 * @param itemAttribute
	 * @param ratingAttribute
	 */
	public DebugPredictionRecommendationLearner(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute) {
		super(inputschema, userAttribute, itemAttribute, ratingAttribute);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#trainModel()
	 */
	@Override
	public void trainModel() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getModel(boolean)
	 */
	@Override
	public RatingPredictor<T, M, U, I, Double> getModel(final boolean train) {
		final Set<UserItemRating<U, I>> tuples = new LinkedHashSet<>();
		for (final T tuple : this.learningData) {
			tuples.add(new UserItemRating<U, I>(getUserInTuple(tuple),
					getItemInTuple(tuple), getRatingInTuple(tuple)));
		}
		return new DebugRatingPredictor<>(ImmutableSet.copyOf(tuples));
	}

}
