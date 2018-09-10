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

import java.util.Iterator;
import java.util.Set;

/**
 * This gives the recommendation candidates for a user. Usually, it returns the
 * unrated items for a user.
 *
 * @author Cornelius Ludmann
 *
 */
public interface RecommendationCandidates<U, I> {

	/**
	 * Returns a set of recommendation candidates.
	 *
	 * @param user
	 *            The user.
	 * @return The set of recommendation candidates.
	 */
	Set<I> getRecommendationCandidates(U user);

	/**
	 * Returns an iterator for the recommendation candidates for the passed
	 * user.
	 *
	 * @param user
	 *            The user.
	 * @return The iterator.
	 */
	Iterator<I> getRecommendationCandidatesIterator(U user);

}
