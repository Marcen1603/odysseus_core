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

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.DataSourceManager;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.costmodel.operator.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PhysicalGraphWalker;
import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

/**
 * Repräsentiert das Kostenmodell nach Operatoreigenschaften.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorCostModel<T extends ISubscriber<T, ISubscription<T>> & ISubscribable<T, ISubscription<T>>> implements ICostModel<T> {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorCostModel.class);

	private IOperatorDetailCostAggregator<IPhysicalOperator> operatorAggregator = new OperatorDetailCostAggregator<IPhysicalOperator>();
	private IOperatorDetailCostAggregator<ILogicalOperator> logicalOperatorAggregator = new OperatorDetailCostAggregator<ILogicalOperator>();

	private final int processorCount;
	private final int schedulerThreadCount;
	private final long memory;

	private final CPUUsage cpuUsage = new CPUUsage();
	private final Runtime runtime = Runtime.getRuntime();

	/**
	 * Standardkonstruktor.
	 */
	public OperatorCostModel() {
		Runtime runtime = Runtime.getRuntime();
		processorCount = runtime.availableProcessors();
		schedulerThreadCount = OdysseusConfiguration.getInt("scheduler_simpleThreadScheduler_executorThreadsCount", 1);

		LOG.debug("Number of Processors available: {} ", processorCount);
		LOG.debug("Number of Scheduler Threads: {}", schedulerThreadCount);
		if (processorCount > schedulerThreadCount) {
			LOG.warn("Number of processors exceeds the number of threads used by simpleThreadScheduler. This can result in performance loss.");
		}

		memory = runtime.totalMemory();
		LOG.debug("Memory in bytes: {}", memory);

		cpuUsage.start();
	}

	@Override
	public ICost<T> getMaximumCost() {
		double mem = memory * OperatorCostModelCfg.getInstance().getMemHeadroom();
		double cpu = processorCount * OperatorCostModelCfg.getInstance().getCpuHeadroom();
		OperatorCost<T> max = new OperatorCost<T>(mem, cpu);
		return max;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICost<T> estimateCost(List<T> operators, final boolean onUpdate) {
		if (operators == null || operators.isEmpty()) {
			return new OperatorCost<T>(null, 0, 0);
		}

		final Map<SDFAttribute, IHistogram> baseHistograms = getBaseHistograms(operators);

		if (operators.get(0) instanceof IPhysicalOperator) {

			final Map<IPhysicalOperator, OperatorEstimation<IPhysicalOperator>> estimatedOperators = Maps.newHashMap();
			PhysicalGraphWalker walker = new PhysicalGraphWalker((List<IPhysicalOperator>) operators);
			walker.walk(new IOperatorWalker<IPhysicalOperator>() {

				@Override
				public void walk(IPhysicalOperator operator) {

					// get prev operators
					List<OperatorEstimation<?>> prevOperators = new ArrayList<OperatorEstimation<?>>();
					if (operator.isSink()) {
						ISink<?> operatorAsSink = (ISink<?>) operator;

						for (int i = 0; i < operatorAsSink.getSubscribedToSource().size(); i++) {
							PhysicalSubscription<?> subscription = operatorAsSink.getSubscribedToSource(i);
							IPhysicalOperator op = (IPhysicalOperator) subscription.getTarget();

							prevOperators.add(estimatedOperators.get(op));
						}
					}

					IOperatorEstimator<IPhysicalOperator> estimator = OperatorEstimatorFactory.getInstance().get(operator);
					OperatorEstimation<IPhysicalOperator> estimation = estimator.estimateOperator(operator, prevOperators, baseHistograms);

					boolean isRunning = determineIsRunning(operator);

					if (!onUpdate) {
						// don't count already running operators
						if (isRunning)
							estimation.setDetailCost(new OperatorDetailCost<IPhysicalOperator>(operator, 0, 0));
					} else {
						// don't count stopped operators
						if (!isRunning) {
							estimation.setDetailCost(new OperatorDetailCost<IPhysicalOperator>(operator, 0, 0));

						}
					}

					estimatedOperators.put(operator, estimation);
				}

			});

			// aggregate costs
			AggregatedCost aggCost = operatorAggregator.aggregate(estimatedOperators);
			return (ICost<T>) new OperatorCost<IPhysicalOperator>(estimatedOperators, aggCost.getMemCost(), aggCost.getCpuCost());

		} else {

			final Map<ILogicalOperator, OperatorEstimation<ILogicalOperator>> estimatedOperators = Maps.newHashMap();
			LogicalGraphWalker walker = new LogicalGraphWalker((List<ILogicalOperator>) operators);
			walker.walk(new IOperatorWalker<ILogicalOperator>() {

				@Override
				public void walk(ILogicalOperator operator) {

					// get prev operators
					List<OperatorEstimation<?>> prevOperators = new ArrayList<OperatorEstimation<?>>();
					for (int i = 0; i < operator.getSubscribedToSource().size(); i++) {
						LogicalSubscription subscription = operator.getSubscribedToSource(i);
						ILogicalOperator op = (ILogicalOperator) subscription.getTarget();

						prevOperators.add(estimatedOperators.get(op));
					}
					IOperatorEstimator<ILogicalOperator> estimator = OperatorEstimatorFactory.getInstance().get(operator);
					OperatorEstimation<ILogicalOperator> estimation = estimator.estimateOperator(operator, prevOperators, baseHistograms);

					boolean isRunning = determineIsRunning(operator);

					if (!onUpdate) {
						// don't count already running operators
						if (isRunning)
							estimation.setDetailCost(new OperatorDetailCost<ILogicalOperator>(operator, 0, 0));
					} else {
						// don't count stopped operators
						if (!isRunning) {
							estimation.setDetailCost(new OperatorDetailCost<ILogicalOperator>(operator, 0, 0));

						}
					}

					estimatedOperators.put(operator, estimation);
				}

			});

			// aggregate costs
			AggregatedCost aggCost = logicalOperatorAggregator.aggregate(estimatedOperators);
			return (ICost<T>) new OperatorCost<ILogicalOperator>(estimatedOperators, aggCost.getMemCost(), aggCost.getCpuCost());

		}

	}

	@Override
	public ICost<T> getZeroCost() {
		return new OperatorCost<T>(0, 0);
	}

	@Override
	public ICost<T> getOverallCost() {
		double mem = runtime.totalMemory() - runtime.freeMemory();
		return new OperatorCost<T>(mem, cpuUsage.getCpuMeanUsage());
	}

	// holt eine Liste der Histogramme der Attribute, die in der Anfrage
	// verarbeitet werden
	@SuppressWarnings("rawtypes")
	private Map<SDFAttribute, IHistogram> getBaseHistograms(List<T> physicalOperators) {
		Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();

		// Find relevant operators.
		// Relevant operators are Select and Join
		List<IPhysicalOperator> relevantOperators = new ArrayList<IPhysicalOperator>();
		for (int i = 0; i < physicalOperators.size(); i++) {
			Object op = physicalOperators.get(i);
			if (op instanceof SelectPO || op instanceof JoinTIPO || op instanceof AntiJoinTIPO)
				relevantOperators.add((IPhysicalOperator) op);
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
				SelectPO<?> selectPO = (SelectPO) op;
				List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(selectPO.getPredicate(), attributes);

				for (SDFAttribute a : attributes)
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a))
						relevantAttributes.add(a);

			} else if (op instanceof JoinTIPO) {
				JoinTIPO<?, ?> joinPO = (JoinTIPO) op;
				List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(joinPO.getPredicate(), attributes);

				for (SDFAttribute a : attributes)
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a))
						relevantAttributes.add(a);

			} else {
				AntiJoinTIPO<?, ?> antiJoin = (AntiJoinTIPO) op;
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

	private static boolean determineIsRunning(Object operator) {
		if (operator instanceof IPhysicalOperator) {
			IPhysicalOperator physicalOperator = (IPhysicalOperator) operator;
			for (IOperatorOwner owner : physicalOperator.getOwner()) {
				IPhysicalQuery query = (IPhysicalQuery) owner;
				if (query.isOpened()) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
}