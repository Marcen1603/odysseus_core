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
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;

/**
 * @author Cornelius Ludmann
 *
 */
@Deprecated
public interface TupleBasedRecommendationCandidatesBuilder<T extends Tuple<M>, M extends IMetaAttribute, U, I> {

	/**
	 * @param user
	 * @param ratedItem
	 * @return
	 */
	TupleBasedRecommendationCandidatesBuilder<T, M, U, I> addRatedItem(U user,
			I ratedItem);

	TupleBasedRecommendationCandidatesBuilder<T, M, U, I> addRatedItem(
			Tuple<M> tuple);

	TupleBasedRecommendationCandidatesBuilder<T, M, U, I> addRatedItems(
			Set<Tuple<M>> tuples);

	/**
	 * @param user
	 * @param ratedItem
	 * @return
	 */
	TupleBasedRecommendationCandidatesBuilder<T, M, U, I> removeRatedItem(
			U user, I ratedItem);

	TupleBasedRecommendationCandidatesBuilder<T, M, U, I> removeRatedItem(
			Tuple<M> tuple);

	TupleBasedRecommendationCandidatesBuilder<T, M, U, I> removeRatedItems(
			Set<Tuple<M>> tuples);

	RecommendationCandidates<U, I> build();

	/**
	 *
	 */
	void clear();

}
