/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.evaluation.physicaloperator;

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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.AccuracyMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.IEvaluationMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.MAEMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.MSEMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.PrecisionMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.RAEMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.RMSEMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.RRSEMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.RSEMetric;
import de.uniol.inf.is.odysseus.mining.evaluation.metric.RecallMetric;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Stephan Wessels
 * 
 */
public class ClassificationEvaluatePO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private final static String[] CLASSIFICATION_METRICS = {"ACCURACY", "ERROR", "RECALL", "TPR", "FNR", "PRECISION", "PPV", "FDR"};
	private final static String[] REGRESSION_METRICS = {"MSE", "RMSE", "MAE", "RAE", "RSE", "RRSE"};

	protected static Logger logger = LoggerFactory.getLogger(ClassificationEvaluatePO.class);	
	final private ITimeIntervalSweepArea<Tuple<M>> classifierSA;
	final private ITimeIntervalSweepArea<Tuple<M>> tupleSA;
	@SuppressWarnings("unchecked")
	private ITimeIntervalSweepArea<Tuple<M>> areas[] = new ITimeIntervalSweepArea[2];
	
	private int indexOfClassifier;
	private int indexOfClass;
	private double fadingFactor = 1.0;
	private SDFAttribute classAttribute;
	private Map<SDFAttribute, List<String>> nominals;
	private List<String> metricsList;
	private IEvaluationMetric[] metrics;

	public ClassificationEvaluatePO(int indexOfClassifier, int indexOfClass, 
				SDFAttribute classAttribute, Map<SDFAttribute, List<String>> nominals, List<String> metricsList, Double fadingFactor, 
				ITimeIntervalSweepArea<Tuple<M>> classifierSA, ITimeIntervalSweepArea<Tuple<M>> tupleSA) {
		this.indexOfClassifier = indexOfClassifier;
		this.indexOfClass = indexOfClass;
		this.classAttribute = classAttribute;
		this.nominals = nominals;
		this.metricsList = metricsList;
		this.fadingFactor = fadingFactor;
		this.checkAvailableMetrics(metricsList);
		this.classifierSA = classifierSA;
		this.tupleSA = tupleSA;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		
		initMetrics();
		
		tupleSA.clear();
		classifierSA.clear();
		areas[0] = classifierSA;
		areas[1] = tupleSA;
	}

	@Override
	protected void process_next(Tuple<M> tuple, int port) {
		synchronized (areas) {
			areas[port].insert(tuple);
			if(port == 0) {				
				//new classifier -> remove old and overlapping tuples
				tupleSA.extractElementsStartingBefore(tuple.getMetadata().getEnd());
				Iterator<Tuple<M>> qualifies = tupleSA.iterator();
				//if a new tuple already exists, test classifier on the first one
				if(qualifies.hasNext()) {
					classify(tuple, qualifies.next());
					//remove classificator after successful evaluation
					classifierSA.remove(tuple);
				}
			} else {
				//new tuple -> test it on old classifiers
				Iterator<Tuple<M>> qualifies = classifierSA.extractElementsBefore(tuple.getMetadata().getStart());
				while(qualifies.hasNext()) {
					classify(qualifies.next(), tuple);
				}
			}
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}


	private void classify(Tuple<M> classifierTuple, Tuple<M> toClassify) {
		//clazzify the tuple (predict class)
		Double clazz = clazzify(classifierTuple, toClassify);
		if(clazz == null) {
			//tuple could not be classified
			return;
		}
		Tuple<M> newtuple = classifierTuple;
		//calculate metrics and append them to the classifier
		for (IEvaluationMetric metric : metrics) {
			Object metricValue = metric.calculateMetric(toClassify.getAttribute(indexOfClass), clazz, fadingFactor);
			newtuple = newtuple.append(metricValue);
		}
		transfer(newtuple);
	}

	protected Double clazzify(Tuple<M> classifierTuple, Tuple<M> toClassify) {
		IClassifier<M> classifier = classifierTuple.getAttribute(indexOfClassifier);
		Object clazz = classifier.classify(toClassify);
		if (clazz == null) {
			logger.warn("value is unknown, so that the tuple could not be classified, tuple: " + toClassify);
			PointInTime min = PointInTime.min(classifierTuple.getMetadata().getStart(), toClassify.getMetadata().getStart());
			sendPunctuation(Heartbeat.createNewHeartbeat(min));
			return null;
		}
		return (Double) clazz;
	}
	
	private void checkAvailableMetrics(List<String> metricsList) {
		//better: use config-files to specify the available metrics 
		if(metricsList != null) {
			//check if numeric or discrete class -> regression or classification
			List<String> availableMetrics = this.classAttribute.getDatatype().isNumeric() ? Arrays.asList(REGRESSION_METRICS): Arrays.asList(CLASSIFICATION_METRICS);
			for (String metric : metricsList) {
				metric = metric.toUpperCase();
				if(!availableMetrics.contains(metric)) {
					throw new IllegalArgumentException("Wrong evaluation metric: " + metric + ". Choose from: " + availableMetrics.toString());
				}
			}
		}
	}
	
	private void initMetrics(){
		//initialize metric-array based on parameter
		metrics = new IEvaluationMetric[metricsList.size()];
		
		for (int i = 0; i < metricsList.size(); i++) {
			switch (metricsList.get(i).toUpperCase()) {
			case "ACCURACY": 
				metrics[i] = new AccuracyMetric(nominals.get(classAttribute));
				break;
			case "ERROR":
				metrics[i] = new AccuracyMetric(nominals.get(classAttribute), true);
				break;
			case "RECALL":
			case "TPR":
				metrics[i] = new RecallMetric(nominals.get(classAttribute));
				break;
			case "FNR":
				metrics[i] = new RecallMetric(nominals.get(classAttribute), true);
				break;
			case "PRECISION":
			case "PPV":
				metrics[i] = new PrecisionMetric(nominals.get(classAttribute));
				break;
			case "FDR":
				metrics[i] = new PrecisionMetric(nominals.get(classAttribute), true);
				break;
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
