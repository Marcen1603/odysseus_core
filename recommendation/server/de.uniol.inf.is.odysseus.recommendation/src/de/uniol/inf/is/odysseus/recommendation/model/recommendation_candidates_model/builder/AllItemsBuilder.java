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
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.ImmutableRecommendationCandidatesSet;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;

/**
 * This builder creates a {@linkplain ImmutableRecommendationCandidatesSet} that
 * returns for all users all items that occurred ever.
 *
 * @author Cornelius Ludmann
 *
 */
public class AllItemsBuilder<T extends Tuple<M>, M extends IMetaAttribute, U, I>
extends AbstractTupleBasedRecommendationCandidatesBuilder<T, M, U, I> {

	private final Set<I> allItems = Collections
			.synchronizedSet(new HashSet<I>());

	/**
	 *
	 */
	public AllItemsBuilder(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute) {
		super(inputschema, userAttribute, itemAttribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .builder.RecommendationCandidatesBuilder#addRatedItem(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public TupleBasedRecommendationCandidatesBuilder<T, M, U, I> addRatedItem(
			final U user, final I ratedItem) {
		this.allItems.add(ratedItem);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .
	 * builder.TupleBasedRecommendationCandidatesBuilder#removeRatedItem(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public TupleBasedRecommendationCandidatesBuilder<T, M, U, I> removeRatedItem(
			final U user, final I ratedItem) {
		// do nothing
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model
	 * .builder.RecommendationCandidatesBuilder#build()
	 */
	@Override
	public RecommendationCandidates<U, I> build() {
		final ImmutableMap<U, ImmutableSet<I>> map = ImmutableMap.of();
		return new ImmutableRecommendationCandidatesSet<U, I>(
				ImmutableSet.copyOf(this.allItems), map);
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
		this.allItems.clear();
	}
}
