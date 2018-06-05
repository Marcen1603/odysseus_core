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
package de.uniol.inf.is.odysseus.recommendation.mahout.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.Recommender;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.AbstractTupleBasedRatingPredictor;

/**
 * @author Cornelius Ludmann
 * 
 */
public class MahoutRecommender
		extends
		AbstractTupleBasedRatingPredictor<Tuple<ITimeInterval>, ITimeInterval,Long, Long, Double> {

	private final Recommender recommender;

	public MahoutRecommender(final Recommender recommender) {
		// TODO: is recommender immutable?
		this.recommender = recommender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.
	 * AbstractTupleBasedRatingPredictor#predict(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Double predict(Long user, Long item) {
		try {
			return (double) recommender.estimatePreference(user, item);
		} catch (TasteException e) {
			return 0.0;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MahoutRecommender [recommender=");
		builder.append(recommender);
		builder.append("]");
		return builder.toString();
	}
	
	

}
