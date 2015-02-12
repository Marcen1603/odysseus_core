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
package de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.ImmutableRecommendationCandidatesSet;

/**
 * This builder creates an {@link ImmutableRecommendationCandidatesSet} object.
 * The built object returns all unrated items as recommendation candidates.
 *
 * @author Cornelius Ludmann
 */
public class AllUnratedItemsByUserBuilder<T extends Tuple<M>, M extends IMetaAttribute, U, I>
		extends AbstractTupleBasedRecommendationCandidatesBuilder<T, M, U, I> {

	/**
	 * @param inputschema
	 * @param userAttribute
	 * @param itemAttribute
	 */
	public AllUnratedItemsByUserBuilder(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute) {
		super(inputschema, userAttribute, itemAttribute);
	}

	private final Multiset<I> ratedItems = ConcurrentHashMultiset.create();
	private final Map<U, Set<I>> ratedItemsByUser = Collections
			.synchronizedMap(new HashMap<U, Set<I>>());

	/**
	 * Adds an occurrence of an items that were rated by anyone.
	 *
	 * @param ratedItems
	 *            A set of items.
	 * @return The builder.
	 */
	public AllUnratedItemsByUserBuilder<T, M, U, I> addRatedItemOccurrence(
			final I ratedItem) {
		this.ratedItems.add(ratedItem);
		return this;
	}

	public AllUnratedItemsByUserBuilder<T, M, U, I> removeRatedItemOccurrence(
			final I ratedItem) {
		this.ratedItems.remove(ratedItem);
		return this;
	}

	public AllUnratedItemsByUserBuilder<T, M, U, I> removeAllRatedItemOccurrences(
			final I ratedItem) {
		this.ratedItems.setCount(ratedItem, 0);
		return this;
	}

	/**
	 * Adds an item rated by the user. This invokes also
	 * addRatedItem(ratedItem).
	 *
	 * @param user
	 *            The user.
	 * @param ratedItem
	 *            . The item
	 * @return The builder.
	 */
	@Override
	public AllUnratedItemsByUserBuilder<T, M, U, I> addRatedItem(final U user,
			final I ratedItem) {
		if (user != null && ratedItem != null) {
			addRatedItemOccurrence(ratedItem);
			Set<I> ratedItemsByUser = this.ratedItemsByUser.get(user);
			if (ratedItemsByUser == null) {
				ratedItemsByUser = new HashSet<>();
				this.ratedItemsByUser.put(user, ratedItemsByUser);
			}
			ratedItemsByUser.add(ratedItem);
			// this.ratedItemsByUser.put(user, ratedItemsByUser);
		}
		return this;
	}

	@Override
	public AllUnratedItemsByUserBuilder<T, M, U, I> removeRatedItem(
			final U user, final I ratedItem) {
		if (user != null && ratedItem != null) {
			removeRatedItemOccurrence(ratedItem);
			final Set<I> ratedItemsByUser = this.ratedItemsByUser.get(user);
			if (ratedItemsByUser != null) {
				ratedItemsByUser.remove(ratedItem);
				if (ratedItemsByUser.size() == 0) {
					this.ratedItemsByUser.remove(user);
				}
			}
		}
		return this;
	}

	/**
	 * Builds an {@link ImmutableRecommendationCandidatesSet} object.
	 *
	 * @return the {@link ImmutableRecommendationCandidatesSet} object
	 */
	@Override
	public ImmutableRecommendationCandidatesSet<U, I> build() {
		final Map<U, ImmutableSet<I>> unratedItemsByUser = new HashMap<>();
		final Set<I> ratedItemsSet = this.ratedItems.elementSet();

		for (final U user : this.ratedItemsByUser.keySet()) {
			unratedItemsByUser.put(user,
					ImmutableSet.copyOf(getUnratedItems(user, ratedItemsSet)));
		}

		return new ImmutableRecommendationCandidatesSet<U, I>(
				ImmutableSet.copyOf(ratedItemsSet),
				ImmutableMap.copyOf(unratedItemsByUser));
	}

	private Set<I> getUnratedItems(final U user, final Set<I> ratedItemsSet) {
		final HashSet<I> unratedItems = new HashSet<>(ratedItemsSet);
		unratedItems.removeAll(this.ratedItemsByUser.get(user));
		return unratedItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .builder.RecommendationCandidatesBuilder#clear()
	 */
	@Override
	public void clear() {
		this.ratedItems.clear();
		this.ratedItemsByUser.clear();
	}
}