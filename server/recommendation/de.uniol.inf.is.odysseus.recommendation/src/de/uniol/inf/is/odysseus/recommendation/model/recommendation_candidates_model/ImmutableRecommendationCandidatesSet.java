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
package de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.builder.AllUnratedItemsByUserBuilder;

/**
 * This is an immutable implementation of {@link RecommendationCandidates}.
 *
 * @author Cornelius Ludmann
 *
 */
public class ImmutableRecommendationCandidatesSet<U, I> implements
		RecommendationCandidates<U, I> {
	private final ImmutableSet<I> allItems;
	private final ImmutableMap<U, ImmutableSet<I>> recommendationCandidates;

	/**
	 * Creates an empty object.
	 */
	public ImmutableRecommendationCandidatesSet() {
		this.allItems = ImmutableSet.of();
		this.recommendationCandidates = ImmutableMap.of();
	}

	/**
	 * Default constructor. Consider using the
	 * {@link AllUnratedItemsByUserBuilder}.
	 */
	public ImmutableRecommendationCandidatesSet(final ImmutableSet<I> allItems,
			final ImmutableMap<U, ImmutableSet<I>> unratedItemsByUser) {
		if (allItems == null || unratedItemsByUser == null) {
			throw new IllegalArgumentException("parameters need to be not null");
		}
		this.allItems = allItems;
		this.recommendationCandidates = unratedItemsByUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.RecommendationCandidates
	 * #getUnratedItems(java.lang.Object)
	 */
	@Override
	public Set<I> getRecommendationCandidates(final U user) {
		Set<I> recommCand = this.recommendationCandidates.get(user);
		if (recommCand == null) {
			recommCand = this.allItems;
		}
		return recommCand;
	}

	/**
	 * <p>
	 * Returns an iterator for the recommendation candidates for the passed
	 * user.
	 *
	 * <p>
	 * This implementation returns an iterator of all items if the user does
	 * exists.
	 *
	 * <p>
	 * This implementation invokes
	 * {@linkplain ImmutableRecommendationCandidatesSet#getRecommendationCandidates(U)}.
	 */
	@Override
	public Iterator<I> getRecommendationCandidatesIterator(final U user) {
		final Set<I> recommCand = getRecommendationCandidates(user);
		return recommCand.iterator();
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
		builder.append("ImmutableRecommendationCandidatesSet [allItems=");
		builder.append(this.allItems != null ? toString(this.allItems, maxLen)
				: null);
		builder.append(", recommendationCandidates=");
		builder.append(this.recommendationCandidates != null ? toString(
				this.recommendationCandidates.entrySet(), maxLen) : null);
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
		builder.append("]");
		return builder.toString();
	}

}
