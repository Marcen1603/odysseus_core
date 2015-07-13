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

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.machine_learning.learner.Learner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;

/**
 * A {@code RecommendationLearner} learns a new model for a recommendation task.
 *
 * @author Cornelius Ludmann
 *
 * @param <T>
 *            Type of the learning data objects.
 * @param <M>
 *            Type of the metadata of the learning data objects.
 * @param <U>
 *            The type of the user identifier.
 * @param <I>
 *            The type of the item identifier.
 * @param <P>
 *            Type of the ratings/predictions.
 */
public interface RecommendationLearner<T extends IStreamObject<M>, M extends IMetaAttribute, U, I, P>
		extends Learner<T, M, P> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.Learner#getModel()
	 */
	@Override
	public RatingPredictor<T, M, U, I, P> getModel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#getModel(boolean
	 * )
	 */
	@Override
	public RatingPredictor<T, M, U, I, P> getModel(boolean train);

	RecommendationCandidates<U, I> getRecommendationCandidatesModel();

	/**
	 * Get some information (key-value pairs) about the learner (e.g. number of
	 * users or items used for learning).
	 * 
	 * @return A key-value map with some status information about the learner.
	 */
	public Map<? extends String, ? extends String> getInfo();
}
