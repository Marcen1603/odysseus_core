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
package de.uniol.inf.is.odysseus.recommendation.matrix_factorization.learner;

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
public class BasicMFLearnerProvider
		implements
		TupleBasedRecommendationLearnerProvider<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.learner.
	 * TupleBasedRecommendationLearnerProvider#getLearnerName()
	 */
	@Override
	public String getLearnerName() {
		// TODO Auto-generated method stub
		return "BasicMF";
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
	public RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> newInstanceOfLearner(
			final SDFSchema inputschema, final SDFAttribute userAttribute,
			final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {
		if (!userAttribute.getDatatype().isLong()) {
			throw new IllegalArgumentException(
					"The attribute with the user identifier must by of type 'long' but is "
							+ userAttribute.getDatatype() + ".");
		}
		if (!itemAttribute.getDatatype().isLong()) {
			throw new IllegalArgumentException(
					"The attribute with the item identifier must by of type 'long' but is "
							+ userAttribute.getDatatype() + ".");
		}
		if (!ratingAttribute.getDatatype().isDouble()) {
			throw new IllegalArgumentException(
					"The attribute with the rating must by of type 'double' but is "
							+ userAttribute.getDatatype() + ".");
		}
		return new BasicMFLearner(inputschema, userAttribute, itemAttribute,
				ratingAttribute, options);
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
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute, java.util.Map,
	 * boolean)
	 */
	@Override
	public TrainRecSysModelPO<ITimeInterval, Long, Long, Double> newInstanceOfTrainRecSysModelPO(
			final SDFSchema inputschema, final SDFAttribute userAttribute,
			final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options, final boolean outputRecomCandObj) {
		final RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> learner = this
				.newInstanceOfLearner(inputschema, userAttribute,
						itemAttribute, ratingAttribute, options);
		final TrainRecSysModelPO<ITimeInterval, Long, Long, Double> po = new TrainRecSysModelPO<>(
				learner, outputRecomCandObj);
		return po;
	}

}
