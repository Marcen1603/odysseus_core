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

import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractTupleBasedRecommendationCandidatesBuilder<T extends Tuple<M>, M extends IMetaAttribute, U, I>
implements TupleBasedRecommendationCandidatesBuilder<T, M, U, I> {
	private final int userAttributeIndex, itemAttributeIndex;

	/**
	 *
	 */
	public AbstractTupleBasedRecommendationCandidatesBuilder(
			final SDFSchema inputschema, final SDFAttribute userAttribute,
			final SDFAttribute itemAttribute) {
		this.userAttributeIndex = inputschema.indexOf(userAttribute);
		this.itemAttributeIndex = inputschema.indexOf(itemAttribute);
		if (this.userAttributeIndex == -1) {
			throw new IllegalArgumentException("User attribute not found.");
		}
		if (this.itemAttributeIndex == -1) {
			throw new IllegalArgumentException("Item attribute not found.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .
	 * builder.TupleBasedRecommendationCandidatesBuilder#addRatedItem(de.uniol.
	 * inf.is.odysseus .core.collection.Tuple)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TupleBasedRecommendationCandidatesBuilder<T, M, U, I> addRatedItem(
			final Tuple<M> tuple) {
		return addRatedItem((U) tuple.getAttribute(this.userAttributeIndex),
				(I) tuple.getAttribute(this.itemAttributeIndex));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .builder.RecommendationCandidatesBuilder#addRatedItems(java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TupleBasedRecommendationCandidatesBuilder<T, M, U, I> addRatedItems(
			final Set<Tuple<M>> tuples) {
		for (final Tuple<M> tuple : tuples) {
			addRatedItem((U) tuple.getAttribute(this.userAttributeIndex),
					(I) tuple.getAttribute(this.itemAttributeIndex));
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .builder.RecommendationCandidatesBuilder#removeRatedItem(de.uniol.inf.is.
	 * odysseus.core.collection.Tuple)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TupleBasedRecommendationCandidatesBuilder<T, M, U, I> removeRatedItem(
			final Tuple<M> tuple) {
		return removeRatedItem((U) tuple.getAttribute(this.userAttributeIndex),
				(I) tuple.getAttribute(this.itemAttributeIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .builder.RecommendationCandidatesBuilder#removeRatedItems(java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TupleBasedRecommendationCandidatesBuilder<T, M, U, I> removeRatedItems(
			final Set<Tuple<M>> tuples) {
		for (final Tuple<M> tuple : tuples) {
			removeRatedItem((U) tuple.getAttribute(this.userAttributeIndex),
					(I) tuple.getAttribute(this.itemAttributeIndex));
		}
		return this;
	}
}
