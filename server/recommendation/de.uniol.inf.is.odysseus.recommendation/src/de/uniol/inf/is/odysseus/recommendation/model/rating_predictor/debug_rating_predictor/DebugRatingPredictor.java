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
package de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.debug_rating_predictor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.AbstractTupleBasedRatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.TrainRecSysModelPO;

/**
 * This rating predictor predicts always 0. Its toString method returns all
 * learning tuples (max 10) that are valid for this model. This class can be
 * used to test if {@linkplain TrainRecSysModelPO} builds the correct learning
 * sets.
 *
 * @author Cornelius Ludmann
 *
 */
public class DebugRatingPredictor<T extends Tuple<M>, M extends IMetaAttribute, U, I>
extends AbstractTupleBasedRatingPredictor<T, M, U, I, Double> {

	/**
	 * @author Cornelius Ludmann
	 *
	 */
	public static class UserItemRating<U, I> {
		public U user;
		public I item;
		public double rating;

		public UserItemRating(final U user, final I item, final double rating) {
			super();
			this.user = user;
			this.item = item;
			this.rating = rating;
		}

		@Override
		public String toString() {
			return "(" + this.user + "," + this.item + "," + this.rating + ")";
		}
	}

	protected static Logger logger = LoggerFactory
			.getLogger(DebugRatingPredictor.class);

	private final Set<UserItemRating<U, I>> tuples;

	/**
	 *
	 */
	public DebugRatingPredictor(
			final ImmutableSet<UserItemRating<U, I>> learningTuples) {
		this.tuples = learningTuples;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.
	 * AbstractTupleBasedRatingPredictor#predict(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Double predict(final U user, final I item) {
		logger.info("predict(user = " + user + ", item = " + item + ")");
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		final StringBuilder builder = new StringBuilder();
		builder.append("DebugRatingPredictor [tuples=");
		builder.append(this.tuples != null ? toString(this.tuples, maxLen)
				: null);
		builder.append("]");
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
		if (collection.size() > maxLen) {
			builder.append(" " + (collection.size() - maxLen) + " more");
		}
		builder.append("]");
		return builder.toString();
	}
}
