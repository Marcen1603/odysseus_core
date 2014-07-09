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

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.recommender.Recommender;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;

/**
 * @author Cornelius Ludmann
 * 
 */
public class RecommendationLearnPO<M extends ITimeInterval> extends
AbstractPipe<Tuple<M>, Tuple<M>> {

	protected static Logger logger = LoggerFactory
			.getLogger(RecommendationLearnPO.class);

	private final DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private final FastArrayList<PointInTime> points = new FastArrayList<PointInTime>();

	/**
	 * The learner.
	 */
	private final RecommendationLearner<M> learner;

	/**
	 * The learner that should be used by this PO.
	 * 
	 * @param learner
	 *            The learner that should be used.
	 */
	public RecommendationLearnPO(final RecommendationLearner<M> learner) {
		if (learner == null) {
			throw new NullPointerException("learner needs to be not null");
		}
		this.learner = learner;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param recommendationLearnPO
	 */
	public RecommendationLearnPO(
			final RecommendationLearnPO<M> recommendationLearnPO) {
		this.learner = recommendationLearnPO.learner;
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
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone
	 * ()
	 */
	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new RecommendationLearnPO<M>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(final Tuple<M> object, final int port) {
		if (!this.points.contains(object.getMetadata().getStart())) {
			this.points.add(object.getMetadata().getStart());
		}
		if (!this.points.contains(object.getMetadata().getEnd())) {
			this.points.add(object.getMetadata().getEnd());
		}
		Collections.sort(this.points);
		// we append before, so we only need a "simple" clone
		sweepArea.insert(object);
		int removeTill = 0;
		for (int i = 1; i < this.points.size(); i++) {
			final PointInTime endP = this.points.get(i);
			final PointInTime startP = this.points.get(i - 1);
			if (endP.beforeOrEquals(object.getMetadata().getStart())) {
				synchronized (this.sweepArea) {
					final TimeInterval ti = new TimeInterval(startP, endP);
					final List<Tuple<M>> qualifies = this.sweepArea
							.queryOverlapsAsList(ti);

					final long tillLearn = System.nanoTime();

					final Recommender recommender = learner
							.createRecommender(qualifies);
					if (recommender != null) {

						final long afterLearn = System.nanoTime();

						final M metadata = (M) object.getMetadata().clone();
						final Tuple<M> newTuple = new Tuple<M>(1, false);
						newTuple.setAttribute(0, recommender);
						newTuple.setMetadata(metadata);
						newTuple.getMetadata().setStartAndEnd(startP, endP);
						// ((ILatency)newTuple.getMetadata()).setLatencyStart(start);
						// ((ILatency)newTuple.getMetadata()).setLatencyEnd(end);
						newTuple.setMetadata("LATENCY_BEFORE", tillLearn);
						newTuple.setMetadata("LATENCY_AFTER", afterLearn);
						transfer(newTuple);
					}
					// System.out.println("TRANSFER: " +
					// (System.currentTimeMillis() - time - duration));
					removeTill = i;
					sendPunctuation(Heartbeat.createNewHeartbeat(startP));
				}
			} else {
				break;
			}
		}
		if (removeTill != 0) {
			this.points.removeRange(0, removeTill);
			sweepArea.purgeElementsBefore(object.getMetadata().getStart());
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
		sweepArea.clear();
		points.clear();
	}
}
