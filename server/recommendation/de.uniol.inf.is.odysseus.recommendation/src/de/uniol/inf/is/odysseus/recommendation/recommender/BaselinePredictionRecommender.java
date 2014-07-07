/**********************************************************************************
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
package de.uniol.inf.is.odysseus.recommendation.recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This recommender is based on the {@code BaselinePredictor} from MOA (massive
 * online analysis, {@link http://moa.cms.waikato.ac.nz/}).
 * 
 * <p>
 * <b>Javadoc from MOA:</b>
 * 
 * A naive algorithm which combines the global mean of all the existing ratings,
 * the mean rating of the user and the mean rating of the item to make a
 * prediction.
 * 
 * @author Cornelius Ludmann
 * 
 */
public class BaselinePredictionRecommender implements Recommender {

	protected static Logger logger = LoggerFactory
			.getLogger(BaselinePredictionRecommender.class);

	/**
	 * The average rating of the whole training set.
	 */
	private final double avgRating;

	/**
	 * This attribute maps the users to his/her average ratings.
	 */
	private final Map<Object, Double> avgUserRatings;

	/**
	 * This attribute maps the items to its average ratings.
	 */
	private final Map<Object, Double> avgItemRatings;

	/**
	 * All known item IDs.
	 */
	private final List<Object> items;

	/**
	 * The lowest rating value.
	 */
	private final double minRating;

	/**
	 * The highest rating value.
	 */
	private final double maxRating;

	/**
	 * Creates a new baseline prediction recommender.
	 * 
	 * @param avgRating
	 *            The average rating of the whole training set.
	 * @param avgUserRatings
	 *            This attribute maps the users to his/her average ratings.
	 * @param avgItemRatings
	 *            This attribute maps the items to its average ratings.
	 * @param items
	 *            All known item IDs.
	 * @param minRating
	 *            The lowest rating value.
	 * @param maxRating
	 *            The highest rating value.
	 */
	public BaselinePredictionRecommender(final double avgRating,
			final Map<Object, Double> avgUserRatings,
			final Map<Object, Double> avgItemRatings, final List<Object> items,
			final double minRating, final double maxRating) {
		this.avgRating = avgRating;
		this.avgUserRatings = avgUserRatings;
		this.avgItemRatings = avgItemRatings;
		this.items = items;
		if (minRating > maxRating) {
			throw new IllegalArgumentException(
					"minRating is higher than maxRating. (minRating: "
							+ minRating + ", maxRating: " + maxRating);
		}
		this.minRating = minRating;
		this.maxRating = maxRating;
	}

	@Override
	public Map<Object, Double> recommend(final Object user) {
		final Map<Object, Double> predictedRatings = new HashMap<Object, Double>(
				items.size());

		double userBias;
		if (!avgUserRatings.containsKey(user)
				|| avgUserRatings.get(user) == null) {
			// if the user does not exist, consider a bias of 0.
			userBias = 0;
		} else {
			userBias = avgUserRatings.get(user) - avgRating;
		}

		for (int i = 0; i < items.size(); ++i) {
			final Object itemId = items.get(i);

			final double itemBias = avgItemRatings.get(itemId) - avgRating;

			double rat = avgRating + userBias + itemBias;
			rat = Math.min(Math.max(rat, minRating), maxRating);

			predictedRatings.put(itemId, rat);
		}

		assert predictedRatings.size() == items.size();

		return predictedRatings;
	}

	@Override
	public Map<Object, Double> recommendTopN(final Object user, final int n) {
		if (n < 1) {
			throw new IllegalArgumentException(
					"Number of recommendations (n) need to be greater than 0.");
		}

		final Map<Object, Double> predictedRatings = recommend(user);

		if (predictedRatings == null) {
			return null;
		}

		if (predictedRatings.size() > n) {
			final List<Double> ratings = new ArrayList<Double>(
					predictedRatings.values());
			Collections.sort(ratings);

			final double minRating = ratings.get(ratings.size() - n);
			// The top-n-th element has the rating minRating.

			final Map<Object, Double> itemsWithMinRating = new HashMap<Object, Double>();
			final List<Object> removes = new ArrayList<Object>();
			for (final Object itemId : predictedRatings.keySet()) {
				if (predictedRatings.get(itemId) < minRating) {
					removes.add(itemId);
				} else if (predictedRatings.get(itemId) == minRating) {
					itemsWithMinRating
					.put(itemId, predictedRatings.get(itemId));
					removes.add(itemId);
				}
			}
			for (final Object remove : removes) {
				predictedRatings.remove(remove);
			}

			if (predictedRatings.size() + itemsWithMinRating.size() > n) {
				// There are too many items with the minimum rating. We have
				// to pick some randomly.

				final List<Object> keys = new ArrayList<Object>(
						itemsWithMinRating.keySet());
				final Random random = new Random();
				while (predictedRatings.size() != n) {
					final int i = random.nextInt(keys.size());
					predictedRatings.put(keys.get(i),
							itemsWithMinRating.get(keys.get(i)));
					keys.remove(i);
				}
			} else {
				predictedRatings.putAll(itemsWithMinRating);
			}
			assert predictedRatings.size() == n;
		}
		assert predictedRatings.size() <= n;

		return predictedRatings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 3;
		final StringBuilder builder = new StringBuilder();
		builder.append(BaselinePredictionRecommender.class.getSimpleName())
		.append(" [avgRating=")
		.append(avgRating)
		.append(", avgUserRatings=")
		.append(avgUserRatings != null ? toString(
				avgUserRatings.entrySet(), maxLen) : null)
				.append(", avgItemRatings=")
				.append(avgItemRatings != null ? toString(
						avgItemRatings.entrySet(), maxLen) : null).append("]");
		return builder.toString();
	}

	private String toString(final Collection<?> collection, final int maxLen) {
		final StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator
				.hasNext() && i < maxLen; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(iterator.next());
		}
		if (collection.size() > i) {
			builder.append(" (").append(collection.size() - i).append(" more)");
		}
		builder.append("]");
		return builder.toString();
	}

}
