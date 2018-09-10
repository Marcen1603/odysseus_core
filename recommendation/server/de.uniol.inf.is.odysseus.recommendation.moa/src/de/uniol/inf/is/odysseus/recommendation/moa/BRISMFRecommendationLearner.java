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

import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.AbstractTupleBasedRecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;

/**
 * @author Cornelius Ludmann
 *
 */
public class BRISMFRecommendationLearner extends
		AbstractTupleBasedRecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Integer, Integer, Double> {

	public static final String NAME = "BRISMF.MOA";
	
	/**
	 * @param inputschema
	 * @param userAttribute
	 * @param itemAttribute
	 * @param ratingAttribute
	 */
	public BRISMFRecommendationLearner(SDFSchema inputschema,
			SDFAttribute userAttribute, SDFAttribute itemAttribute,
			SDFAttribute ratingAttribute) {
		super(inputschema, userAttribute, itemAttribute, ratingAttribute);
	}

	private BRISMFPredictor brismfPredictor = null;
	private final int nFeatures = 20;


	public boolean isPredictorNull() {
		return this.brismfPredictor == null;
	}

	/**
	 *
	 */
	private void initPredictor() {
		final RecommenderData data = new MemRecommenderData();
		this.brismfPredictor = new BRISMFPredictor(this.nFeatures, data, false);
		this.brismfPredictor.getData().clear();
		this.brismfPredictor.getData().disableUpdates(false);
	}

	@Override
	public void clear() {
		this.brismfPredictor = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getModel(boolean)
	 */
	@Override
	public RatingPredictor<Tuple<ITimeInterval>, ITimeInterval, Integer, Integer, Double> getModel(
			boolean train) {
		if(brismfPredictor == null)
			return null;
		return brismfPredictor.getImmutableCopy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#trainModel()
	 */
	@Override
	public void trainModel() {
		// nothing to do. model is trained on-the-fly.
		isModelUpToDate = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * addLearningData(java.util.Set)
	 */
	@Override
	public void addLearningData(Set<Tuple<ITimeInterval>> newLearningObjects) {
		if (isPredictorNull()) {
			initPredictor();
		}
		// TODO: disable updates?
		for (Tuple<ITimeInterval> tuple : newLearningObjects) {
			int user = getUserInTuple(tuple);
			int item = getItemInTuple(tuple);
			double rating = getRatingInTuple(tuple);
			this.brismfPredictor.getData().setRating(user, item, rating);
		}
		
		
		for (final Tuple<ITimeInterval> t : newLearningObjects) {
			addRatedItem(getUserInTuple(t), getItemInTuple(t));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * addLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public void addLearningData(Tuple<ITimeInterval> tuple) {
		if (isPredictorNull()) {
			initPredictor();
		}
		int user = getUserInTuple(tuple);
		int item = getItemInTuple(tuple);
		double rating = getRatingInTuple(tuple);
		this.brismfPredictor.getData().setRating(user, item, rating);
		
		
		addRatedItem(getUserInTuple(tuple),
				getItemInTuple(tuple));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * removeLearningData(java.util.Set)
	 */
	@Override
	public void removeLearningData(Set<Tuple<ITimeInterval>> oldLearningObjects) {
		if (isPredictorNull()) {
			initPredictor();
		}
		// TODO: disable updates?
		brismfPredictor.getData().disableUpdates(true);
		for (Tuple<ITimeInterval> tuple : oldLearningObjects) {
			int user = getUserInTuple(tuple);
			int item = getItemInTuple(tuple);
			this.brismfPredictor.getData().removeRating(user, item);
		}
		brismfPredictor.getData().disableUpdates(false);
		
		
		for (final Tuple<ITimeInterval> t : oldLearningObjects) {
			removeRatedItem(getUserInTuple(t), getItemInTuple(t));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#
	 * removeLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public void removeLearningData(Tuple<ITimeInterval> tuple) {
		if (isPredictorNull()) {
			initPredictor();
		}
		int user = getUserInTuple(tuple);
		int item = getItemInTuple(tuple);
		this.brismfPredictor.getData().removeRating(user, item);
		
		
		removeRatedItem(getUserInTuple(tuple),
				getItemInTuple(tuple));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#isTrained
	 * ()
	 */
	@Override
	public boolean isTrained() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner#getName
	 * ()
	 */
	@Override
	public String getName() {
		return BRISMFRecommendationLearner.NAME;
	}

}
