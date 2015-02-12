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
package de.uniol.inf.is.odysseus.recommendation.learner.baseline_learner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.AbstractTupleBasedRecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.ConstantValueRatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.baseline_rating_predictor.BaselineRatingPredictor;

/**
 * This learner creates an {@link BaselineRatingPredictor}. It calculates the
 * average rating over all data as well as the average rating of each user and
 * each item.
 *
 * @author Cornelius Ludmann
 */
public class BaselinePredictionRecommendationLearner<T extends Tuple<M>, M extends IMetaAttribute, U, I>
		extends AbstractTupleBasedRecommendationLearner<T, M, U, I, Double> {

	public static final String NAME = "BaselinePrediction";
	private static Logger logger = LoggerFactory
			.getLogger(BaselinePredictionRecommendationLearner.class);

	private RatingPredictor<T, M, U, I, Double> model = new ConstantValueRatingPredictor<T, M, U, I, Double>(
			0.0);

	/**
	 * @param inputschema
	 * @param userAttribute
	 * @param itemAttribute
	 * @param ratingAttribute
	 */
	public BaselinePredictionRecommendationLearner(final SDFSchema inputschema,
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

		if (this.learningData == null || this.learningData.size() == 0) {
			this.model = new ConstantValueRatingPredictor<T, M, U, I, Double>(
					0.0);
			return;
		}

		synchronized (this.learningData) {

			double avgRating = 0;
			final Map<U, Double> avgUserRatings = new HashMap<U, Double>();
			final Map<I, Double> avgItemRatings = new HashMap<I, Double>();
			double minRating = Double.MAX_VALUE;
			double maxRating = Double.MIN_VALUE;

			int noRatings = 0;
			final Map<U, Integer> noUserRatings = new HashMap<U, Integer>();
			final Map<I, Integer> noItemRatings = new HashMap<I, Integer>();

			for (final T tuple : this.learningData) {
				final U user = getUserInTuple(tuple);
				final I item = getItemInTuple(tuple);
				final double rating = getRatingInTuple(tuple);

				++noRatings;
				Integer nUR = noUserRatings.get(user);
				nUR = (nUR == null ? 1 : nUR + 1);
				noUserRatings.put(user, nUR);
				Integer nIR = noItemRatings.get(item);
				nIR = (nIR == null ? 1 : nIR + 1);
				noItemRatings.put(item, nIR);

				avgRating += (rating - avgRating) / noRatings;

				Double aUR = avgUserRatings.get(user);
				aUR = (aUR == null ? 0 : aUR);
				avgUserRatings.put(user, aUR + ((rating - aUR) / nUR));

				Double aIR = avgItemRatings.get(item);
				aIR = (aIR == null ? 0 : aIR);
				avgItemRatings.put(item, aIR + ((rating - aIR) / nIR));

				minRating = Math.min(minRating, rating);
				maxRating = Math.max(maxRating, rating);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("LEARN: no of tuples: " + this.learningData.size()
						+ "  no of items: " + avgItemRatings.keySet().size());
			}

			this.model = new BaselineRatingPredictor<T, M, U, I>(avgRating,
					avgUserRatings, avgItemRatings, minRating, maxRating);
			this.isModelUpToDate = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#getModel(boolean
	 * )
	 */
	@Override
	public RatingPredictor<T, M, U, I, Double> getModel(final boolean train) {
		if (!this.isModelUpToDate) {
			trainModel();
		}
		return this.model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#getName
	 * ()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaselinePredictionRecommendationLearner [model=" + this.model
				+ "]";
	}

}
