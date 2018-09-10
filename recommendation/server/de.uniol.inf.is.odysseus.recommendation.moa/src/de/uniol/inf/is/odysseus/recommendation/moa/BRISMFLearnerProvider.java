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
package de.uniol.inf.is.odysseus.recommendation.moa;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.learner.TupleBasedRecommendationLearnerProvider;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.TrainRecSysModelPO;

/**
 * @author Cornelius Ludmann
 *
 */
public class BRISMFLearnerProvider
		implements
		TupleBasedRecommendationLearnerProvider<Tuple<ITimeInterval>, ITimeInterval, Integer, Integer, Double> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.learner.
	 * TupleBasedRecommendationLearnerProvider #getLearnerName()
	 */
	@Override
	public String getLearnerName() {
		return BRISMFRecommendationLearner.NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.learner.
	 * TupleBasedRecommendationLearnerProvider
	 * #newInstanceOfLearner(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute, java.util.Map)
	 */
	@Override
	public RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Integer, Integer, Double> newInstanceOfLearner(
			final SDFSchema inputschema, final SDFAttribute userAttribute,
			final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {
		if (!userAttribute.getDatatype().isInteger()) {
			throw new IllegalArgumentException(
					"The attribute with the user identifier must by of type 'integer' but is "
							+ userAttribute.getDatatype() + ".");
		}
		if (!itemAttribute.getDatatype().isInteger()) {
			throw new IllegalArgumentException(
					"The attribute with the item identifier must by of type 'integer' but is "
							+ userAttribute.getDatatype() + ".");
		}
		if (!ratingAttribute.getDatatype().isDouble()) {
			throw new IllegalArgumentException(
					"The attribute with the rating must by of type 'double' but is "
							+ userAttribute.getDatatype() + ".");
		}
		return new BRISMFRecommendationLearner(inputschema, userAttribute,
				itemAttribute, ratingAttribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.learner.
	 * TupleBasedRecommendationLearnerProvider
	 * #newInstanceOfTrainRecSysModelPO(de
	 * .uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute, java.util.Map)
	 */
	@Override
	public TrainRecSysModelPO<ITimeInterval, Integer, Integer, Double> newInstanceOfTrainRecSysModelPO(
			SDFSchema inputschema, SDFAttribute userAttribute,
			SDFAttribute itemAttribute, SDFAttribute ratingAttribute,
			Map<String, String> options, final boolean outputRecomCandObj) {
		// TupleBasedRecommendationCandidatesBuilder<Tuple<ITimeInterval>,
		// ITimeInterval, Integer, Integer> recommCandBuilder = null;
		// if (outputRecomCandObj) {
		// recommCandBuilder = new AllUnratedItemsByUserBuilder<>(inputschema,
		// userAttribute, itemAttribute);
		// }
		final RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Integer, Integer, Double> learner = newInstanceOfLearner(
				inputschema, userAttribute, itemAttribute, ratingAttribute,
				options);
		final TrainRecSysModelPO<ITimeInterval, Integer, Integer, Double> po = new TrainRecSysModelPO<ITimeInterval, Integer, Integer, Double>(
				learner, outputRecomCandObj);
		return po;
	}
}
