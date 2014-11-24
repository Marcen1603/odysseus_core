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
package de.uniol.inf.is.odysseus.recommendation.evaluation.physicaloperator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.IEvaluationMetric;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.MAEMetric;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.MSEMetric;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.RAEMetric;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.RMSEMetric;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.RRSEMetric;
import de.uniol.inf.is.odysseus.recommendation.evaluation.metric.RSEMetric;
import de.uniol.inf.is.odysseus.recommendation.recommender.Recommender;

/**
 * @author Stephan Wessels
 * 
 */
public class RecommendationEvaluatePO<M extends ITimeInterval> extends
AbstractPipe<Tuple<M>, Tuple<M>> {
	
	private final static String[] REGRESSION_METRICS = {"MSE", "RMSE", "MAE", "RAE", "RSE", "RRSE"};

	protected static Logger logger = LoggerFactory
			.getLogger(RecommendationEvaluatePO.class);

	private int recommenderAttributeIndex, userAttributeIndex, itemAttributeIndex, ratingAttributeIndex;
	
	private double fadingFactor = 1.0;
	private List<String> metricsList;
	private IEvaluationMetric[] metrics;

	@SuppressWarnings("unchecked")
	private final DefaultTISweepArea<Tuple<M>> areas[] = new DefaultTISweepArea[2];

	public RecommendationEvaluatePO(
			final int recommenderAttributeIndex, final int userAttributeIndex,
			final int itemAttributeIndex, final int ratingAttributeIndex,
			final List<String> metricsList, final double fadingFactor) {

		this.userAttributeIndex = userAttributeIndex;
		this.itemAttributeIndex = itemAttributeIndex;
		this.ratingAttributeIndex = ratingAttributeIndex;
		this.recommenderAttributeIndex = recommenderAttributeIndex;
		this.metricsList = metricsList;
		this.fadingFactor = fadingFactor;
		this.checkAvailableMetrics(metricsList);
	}

	public RecommendationEvaluatePO(final RecommendationEvaluatePO<M> recommendationEvaluatePO) {
		this.userAttributeIndex = recommendationEvaluatePO.userAttributeIndex;
		this.itemAttributeIndex = recommendationEvaluatePO.itemAttributeIndex;
		this.ratingAttributeIndex = recommendationEvaluatePO.ratingAttributeIndex;
		this.recommenderAttributeIndex = recommendationEvaluatePO.recommenderAttributeIndex;
		this.metricsList = recommendationEvaluatePO.metricsList;
		this.fadingFactor = recommendationEvaluatePO.fadingFactor;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		
		initMetrics();
		
		areas[0] = new DefaultTISweepArea<Tuple<M>>();
		areas[1] = new DefaultTISweepArea<Tuple<M>>();
		
		areas[0].clear();
		areas[1].clear();
	}

	@Override
	protected void process_next(final Tuple<M> tuple, final int port) {
		synchronized (areas) {
			
			areas[port].insert(tuple);
			if (port == 0) {
				//new recommender -> remove old and overlapping tuples
				areas[1].extractElementsStartingBefore(tuple.getMetadata().getEnd());
				Iterator<Tuple<M>> qualifies = areas[1].iterator();
				//if a new tuple already exists, test recommender on the first one
				if(qualifies.hasNext()) {
					testRecommendations(tuple, qualifies.next());
					//remove recommender after successful evaluation
					areas[0].remove(tuple);
				}
			} else {
				//new tuple -> test it on old recommenders
				Iterator<Tuple<M>> qualifies = areas[0].extractElementsBefore(tuple.getMetadata().getStart());
				while(qualifies.hasNext()) {
					testRecommendations(qualifies.next(), tuple);
				}
			}
		}
	}
	
	private void testRecommendations(final Tuple<M> recommenderTuple, final Tuple<M> toPredict) {
		//get predicted rating for the tuple
		Double recommendation = predictRating(recommenderTuple, toPredict);
		if(recommendation == null) {
			//if no rating could be predicted
			//recommendation = 0.0;
			return;
		}
		Tuple<M> newtuple = recommenderTuple;
		//calculate metrics and append them to the recommender
		for (IEvaluationMetric metric : metrics) {
			Object metricValue = metric.calculateMetric(toPredict.getAttribute(ratingAttributeIndex), recommendation, fadingFactor);
			newtuple = newtuple.append(metricValue);
		}
		transfer(newtuple);

	}
	
	private Double predictRating(final Tuple<M> recommenderTuple, final Tuple<M> ratingTuple) {
		Recommender recommender = recommenderTuple.getAttribute(recommenderAttributeIndex);
		//get predicted ratings for the user in ratingTuple
		Map<Object, Double> recommendations = recommender.recommend(ratingTuple.getAttribute(userAttributeIndex));
		if (recommendations == null) {
			logger.warn("could not calculate recommendations, tuple: "
					+ ratingTuple);
			final PointInTime min = PointInTime.min(recommenderTuple.getMetadata()
					.getStart(), ratingTuple.getMetadata().getStart());
			sendPunctuation(Heartbeat.createNewHeartbeat(min));
			return null;
		}
		if (recommendations.size() == 0) {
			logger.warn("no recommendations");
			final PointInTime min = PointInTime.min(recommenderTuple.getMetadata()
					.getStart(), ratingTuple.getMetadata().getStart());
			sendPunctuation(Heartbeat.createNewHeartbeat(min));
			return null;
		}
		//return predicted user-rating of the item in ratingTuple
		
		if(!recommendations.containsKey(ratingTuple.getAttribute(itemAttributeIndex))) {
			//item is new
			if(recommendations.isEmpty()) {
				//no items -> no recommendation
				return null;
			}
			//for unknown items predict the mean of all itemRatings
			//better solution: predict userBias + avgRatings (new method in Recommender?)
			double ratings = 0;
			for(Double rating : recommendations.values()) {
				ratings += rating;
			}
			return ratings / recommendations.values().size();
		}
		return recommendations.get(ratingTuple.getAttribute(itemAttributeIndex));

	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new RecommendationEvaluatePO<M>(this);
	}
	

	private void checkAvailableMetrics(List<String> metricsList) {
		if(metricsList != null) {
			List<String> availableMetrics = Arrays.asList(REGRESSION_METRICS);
			for (String metric : metricsList) {
				metric = metric.toUpperCase();
				if(!availableMetrics.contains(metric)) {
					throw new IllegalArgumentException("Wrong evaluation metric: " + metric + ". Choose from: " + availableMetrics.toString());
				}
			}
		}
	}
	
	private void initMetrics(){
		//initialize metric-array
		metrics = new IEvaluationMetric[metricsList.size()];
		
		for (int i = 0; i < metricsList.size(); i++) {
			switch (metricsList.get(i).toUpperCase()) {
			case "MAE":
				metrics[i] = new MAEMetric();
				break;
			case "MSE":
				metrics[i] = new MSEMetric();
				break;
			case "RMSE":
				metrics[i] = new RMSEMetric();
				break;
			case "RAE":
				metrics[i] = new RAEMetric();
				break;
			case "RSE":
				metrics[i] = new RSEMetric();
				break;
			case "RRSE":
				metrics[i] = new RRSEMetric();
				break;
			}
		}
	}

}
