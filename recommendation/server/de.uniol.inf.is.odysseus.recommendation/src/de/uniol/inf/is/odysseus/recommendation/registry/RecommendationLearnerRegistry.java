/**
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.learner.TupleBasedRecommendationLearnerProvider;
import de.uniol.inf.is.odysseus.recommendation.learner.baseline_learner.BaselinePredictionRecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.TrainRecSysModelPO;

/**
 * This class register all recommendation learner.
 *
 * @author Cornelius Ludmann
 *
 */
public class RecommendationLearnerRegistry {
	private static RecommendationLearnerRegistry INSTANCE = null;

	private final Set<TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?>> learnerProvider = new HashSet<TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?>>();

	/**
	 * Singleton.
	 */
	private RecommendationLearnerRegistry() {
	}

	/**
	 * Returns the instance of the registry.
	 *
	 * @return
	 *
	 * @return the instance of the registry.
	 */
	public static synchronized RecommendationLearnerRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RecommendationLearnerRegistry();
		}

		return INSTANCE;
	}

	/**
	 * Adds a recommendation learner to the registry.
	 *
	 * @param learner
	 *            The recommendation learner to add.
	 */
	public void addRecommendationLearner(
			final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> learner) {
		this.learnerProvider.add(learner);
	}

	/**
	 * Removes a recommendation learner to the registry. Nothing happens if the
	 * recommendation learner does not exist.
	 *
	 * @param learner
	 *            The learner to remove.
	 */
	public void removeRecommendationLearner(
			final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> learner) {
		this.learnerProvider.remove(learner);

	}

	/**
	 * Creates an instance of the recommendation learner by the given name.
	 *
	 * @param name
	 *            The name of the recommendation learner.
	 * @return The instance of the recommendation learner or {@code null} if no
	 *         recommendation learner with the given name exists.
	 */
	public RecommendationLearner<?, ?, ?, ?, ?> createRecommendationLearner(
			final String name, final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {
		for (final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> provider : this.learnerProvider) {
			if (provider.getLearnerName().equalsIgnoreCase(name)) {
				return provider.newInstanceOfLearner(inputschema,
						userAttribute, itemAttribute, ratingAttribute, options);
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public List<String> getLearnerList() {
		final List<String> names = new ArrayList<String>();
		for (final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> provider : this.learnerProvider) {
			names.add(provider.getLearnerName());
		}
		return names;
	}

	/**
	 *
	 * TODO
	 *
	 * @param learner
	 * @return
	 *
	 *
	 */
	public Map<String, String> getPossibleOptions(final String learner) {
		if (learner
				.equalsIgnoreCase(BaselinePredictionRecommendationLearner.NAME)) {
			return null;
		} else {
			final Map<String, String> ret = new HashMap<String, String>();
			ret.put("test", "bla");
			ret.put("abc", "def");
			return ret;
		}
	}

	/**
	 * @param learner
	 * @param inputSchema
	 * @param userAttribute
	 * @param itemAttribute
	 * @param ratingAttribute
	 * @param options
	 * @param outputRecomCandObj
	 * @return
	 */
	public TrainRecSysModelPO<?, ?, ?, ?> createTrainRecSysModelPO(
			final String name, final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options, final boolean outputRecomCandObj) {
		for (final TupleBasedRecommendationLearnerProvider<?, ?, ?, ?, ?> provider : this.learnerProvider) {
			if (provider.getLearnerName().equalsIgnoreCase(name)) {
				return provider.newInstanceOfTrainRecSysModelPO(inputschema,
						userAttribute, itemAttribute, ratingAttribute, options,
						outputRecomCandObj);
			}
		}
		return null;
	}

}
