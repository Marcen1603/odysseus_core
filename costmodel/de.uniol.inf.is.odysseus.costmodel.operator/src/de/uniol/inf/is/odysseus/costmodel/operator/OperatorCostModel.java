/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.DataSourceManager;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.GraphWalker;
import de.uniol.inf.is.odysseus.costmodel.operator.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

/**
 * Repräsentiert das Kostenmodell nach Operatoreigenschaften.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorCostModel implements ICostModel {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorCostModel.class);

	private IOperatorDetailCostAggregator operatorAggregator = new OperatorDetailCostAggregator();

	private final int processorCount;
	private final long memory;

	/**
	 * Standardkonstruktor.
	 */
	public OperatorCostModel() {
		Runtime runtime = Runtime.getRuntime();
		processorCount = runtime.availableProcessors();

		LOG.debug("Number of Processors available: {} ", processorCount);

		memory = runtime.totalMemory();
		LOG.debug("Memory in bytes: {}", memory);
	}

	@Override
	public ICost getMaximumCost() {
		// return new OperatorCost(memory, processorCount);
		return new OperatorCost(memory * OperatorCostModelCfg.getInstance().getMemHeadroom(), processorCount * OperatorCostModelCfg.getInstance().getCpuHeadroom());
	}

	@Override
	public ICost estimateCost(List<IPhysicalOperator> operators, final boolean onUpdate) {

		final Map<SDFAttribute, IHistogram> baseHistograms = getBaseHistograms(operators);
		final Map<IPhysicalOperator, OperatorEstimation> estimatedOperators = new HashMap<IPhysicalOperator, OperatorEstimation>();

		GraphWalker walker = new GraphWalker(operators);
		walker.walk(new IOperatorWalker() {

			@Override
			public void walk(IPhysicalOperator operator) {

				// get prev operators
				List<OperatorEstimation> prevOperators = new ArrayList<OperatorEstimation>();
				if (operator.isSink()) {
					ISink<?> operatorAsSink = (ISink<?>) operator;

					for (int i = 0; i < operatorAsSink.getSubscribedToSource().size(); i++) {
						PhysicalSubscription<?> subscription = operatorAsSink.getSubscribedToSource(i);
						IPhysicalOperator op = (IPhysicalOperator) subscription.getTarget();

						prevOperators.add(estimatedOperators.get(op));
					}
				}

				IOperatorEstimator<IPhysicalOperator> estimator = OperatorEstimatorFactory.getInstance().get(operator);
				OperatorEstimation estimation = estimator.estimateOperator(operator, prevOperators, baseHistograms);

				// check estimation
				if (!estimation.check()) {
					LOG.error("Estimation of {} with estimator {} not correct", operator, estimator.getClass());
					StandardOperatorEstimator<IPhysicalOperator> stdEstimator = (StandardOperatorEstimator<IPhysicalOperator>) OperatorEstimatorFactory.getInstance().get(null);
					OperatorEstimation stdEstimation = stdEstimator.estimateOperator(operator, prevOperators, baseHistograms);
					if (estimation.getHistograms() == null) {
						LOG.error("No histograms in Estimation!");
						estimation.setHistograms(stdEstimation.getHistograms());
					}
					if (estimation.getSelectivity() == null) {
						LOG.error("No selectivity in estimation!");
						estimation.setSelectivity(stdEstimation.getSelectivity());
					}
					if (estimation.getDataStream() == null) {
						LOG.error("No datastream in estimation!");
						estimation.setDataStream(stdEstimation.getDataStream());
					}
					if (estimation.getDetailCost() == null) {
						LOG.error("No detailcost in estimation!");
						estimation.setDetailCost(stdEstimation.getDetailCost());
					}
				}

				boolean isRunning = false;
				for (IOperatorOwner owner : operator.getOwner()) {
					IPhysicalQuery query = (IPhysicalQuery) owner;
					if (query.isOpened()) {
						isRunning = true;
						break;
					}
				}

				if (!onUpdate) {
					// don't count already running operators
					if (isRunning)
						estimation.setDetailCost(new OperatorDetailCost(operator, 0, 0));
				} else {
					// don't count stopped operators
					if (!isRunning) {
						estimation.setDetailCost(new OperatorDetailCost(operator, 0, 0));

					}
				}

				estimatedOperators.put(operator, estimation);
			}

		});

		// aggregate costs
		AggregatedCost aggCost = operatorAggregator.aggregate(estimatedOperators);

//		System.out.println();
//		for (IPhysicalOperator op : operators) {
//
//			OperatorEstimation estimation = estimatedOperators.get(op);
//			double s = estimation.getSelectivity();
//			double r = estimation.getDataStream().getDataRate();
//			double g = estimation.getDataStream().getIntervalLength();
//			double cpu = estimation.getDetailCost().getProcessorCost();
//			double mem = estimation.getDetailCost().getMemoryCost();
//
//			System.out.println(String.format("%-20s : s = %-8.6f, r = %-10.6f, g = %-10.6f, cpu = %-10.6f, mem = %-10.6f ", op.getClass().getSimpleName(), s, r, g, cpu, mem));
//		}
//
//		System.out.println("Aggregated: " + aggCost);

		return new OperatorCost(estimatedOperators, aggCost.getMemCost(), aggCost.getCpuCost());
	}

	@Override
	public ICost getZeroCost() {
		return new OperatorCost(0, 0);
	}

	// holt eine Liste der Histogramme der Attribute, die in der Anfrage
	// verarbeitet werden
	private Map<SDFAttribute, IHistogram> getBaseHistograms(List<IPhysicalOperator> physicalOperators) {
		Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();

		// Find relevant operators.
		// Relevant operators are Select and Join
		List<IPhysicalOperator> relevantOperators = new ArrayList<IPhysicalOperator>();
		for (IPhysicalOperator op : physicalOperators) {
			if (op instanceof SelectPO || op instanceof JoinTIPO || op instanceof AntiJoinTIPO)
				relevantOperators.add(op);
		}

		if (relevantOperators.isEmpty()) {
			LOG.info("No relevant operators for selectivity-estimation found. ");
			return histograms;
		}

		LOG.debug("Relevant operators are {}", relevantOperators);

		// find relevant attributes
		// relevant attributes are attributes which are used
		// in select- and join-operators
		List<SDFAttribute> relevantAttributes = new ArrayList<SDFAttribute>();
		for (IPhysicalOperator op : relevantOperators) {
			if (op instanceof SelectPO) {
				SelectPO<?> selectPO = (SelectPO<?>) op;
				List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(selectPO.getPredicate(), attributes);

				for (SDFAttribute a : attributes)
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a))
						relevantAttributes.add(a);

			} else if (op instanceof JoinTIPO) {
				JoinTIPO<?, ?> joinPO = (JoinTIPO<?, ?>) op;
				List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(joinPO.getPredicate(), attributes);

				for (SDFAttribute a : attributes)
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a))
						relevantAttributes.add(a);

			} else {
				AntiJoinTIPO<?, ?> antiJoin = (AntiJoinTIPO<?, ?>) op;
				for (SDFAttribute a : antiJoin.getOutputSchema())
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a))
						relevantAttributes.add(a);
			}
		}
		if (relevantAttributes.isEmpty()) {
			LOG.info("No relevant attributes found. Selectivity-estimation finished");
			return histograms;
		}
		LOG.debug("Relevant attributes are {}", relevantAttributes);

		// getting histograms
		for (SDFAttribute attribute : relevantAttributes) {

			IHistogram hist = DataSourceManager.getInstance().getHistogram(attribute);
			if (hist != null) {
				histograms.put(attribute, hist);
				LOG.debug("Got histogram for attribute {} ", attribute);
			} else {
				LOG.warn("No histogram for attribute {}", attribute);
			}
		}

		return histograms;
	}

	// sucht aus einem prädikat alle Attribute raus
	private void fillWithAttributes(IPredicate<?> predicate, List<SDFAttribute> attributes) {
		if (predicate instanceof IRelationalPredicate) {
			IRelationalPredicate pred = (IRelationalPredicate) predicate;
			List<SDFAttribute> attributeList = pred.getAttributes();

			for (SDFAttribute attribute : attributeList) {
				if (!attributes.contains(attribute))
					attributes.add(attribute);
			}
		} else if (predicate instanceof ComplexPredicate) {
			ComplexPredicate<?> comp = (ComplexPredicate<?>) predicate;
			fillWithAttributes(comp.getLeft(), attributes);
			fillWithAttributes(comp.getRight(), attributes);
		} else {
			LOG.warn("Unknown type of predicate : {}", predicate.getClass());
		}
	}

}
