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
package de.uniol.inf.is.odysseus.recommendation.learner;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.registry.RecommendationLearnerRegistry;

/**
 * A {@code RecommendationLearnerProvider} will be registered at the
 * {@link RecommendationLearnerRegistry}. It provides the name and a new
 * instance of the associated learner.
 * 
 * @author Cornelius Ludmann
 */
public interface RecommendationLearnerProvider<T extends ITimeInterval> {
	/**
	 * Returns the name of the learner.
	 * 
	 * @return the name of the learner
	 */
	String getLearnerName();

	/**
	 * Creates a new instance of the learner.
	 * 
	 * @param inputschema
	 *            The schema of the input tuples.
	 * @param userAttribute
	 *            The attribute that denotes the user id.
	 * @param itemAttribute
	 *            The attribute that denotes the item id.
	 * @param ratingAttribute
	 *            The attribute that denotes the rating.
	 * @param options
	 *            The options for the learner (optional, can be null)
	 * @return a new instance of the learner
	 */
	RecommendationLearner<T> newInstanceOfLearner(SDFSchema inputschema,
			SDFAttribute userAttribute, SDFAttribute itemAttribute,
			SDFAttribute ratingAttribute, Map<String, String> options);
}
