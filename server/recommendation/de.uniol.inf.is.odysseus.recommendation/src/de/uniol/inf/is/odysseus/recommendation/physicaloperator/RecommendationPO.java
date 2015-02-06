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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.recommendation.recommender.Recommender;

/**
 * @author Cornelius Ludmann
 *
 */
public class RecommendationPO<M extends ITimeInterval> extends
AbstractPipe<Tuple<M>, Tuple<M>> {

	protected static Logger logger = LoggerFactory
			.getLogger(RecommendationPO.class);

	private final int recommenderAttributeIndex, userAttibuteIndex;
	/**
	 * Number of recommendations. -1 means all items.
	 */
	private int numberOfRecommendations = -1;

	// TODO: oneModel
	boolean oneModel = true;

	@SuppressWarnings("unchecked")
	private final DefaultTISweepArea<Tuple<M>> areas[] = new DefaultTISweepArea[2];
	protected IMetadataMergeFunction<M> metadataMerge;
	protected ITransferArea<Tuple<M>, Tuple<M>> transferFunction;

	private int elementsPort = 0;
	private int modelPort = 1;

	public RecommendationPO(final int modelPort,
			final int recommenderAttributeIndex, final int elementsPort,
			final int userAttibuteIndex, final int numberOfRecommendations,
			final IMetadataMergeFunction<M> metadataMerge,
			final ITransferArea<Tuple<M>, Tuple<M>> transferFunction) {
		this.modelPort = modelPort;
		this.elementsPort = elementsPort;
		this.userAttibuteIndex = userAttibuteIndex;
		this.recommenderAttributeIndex = recommenderAttributeIndex;
		this.numberOfRecommendations = numberOfRecommendations;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
	}

	/**
	 * Copy constructor.
	 *
	 * @param recommendationPO
	 */
	public RecommendationPO(final RecommendationPO<M> recommendationPO) {
		this.modelPort = recommendationPO.modelPort;
		this.elementsPort = recommendationPO.elementsPort;
		this.userAttibuteIndex = recommendationPO.userAttibuteIndex;
		this.recommenderAttributeIndex = recommendationPO.recommenderAttributeIndex;
		this.numberOfRecommendations = recommendationPO.numberOfRecommendations;
		this.metadataMerge = recommendationPO.metadataMerge;
		this.transferFunction = recommendationPO.transferFunction;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		if (areas[elementsPort] == null) {
			areas[elementsPort] = new DefaultTISweepArea<Tuple<M>>();
		}
		if (areas[modelPort] == null) {
			areas[modelPort] = new DefaultTISweepArea<Tuple<M>>();
		}
		areas[elementsPort].clear();
		areas[modelPort].clear();

		this.metadataMerge.init();
		this.transferFunction.init(this, getSubscribedToSource().size());
	}

	@Override
	protected void process_next(final Tuple<M> tuple, final int port) {
		synchronized (areas) {

			if (oneModel && port == modelPort) {
				areas[modelPort].clear();
			}
			areas[port].insert(tuple);
			final int other = (port + 1) % 2;
			areas[other].purgeElements(tuple, Order.LeftRight);
			// areas[other].purgeElementsBefore(tuple.getMetadata().getStart());
			// System.out.println("----------------------------------");
			// System.out.println("IN "+port+": "+tuple);
			// System.OUT.PRINTLN("PORT 0: "+AREAS[0].SIZE()+" TI: "+AREAS[0].GETMINTS()+" - "+AREAS[0].GETMAXTS());
			// System.out.println("PORT 1: "+areas[1].size()+" TI: "+areas[1].getMinTs()+" - "+areas[1].getMaxTs());
			final List<Tuple<M>> qualifies = areas[other]
					.queryOverlapsAsList(tuple.getMetadata());
			recommend(tuple, port, qualifies);
		}
	}

	@Override
	public void processPunctuation(final IPunctuation punctuation,
			final int port) {
		sendPunctuation(punctuation);
	}

	private void recommend(final Tuple<M> tuple, final int port,
			final List<Tuple<M>> qualifies) {
		if (logger.isDebugEnabled()) {
			logger.debug("RECOMMEND: port: " + port + "  no of tuples: "
					+ qualifies.size());
		}
		for (final Tuple<M> qualified : qualifies) {
			if (port == 0) {
				classifyAndTransfer(qualified, tuple);
			} else {
				classifyAndTransfer(tuple, qualified);
			}
		}
	}

	private void classifyAndTransfer(final Tuple<M> modelTuple,
			final Tuple<M> elementTuple) {
		final Recommender recommender = modelTuple
				.getAttribute(recommenderAttributeIndex);
		final long tillLearn = System.nanoTime();
		Map<Object, Double> recommendations = null;
		if (numberOfRecommendations < 1) {
			recommendations = recommender.recommend(elementTuple
					.getAttribute(userAttibuteIndex));
		} else {
			recommendations = recommender.recommendTopN(
					elementTuple.getAttribute(userAttibuteIndex),
					numberOfRecommendations);
		}

		if (recommendations == null) {
			logger.warn("could not calculate recommendations, tuple: "
					+ elementTuple);
			final PointInTime min = PointInTime.min(modelTuple.getMetadata()
					.getStart(), elementTuple.getMetadata().getStart());
			sendPunctuation(Heartbeat.createNewHeartbeat(min));
			return;
		}

		if (recommendations.size() == 0) {
			logger.warn("no recommendations");
			final PointInTime min = PointInTime.min(modelTuple.getMetadata()
					.getStart(), elementTuple.getMetadata().getStart());
			sendPunctuation(Heartbeat.createNewHeartbeat(min));
			return;
		}

		final long afterLearn = System.nanoTime();

		final M metadata = this.metadataMerge.mergeMetadata(
				elementTuple.getMetadata(), modelTuple.getMetadata());

		final List<Tuple<M>> tuples = createTuples(elementTuple,
				recommendations, metadata, tillLearn, afterLearn);

		transfer(tuples);
	}

	/**
	 * <p>
	 * Creates tuples of the predicted ratings.
	 *
	 * <p>
	 * The items list and predicted ratings list need to have the same size (and
	 * of course the same order).
	 *
	 * @param tuple
	 *            The input tuple with the user ID.
	 * @param afterLearn
	 * @param tillLearn
	 * @param items
	 *            The item IDs.
	 * @param predictedRatings
	 *            The predicted item IDs.
	 * @return
	 */
	private List<Tuple<M>> createTuples(final Tuple<M> tuple,
			final Map<Object, Double> recommendations, final M metadata,
			final long tillLearn, final long afterLearn) {

		final List<Tuple<M>> tuples = new LinkedList<Tuple<M>>();
		for (final Object item : recommendations.keySet()) {
			Tuple<M> t = tuple.append(item);
			t = t.append(recommendations.get(item));
			t.setMetadata(metadata);
			t.setMetadata("LATENCY_BEFORE", tillLearn);
			t.setMetadata("LATENCY_AFTER", afterLearn);
			tuples.add(t);
		}

		return tuples;
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new RecommendationPO<M>(this);
	}

}
