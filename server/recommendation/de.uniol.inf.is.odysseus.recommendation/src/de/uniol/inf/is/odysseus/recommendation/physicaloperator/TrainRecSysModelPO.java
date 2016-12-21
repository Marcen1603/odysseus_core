/**********************************************************************************
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
package de.uniol.inf.is.odysseus.recommendation.physicaloperator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.ConstantValueRatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.ImmutableRecommendationCandidatesSet;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;

/**
 * @author Cornelius Ludmann
 *
 */
public class TrainRecSysModelPO<M extends ITimeInterval, U, I, P> extends
		AbstractPipe<Tuple<M>, Tuple<M>> implements Cloneable,
		IPhysicalOperatorKeyValueProvider {

	protected static Logger logger = LoggerFactory
			.getLogger(TrainRecSysModelPO.class);

	private boolean transferModelOnlyOnPunctuation;

	private Tuple<M> modelTuple;
	private Tuple<M> recommCandTuple;
	private PointInTime previousSystemPointInTimeOnPunctuation = new PointInTime(System.currentTimeMillis());

	/**
	 * The point in time of the interval start of these learning tuples that
	 * were currently buffered. When a tuple arrives, whose interval start is
	 * greater than <code>currentBufferingPointInTime</code>, all buffered
	 * tuples can be passed to the learner because no further tuple with the
	 * same interval start time will be arrived.
	 */
	private PointInTime currentBufferingPointInTime = PointInTime.getZeroTime();

	/**
	 * <p>
	 * The currently buffered learning tuples. When a tuple with a newer
	 * interval start arrives, these Set will be passed to the learner as new
	 * learning tuples. Afterward, this field will be initialized with a new
	 * {@link LinkedHashSet}.
	 *
	 * <p>
	 * A {@link LinkedHashSet} is used to preserve the order of arrival, to
	 * support a fast iteration in this order, and to ensure that each tuple
	 * occurs only once.
	 */
	private Set<Tuple<M>> newLearningTuples = new LinkedHashSet<>();

	/**
	 * <p>
	 * All tuples that decay in the future. This map maps the points in time of
	 * the decay (the end point in time of the time interval) to the learning
	 * tuples.
	 *
	 * <p>
	 * A {@link TreeMap} is used, because we need the entries ordered by decay
	 * time (key of the map). Each new entry is initialized with a
	 * {@link LinkedHashSet} which preserves the order of insertion of the new
	 * tuples with the same decay time and allows a fast iteration.
	 */
	private SortedMap<PointInTime, Set<Tuple<M>>> decayingLearningTuples = new TreeMap<>();

	/**
	 * The learner.
	 */
	private final RecommendationLearner<Tuple<M>, M, U, I, P> learner;

	private final boolean outputRecomCandObj;

	/**
	 * This variable stores the number of leanring tuples that are currently
	 * valid. That means, every time a tuple is transfered to the learner, this
	 * variable is increased, every time a tuple is removed from the leaner
	 * (decaying tuple), this variable is decreased.
	 */
	private long noOfActiveLearningTuples = 0;

	/**
	 *
	 */
	// private TupleBasedRecommendationCandidatesBuilder<Tuple<M>, M, U, I>
	// recommCandBuilder = null;

	/**
	 * Constructor.
	 *
	 * @param learner
	 *            The learner that should be used.
	 * @param outputRecommendationCandidatesModels
	 * @param recommCandBuilder
	 *            The builder for {@linkplain RecommendationCandidates}.
	 *            <code>null</code> is allowed. If <code>null</code>, no
	 *            {@linkplain RecommendationCandidates} will be transfered.
	 */
	public TrainRecSysModelPO(
			final RecommendationLearner<Tuple<M>, M, U, I, P> learner/*
																	 * , final
																	 * TupleBasedRecommendationCandidatesBuilder
																	 * <
																	 * Tuple<M>,
																	 * M, U, I>
																	 * recommCandBuilder
																	 */) {
		if (learner == null) {
			throw new NullPointerException("learner needs to be not null");
		}
		this.learner = learner;
		this.outputRecomCandObj = true;
		// this.recommCandBuilder = recommCandBuilder;
	}

	/**
	 * Constructor.
	 *
	 * @param learner
	 *            The learner that should be used.
	 * @param outputRecommendationCandidatesModels
	 * @param recommCandBuilder
	 *            The builder for {@linkplain RecommendationCandidates}.
	 *            <code>null</code> is allowed. If <code>null</code>, no
	 *            {@linkplain RecommendationCandidates} will be transfered.
	 */
	public TrainRecSysModelPO(
			final RecommendationLearner<Tuple<M>, M, U, I, P> learner,
			final boolean outputRecomCandObj) {
		if (learner == null) {
			throw new NullPointerException("learner needs to be not null");
		}
		this.learner = learner;
		this.outputRecomCandObj = outputRecomCandObj;
	}

	/**
	 * Copy constructor.
	 *
	 * @param recommendationLearnPO
	 */
	public TrainRecSysModelPO(
			final TrainRecSysModelPO<M, U, I, P> recommendationLearnPO) {
		this.learner = recommendationLearnPO.learner;
		this.outputRecomCandObj = recommendationLearnPO.outputRecomCandObj;
		this.transferModelOnlyOnPunctuation = recommendationLearnPO.transferModelOnlyOnPunctuation;
		// this.recommCandBuilder = recommendationLearnPO.recommCandBuilder;
		// TODO: save copy of fields?
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(final Tuple<M> object, final int port) {
		final PointInTime ts = object.getMetadata().getStart();
		final PointInTime te = object.getMetadata().getEnd();
		@SuppressWarnings("unchecked")
		final M metadata = (M) object.getMetadata().clone();

		synchronized (this.newLearningTuples) {

			if (ts.equals(this.currentBufferingPointInTime)) {
				/*
				 * This tuple has the same start time as the other tuples in
				 * newLearningTuples. All tuples with the same start time should
				 * be passed to the learner at once (buffering). Therefore, this
				 * tuple will be solely added to the learning tuples set and to
				 * the decaying tuples set. That's it.
				 */
				this.newLearningTuples.add(object);
				addToDecayingLearningTuples(object, te);
			} else {
				/*
				 * This tuple has a greater start time as the tuples in
				 * newLearningTuples. Thus, no tuple with the same start time
				 * will be arrived in the future. All new learning tuples are
				 * passed to the learner.
				 */

				// A newer learning tuples as currentBufferingPointInTime is
				// arrived. Add all buffered learning tuples to the learner.
				addLearningTuplesToLearners(this.newLearningTuples);

				// remove all tuples that decay at currentBufferingPointInTime
				// this.learner.removeLearningData(this.decayingLearningTuples
				// .get(currentBufferingPointInTime));
				// this.decayingLearningTuples
				// .remove(this.currentBufferingPointInTime);

				if (this.decayingLearningTuples.isEmpty()) {
					/*
					 * There are no learning tuples that will be decay in the
					 * future. Therefore, we can learn a model that is valid
					 * from the start of the new learning tuples to the start of
					 * the new arrived tuple.
					 */
					createAndTransferNewModel(this.currentBufferingPointInTime,
							ts, metadata);

				} else {

					/*
					 * There could be tuples that decay before the new arrived
					 * tuple get valid. For each interval which its own learning
					 * set, we learn a model.
					 */

					// We start with the start time of the new learning tuples.
					PointInTime startP = this.currentBufferingPointInTime;

					synchronized (this.decayingLearningTuples) {
						final Iterator<Entry<PointInTime, Set<Tuple<M>>>> iter = this.decayingLearningTuples
								.entrySet().iterator();

						while (iter.hasNext()) {
							final Entry<PointInTime, Set<Tuple<M>>> entry = iter
									.next();

							// Whenever learning tuples decay, a new time
							// interval with its own set of learning tuples
							// start.
							final PointInTime endP = entry.getKey();

							// If endP is not after startP, old decaying tuple
							// were not be cleaned up correctly.
							assert endP.after(startP);

							if (endP.afterOrEquals(ts)) {
								/*
								 * When endP is after or equals to the start
								 * time of the arrived tuple, the last model
								 * interval of [startP, ts) is reached.
								 */
								createAndTransferNewModel(
										this.currentBufferingPointInTime, ts,
										metadata);

								if (endP.equals(ts)) {
									/*
									 * If endP is equals the start of time of
									 * the arrived tuple, we can (and need to!)
									 * remove this tuples because the following
									 * intervals do not need these tuples
									 * anymore.
									 */
									removeLearningTuplesFromLearners(entry
											.getValue());
									iter.remove();
								}
								break;
							} else {

								createAndTransferNewModel(
										this.currentBufferingPointInTime, ts,
										metadata);

								/*
								 * Remove the decaying tuples for the next
								 * interval.
								 */
								this.learner.removeLearningData(entry
										.getValue());
								iter.remove();

								// set the end of this interval to the start of
								// the next interval
								startP = endP;
							}
						}
					}

				}

				/*
				 * init a new learning tuple buffering set for the new tuples
				 * and add the new learning tuple
				 *
				 * We do not just clear this set because we passed it to the
				 * learner. I guess, the learner would be upset when we meddle
				 * with this set.
				 */
				this.newLearningTuples = new LinkedHashSet<>();
				this.newLearningTuples.add(object);
				this.currentBufferingPointInTime = ts;
				addToDecayingLearningTuples(object, te);
			}
		}
	}

	/**
	 * @param currentBufferingPointInTime2
	 * @param ts
	 * @param metadata
	 */
	private void createAndTransferNewModel(
			final PointInTime currentBufferingPointInTime,
			final PointInTime ts, final M metadata) {
		final long startOfModelLearning = System.currentTimeMillis();

		final RatingPredictor<Tuple<M>, M, U, I, P> model = this.learner
				.getModel(true);
		final long endOfModelLearning = System.currentTimeMillis();

		final RecommendationCandidates<U, I> recommCandModel = this.outputRecomCandObj ? this.learner
				.getRecommendationCandidatesModel() : null;

		final long endOfRecomModelLearning = System.currentTimeMillis();
		transferNewModel(model, recommCandModel, currentBufferingPointInTime,
				ts, metadata, endOfModelLearning - startOfModelLearning,
				endOfRecomModelLearning - endOfModelLearning);
	}

	/**
	 *
	 * @param decayingLearningTuples
	 */
	private void removeLearningTuplesFromLearners(
			final Set<Tuple<M>> decayingLearningTuples) {
		this.learner.removeLearningData(decayingLearningTuples);
		--this.noOfActiveLearningTuples;
		//
		// if (this.recommCandBuilder != null) {
		// this.recommCandBuilder.removeRatedItems(decayingLearningTuples);
		// }
	}

	/**
	 *
	 * @param newLearningTuples
	 */
	private void addLearningTuplesToLearners(
			final Set<Tuple<M>> newLearningTuples) {
		this.learner.addLearningData(newLearningTuples);
		++this.noOfActiveLearningTuples;
		//
		// if (this.recommCandBuilder != null) {
		// this.recommCandBuilder.addRatedItems(newLearningTuples);
		// }
	}

	/**
	 * Transfers the models.
	 *
	 * @param model
	 *            The model that should be transfered on port 0. If
	 *            <code>model</code> is <code>null</code>, a new
	 *            {@linkplain ConstantValueRatingPredictor} is transfered.
	 * @param recommendationCandidatesModel
	 *            The model that returns for each user a set of recommendation
	 *            candidates. It is transfered on port 1. If
	 *            <code>recommendationCandidatesModel</code> is
	 *            <code>null</code>, a new
	 *            {@linkplain ImmutableRecommendationCandidatesSet} is
	 *            transfered, that returns for each user an empty set of
	 *            recommendation candidates.
	 * @param startP
	 *            Start of the validity interval.
	 * @param endP
	 *            End of the validity interval.
	 * @param metadataCopy
	 *            A metadata object that is used for the model (after adjusting
	 *            start and end).
	 * @param recomCandModelLearningDuration
	 * @param modelLearningDuration
	 */
	private void transferNewModel(
			final RatingPredictor<Tuple<M>, M, U, I, P> model,
			final RecommendationCandidates<U, I> recommendationCandidatesModel,
			final PointInTime startP, final PointInTime endP,
			final M metadataCopy, final long modelLearningDuration,
			final long recomCandModelLearningDuration) {
		if (logger.isDebugEnabled()) {
			logger.debug("new model for time span: [" + startP + ", " + endP
					+ "): " + model);
		}

		this.modelTuple = new Tuple<M>(1, false);
		if (model != null) {
			modelTuple.setAttribute(0, model);
		} else {
			// transfer a constant value model that predicts always 0.
			modelTuple.setAttribute(0, new ConstantValueRatingPredictor<>(0.0));
		}
		modelTuple.setMetadata(metadataCopy);
		modelTuple.getMetadata().setStartAndEnd(startP, endP);
//		modelTuple
//				.setKeyValue("model learning duration", modelLearningDuration);
		if(!transferModelOnlyOnPunctuation) {
			transfer(modelTuple);
		}

		@SuppressWarnings("unchecked")
		final M metadataCopy2 = (M) metadataCopy.clone();
		this.recommCandTuple = new Tuple<M>(1, false);
		if (recommendationCandidatesModel != null) {
			recommCandTuple.setAttribute(0, recommendationCandidatesModel);
		} else {
			recommCandTuple.setAttribute(0,
					new ImmutableRecommendationCandidatesSet<>());
		}
		recommCandTuple.setMetadata(metadataCopy2);
		recommCandTuple.getMetadata().setStartAndEnd(startP, endP);
//		recommCandTuple.setKeyValue("recomm cand model learning",
//				recomCandModelLearningDuration);
		if(!transferModelOnlyOnPunctuation) {
			transfer(recommCandTuple, 1);
		}
	}

	/**
	 * @param object
	 */
	private void addToDecayingLearningTuples(final Tuple<M> object,
			final PointInTime te) {
		if (!te.isInfinite()) {
			synchronized (this.decayingLearningTuples) {
				if (this.decayingLearningTuples.containsKey(te)) {
					this.decayingLearningTuples.get(te).add(object);
				} else {
					final Set<Tuple<M>> set = new LinkedHashSet<>();
					set.add(object);
					this.decayingLearningTuples.put(te, set);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_close()
	 */
	@Override
	protected void process_close() {
		super.process_close();
		logger.info("process_close");
		this.currentBufferingPointInTime = PointInTime.getZeroTime();
		this.newLearningTuples = new LinkedHashSet<>();
		this.decayingLearningTuples = new TreeMap<>();
		this.learner.clear();
		// if (this.recommCandBuilder != null) {
		// this.recommCandBuilder.clear();
		// }
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ISink#processPunctuation
	 * (de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(final IPunctuation punctuation,
			final int port) {

		long currentSystemTime = System.currentTimeMillis();
		PointInTime systemPointInTime = new PointInTime(currentSystemTime);
		if(this.modelTuple!=null){
			this.modelTuple.getMetadata().setStartAndEnd(previousSystemPointInTimeOnPunctuation, systemPointInTime);
			transfer(this.modelTuple,0 );
		}
		if(this.recommCandTuple!=null){
			this.recommCandTuple.getMetadata().setStartAndEnd(previousSystemPointInTimeOnPunctuation, systemPointInTime);
			transfer(this.recommCandTuple, 1);
		}
		previousSystemPointInTimeOnPunctuation = systemPointInTime;
		sendPunctuation(Heartbeat.createNewHeartbeat(systemPointInTime), 2);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
	 * getName()
	 */
	@Override
	public String getName() {
		return this.learner.getName();
		// return super.getName();
	}

	/**
	 * @return the transferModelOnlyOnPunctuation
	 */
	public boolean isTransferModelOnlyOnPunctuation() {
		return transferModelOnlyOnPunctuation;
	}

	/**
	 * @param transferModelOnlyOnPunctuation the transferModelOnlyOnPunctuation to set
	 */
	public void setTransferModelOnlyOnPunctuation(boolean transferModelOnlyOnPunctuation) {
		this.transferModelOnlyOnPunctuation = transferModelOnlyOnPunctuation;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.
	 * IPhysicalOperatorKeyValueProvider#getKeyValues()
	 */
	@Override
	public Map<String, String> getKeyValues() {
		final Map<String, String> keyValues = new HashMap<>();
		keyValues.put("Tuples in Model",
				String.valueOf(this.noOfActiveLearningTuples));
		keyValues.putAll(this.learner.getInfo());
		keyValues.put("transferModelOnlyOnPunctuation", String.valueOf(transferModelOnlyOnPunctuation));
		return keyValues;
	}
}