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
import de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner;
import de.uniol.inf.is.odysseus.machine_learning.learner.Learner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.ImmutableRecommendationCandidatesSet;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;

/**
 * @author Cornelius Ludmann
 *
 * @see {@link Learner}, {@link AbstractLearner}
 */
public abstract class AbstractTupleBasedRecommendationLearner<T extends Tuple<M>, M extends IMetaAttribute, U, I, P>
		extends AbstractLearner<T, M, P> implements
		RecommendationLearner<T, M, U, I, P> {

	protected final int userAttributeIndex, itemAttributeIndex,
			ratingAttributeIndex;

	protected final Multiset<I> ratedItems = ConcurrentHashMultiset.create();
	protected final Map<U, Set<I>> ratedItemsByUser = Collections
			.synchronizedMap(new HashMap<U, Set<I>>());

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * addLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public void addLearningData(final T newLearningObject) {
		super.addLearningData(newLearningObject);
		addRatedItem(getUserInTuple(newLearningObject),
				getItemInTuple(newLearningObject));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * addLearningData(java.util.Set)
	 */
	@Override
	public void addLearningData(final Set<T> newLearningObjects) {
		super.addLearningData(newLearningObjects);
		for (final T t : newLearningObjects) {
			addRatedItem(getUserInTuple(t), getItemInTuple(t));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * removeLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public void removeLearningData(final T oldLearningObject) {
		super.removeLearningData(oldLearningObject);
		removeRatedItem(getUserInTuple(oldLearningObject),
				getItemInTuple(oldLearningObject));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * removeLearningData(java.util.Set)
	 */
	@Override
	public void removeLearningData(final Set<T> oldLearningObjects) {
		super.removeLearningData(oldLearningObjects);
		for (final T t : oldLearningObjects) {
			removeRatedItem(getUserInTuple(t), getItemInTuple(t));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		this.ratedItems.clear();
		this.ratedItemsByUser.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getRecommendationCandidatesModel()
	 */
	@Override
	public RecommendationCandidates<U, I> getRecommendationCandidatesModel() {
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

	private void addRatedItemOccurrence(final I ratedItem) {
		this.ratedItems.add(ratedItem);
	}

	private void removeRatedItemOccurrence(final I ratedItem) {
		this.ratedItems.remove(ratedItem);
	}

	@SuppressWarnings("unused")
	private void removeAllRatedItemOccurrences(final I ratedItem) {
		this.ratedItems.setCount(ratedItem, 0);
	}

	/**
	 * Adds an item rated by the user. This invokes also
	 * addRatedItem(ratedItem).
	 *
	 * @param user
	 *            The user.
	 * @param ratedItem
	 *            . The item
	 */
	protected void addRatedItem(final U user, final I ratedItem) {
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
	}

	protected void removeRatedItem(final U user, final I ratedItem) {
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
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getInfo()
	 */
	@Override
	public Map<String, String> getInfo() {
		final Map<String, String> info = new HashMap<>();
		info.put("Ratings in Model", String.valueOf(this.ratedItems.size()));
		info.put("Users in Model", String.valueOf(this.ratedItemsByUser.size()));
		info.put("Items in Model",
				String.valueOf(this.ratedItems.entrySet().size()));
		return info;
	}
}
