/**********************************************************************************
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
package de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.baseline_rating_predictor;

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

import com.google.common.collect.ImmutableMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.AbstractTupleBasedRatingPredictor;

/**
 * <p>
 * This recommender based on {@code BaselinePredictor} from MOA (massive online
 * analysis, {@link http://moa.cms.waikato.ac.nz/}).
 *
 * <p>
 * <b>Javadoc from MOA:</b> A naive algorithm which combines the global mean of
 * all the existing ratings, the mean rating of the user and the mean rating of
 * the item to make a prediction.
 *
 * <p>
 * This implementation is immutable.
 *
 * @author Cornelius Ludmann
 *
 */
public class BaselineRatingPredictor<T extends Tuple<M>, M extends IMetaAttribute, U, I>
extends AbstractTupleBasedRatingPredictor<T, M, U, I, Double> {

	protected static Logger logger = LoggerFactory
			.getLogger(BaselineRatingPredictor.class);

	/**
	 * The average rating of the whole training set.
	 */
	private final double avgRating;

	/**
	 * This attribute maps the users to his/her average ratings.
	 */
	private final ImmutableMap<U, Double> avgUserRatings;

	/**
	 * This attribute maps the items to its average ratings.
	 */
	private final ImmutableMap<I, Double> avgItemRatings;

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
	public BaselineRatingPredictor(final double avgRating,
			final Map<U, Double> avgUserRatings,
			final Map<I, Double> avgItemRatings, final double minRating,
			final double maxRating) {
		this.avgRating = avgRating;
		this.avgUserRatings = ImmutableMap.copyOf(avgUserRatings);
		this.avgItemRatings = ImmutableMap.copyOf(avgItemRatings);
		if (minRating > maxRating) {
			throw new IllegalArgumentException(
					"minRating is higher than maxRating. (minRating: "
							+ minRating + ", maxRating: " + maxRating);
		}
		this.minRating = minRating;
		this.maxRating = maxRating;
	}

	/**
	 * Calculates the user bias. If user does not exist, a bias of 0 is assumed.
	 *
	 * @param user
	 *            The user.
	 * @return The user bias.
	 */
	private double getUserBias(final U user) {
		double userBias;
		if (!this.avgUserRatings.containsKey(user)
				|| this.avgUserRatings.get(user) == null) {
			// if the user does not exist, consider a bias of 0.
			userBias = 0;
		} else {
			userBias = this.avgUserRatings.get(user) - this.avgRating;
		}
		return userBias;
	}

	/**
	 * Calculates the item bias. If item does not exist, a bias of 0 is assumed.
	 *
	 * @param item
	 *            The item.
	 * @return The item bias.
	 */
	private double getItemBias(final I item) {
		double itemBias;
		if (!this.avgItemRatings.containsKey(item)
				|| this.avgItemRatings.get(item) == null) {
			// if the item does not exist, consider a bias of 0.
			itemBias = 0;
		} else {
			itemBias = this.avgItemRatings.get(item) - this.avgRating;
		}
		return itemBias;
	}

	/**
	 * Predicts the ratings for all known items for the user.
	 *
	 * @param user
	 *            The user.
	 * @return A map of items to predicted ratings.
	 */
	public ImmutableMap<I, Double> predictRatingForAllItems(final U user) {
		final int itemsSize = this.avgItemRatings.size();
		final Map<I, Double> predictedRatings = new HashMap<I, Double>(
				itemsSize);

		for (final I item : this.avgItemRatings.keySet()) {
			final double rat = predict(user, item);

			predictedRatings.put(item, rat);
		}

		assert predictedRatings.size() == itemsSize;

		return ImmutableMap.copyOf(predictedRatings);
	}

	/**
	 * Predicts the ratings for all known items for the user and returns the top
	 * N items.
	 *
	 * @param user
	 *            The user.
	 * @param n
	 *            Number of items.
	 * @return A map of items to predicted ratings.
	 */
	public ImmutableMap<I, Double> recommendTopN(final U user, final int n) {
		if (n < 1) {
			throw new IllegalArgumentException(
					"Number of recommendations (n) need to be greater than 0.");
		}

		final Map<I, Double> predictedRatings = predictRatingForAllItems(user);

		if (predictedRatings == null) {
			return null;
		}

		if (predictedRatings.size() > n) {
			final List<Double> ratings = new ArrayList<Double>(
					predictedRatings.values());
			Collections.sort(ratings);

			final double minRating = ratings.get(ratings.size() - n);
			// The top-n-th element has the rating minRating.

			final Map<I, Double> itemsWithMinRating = new HashMap<I, Double>();
			final List<I> removes = new ArrayList<I>();
			for (final I itemId : predictedRatings.keySet()) {
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

				final List<I> keys = new ArrayList<I>(
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

		return ImmutableMap.copyOf(predictedRatings);
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
		builder.append(BaselineRatingPredictor.class.getSimpleName())
		.append(" [avgRating=")
		.append(this.avgRating)
		.append(", avgUserRatings=")
		.append(this.avgUserRatings != null ? toString(
				this.avgUserRatings.entrySet(), maxLen) : null)
				.append(", avgItemRatings=")
				.append(this.avgItemRatings != null ? toString(
						this.avgItemRatings.entrySet(), maxLen) : null)
						.append("]");
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.AbstractRatingPredictor
	 * #predict(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Double predict(final U user, final I item) {
		final double userBias = getUserBias(user);
		final double itemBias = getItemBias(item);

		double rat = this.avgRating + userBias + itemBias;
		rat = Math.min(Math.max(rat, this.minRating), this.maxRating);

		return rat;
	}

}
