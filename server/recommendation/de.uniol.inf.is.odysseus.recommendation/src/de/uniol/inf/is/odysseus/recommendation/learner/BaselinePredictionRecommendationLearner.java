/**
 * Copyright 2014 The Odysseus Team
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.recommender.BaselinePredictionRecommender;
import de.uniol.inf.is.odysseus.recommendation.recommender.Recommender;

/**
 * This learner creates an {@link BaselinePredictionRecommender}. It calculates
 * the average rating over all data as well as the average rating of each user
 * and each item.
 * 
 * @author Cornelius Ludmann
 */
public class BaselinePredictionRecommendationLearner<M extends ITimeInterval>
implements RecommendationLearner<M> {

	public static final String NAME = "BaselinePrediction";
	private static Logger logger = LoggerFactory
			.getLogger(BaselinePredictionRecommendationLearner.class);

	private int userAttributeIndex = -1;
	private int itemAttributeIndex = -1;
	private int ratingAttributeIndex = -1;

	/**
	 * Creates a new BaselinePredictionRecommendationLearner. Options are
	 * ignored.
	 * 
	 * @param inputschema
	 *            The schema of the input tuples.
	 * @param userAttribute
	 *            The attribute that denotes the user id.
	 * @param itemAttribute
	 *            The attribute that denotes the item id.
	 * @param ratingAttribute
	 *            The attribute that denotes the rating.
	 * @param options
	 *            The options for the learner (optional, can be null)
	 */
	public BaselinePredictionRecommendationLearner(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {
		this.userAttributeIndex = inputschema.indexOf(userAttribute);
		this.itemAttributeIndex = inputschema.indexOf(itemAttribute);
		this.ratingAttributeIndex = inputschema.indexOf(ratingAttribute);
		// ignore options
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #createRecommender(java.util.List)
	 */
	@Override
	public Recommender createRecommender(final List<Tuple<M>> tuples) {

		if (userAttributeIndex == -1 || itemAttributeIndex == -1
				|| ratingAttributeIndex == -1) {
			throw new IllegalStateException(
					"user, item, or rating attribute index is not set.");
		}

		if (tuples == null || tuples.size() == 0) {
			return null;
		}

		double avgRating = 0;
		final Map<Object, Double> avgUserRatings = new HashMap<Object, Double>();
		final Map<Object, Double> avgItemRatings = new HashMap<Object, Double>();
		List<Object> items;
		double minRating = Double.MAX_VALUE;
		double maxRating = Double.MIN_VALUE;

		int noRatings = 0;
		final Map<Object, Integer> noUserRatings = new HashMap<Object, Integer>();
		final Map<Object, Integer> noItemRatings = new HashMap<Object, Integer>();

		for (final Tuple<M> tuple : tuples) {
			final Object user = tuple.getAttribute(userAttributeIndex);
			final Object item = tuple.getAttribute(itemAttributeIndex);
			final double rating = tuple.getAttribute(ratingAttributeIndex);

			++noRatings;
			Integer nUR = noUserRatings.get(user);
			nUR = (nUR == null ? 1 : nUR + 1);
			noUserRatings.put(user, nUR);
			Integer nIR = noItemRatings.get(item);
			nIR = (nIR == null ? 1 : nIR + 1);
			noItemRatings.put(user, nIR);

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

		items = new ArrayList<Object>(avgItemRatings.keySet());

		if (logger.isDebugEnabled()) {
			logger.debug("LEARN: no of tuples: " + tuples.size()
					+ "  no of items: " + items.size());
		}

		return new BaselinePredictionRecommender(avgRating, avgUserRatings,
				avgItemRatings, items, minRating, maxRating);
	}

}
