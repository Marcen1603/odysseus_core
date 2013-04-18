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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
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

	private static final IOperatorDetailCostAggregator<IPhysicalOperator> AGGREGATOR = new OperatorDetailCostAggregator<IPhysicalOperator>();
	private static final IOperatorDetailCostAggregator<ILogicalOperator> LOGICAL_AGGREGATOR = new OperatorDetailCostAggregator<ILogicalOperator>();

	private final int processorCount;
	private final int schedulerThreadCount;
	private final long memory;

	private final CPUUsage cpuUsage = new CPUUsage();
	private final Runtime runtime = Runtime.getRuntime();

	private boolean useHistograms = true;

	/**
	 * Standardkonstruktor.
	 */
	public OperatorCostModel() {
		final Runtime runtime = Runtime.getRuntime();
		processorCount = runtime.availableProcessors();
		schedulerThreadCount = OdysseusConfiguration.getInt("scheduler_simpleThreadScheduler_executorThreadsCount", 1);
		useHistograms = OdysseusConfiguration.getBoolean("ac_operator_useHistograms", true);

		LOG.debug("Number of Processors available: {} ", processorCount);
		LOG.debug("Number of Scheduler Threads: {}", schedulerThreadCount);
		if (processorCount > schedulerThreadCount) {
			LOG.warn("Number of processors exceeds the number of threads used by simpleThreadScheduler. This can result in performance loss.");
		}

		memory = runtime.totalMemory();
		LOG.debug("Memory in bytes: {}", memory);

		cpuUsage.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICost<T> estimateCost(List<T> operators, final boolean onUpdate) {
		if (operators == null || operators.isEmpty()) {
			return new OperatorCost<T>(null, 0, 0);
		}

		final Map<SDFAttribute, IHistogram> baseHistograms = isUseHistograms() ? getBaseHistograms(operators) : Maps.<SDFAttribute, IHistogram> newHashMap();

		if (operators.get(0) instanceof IPhysicalOperator) {
			return (ICost<T>) estimatePhysical((List<IPhysicalOperator>) operators, onUpdate, baseHistograms);
		}

		return (ICost<T>) estimateLogical((List<ILogicalOperator>) operators, onUpdate, baseHistograms);
	}

	@Override
	public ICost<T> getMaximumCost() {
		final double mem = memory * OperatorCostModelCfg.getInstance().getMemHeadroom();
		final double cpu = processorCount * OperatorCostModelCfg.getInstance().getCpuHeadroom();
		final OperatorCost<T> max = new OperatorCost<T>(mem, cpu);
		return max;
	}

	@Override
	public ICost<T> getOverallCost() {
		final double mem = runtime.totalMemory() - runtime.freeMemory();
		return new OperatorCost<T>(mem, cpuUsage.getCpuMeanUsage());
	}

	@Override
	public ICost<T> getZeroCost() {
		return new OperatorCost<T>(0, 0);
	}

	public boolean isUseHistograms() {
		return useHistograms;
	}

	public void setUseHistograms(boolean useHistograms) {
		this.useHistograms = useHistograms;
	}

	private static boolean determineIsRunning(Object operator) {
		if (operator instanceof IPhysicalOperator) {
			final IPhysicalOperator physicalOperator = (IPhysicalOperator) operator;
			for (final IOperatorOwner owner : physicalOperator.getOwner()) {
				final IPhysicalQuery query = (IPhysicalQuery) owner;
				if (query.isOpened()) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	private static ICost<ILogicalOperator> estimateLogical(List<ILogicalOperator> operators, final boolean onUpdate, final Map<SDFAttribute, IHistogram> baseHistograms) {
		final Map<ILogicalOperator, OperatorEstimation<ILogicalOperator>> estimatedOperators = Maps.newHashMap();
		final LogicalGraphWalker walker = new LogicalGraphWalker(operators);
		walker.walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {

				// get prev operators
				final List<OperatorEstimation<?>> prevOperators = new ArrayList<OperatorEstimation<?>>();
				for (int i = 0; i < operator.getSubscribedToSource().size(); i++) {
					final LogicalSubscription subscription = operator.getSubscribedToSource(i);
					final ILogicalOperator op = subscription.getTarget();

					prevOperators.add(estimatedOperators.get(op));
				}
				final IOperatorEstimator<ILogicalOperator> estimator = OperatorEstimatorFactory.getInstance().get(operator);
				try {
					final OperatorEstimation<ILogicalOperator> estimation = estimator.estimateOperator(operator, prevOperators, baseHistograms);

					final boolean isRunning = determineIsRunning(operator);

					if (!onUpdate) {
						// don't count already running operators
						if (isRunning) {
							estimation.setDetailCost(new OperatorDetailCost<ILogicalOperator>(operator, 0, 0));
						}
					} else {
						// don't count stopped operators
						if (!isRunning) {
							estimation.setDetailCost(new OperatorDetailCost<ILogicalOperator>(operator, 0, 0));

						}
					}

					estimatedOperators.put(operator, estimation);
				} catch (final Throwable t) {
					LOG.error("Could not estimate costs for logical operator {}", operator, t);
				}
			}

		});

		// aggregate costs
		final AggregatedCost aggCost = LOGICAL_AGGREGATOR.aggregate(estimatedOperators);
		return new OperatorCost<ILogicalOperator>(estimatedOperators, aggCost.getMemCost(), aggCost.getCpuCost());
	}

	private static ICost<IPhysicalOperator> estimatePhysical(List<IPhysicalOperator> operators, final boolean onUpdate, final Map<SDFAttribute, IHistogram> baseHistograms) {
		final Map<IPhysicalOperator, OperatorEstimation<IPhysicalOperator>> estimatedOperators = Maps.newHashMap();
		final PhysicalGraphWalker walker = new PhysicalGraphWalker(operators);
		walker.walk(new IOperatorWalker<IPhysicalOperator>() {

			@Override
			public void walk(IPhysicalOperator operator) {

				// get prev operators
				final List<OperatorEstimation<?>> prevOperators = new ArrayList<OperatorEstimation<?>>();
				if (operator.isSink()) {
					final ISink<?> operatorAsSink = (ISink<?>) operator;

					for (int i = 0; i < operatorAsSink.getSubscribedToSource().size(); i++) {
						final PhysicalSubscription<?> subscription = operatorAsSink.getSubscribedToSource(i);
						final IPhysicalOperator op = (IPhysicalOperator) subscription.getTarget();

						prevOperators.add(estimatedOperators.get(op));
					}
				}

				final IOperatorEstimator<IPhysicalOperator> estimator = OperatorEstimatorFactory.getInstance().get(operator);
				try {
					final OperatorEstimation<IPhysicalOperator> estimation = estimator.estimateOperator(operator, prevOperators, baseHistograms);

					final boolean isRunning = determineIsRunning(operator);

					if (!onUpdate) {
						// don't count already running operators
						if (isRunning) {
							estimation.setDetailCost(new OperatorDetailCost<IPhysicalOperator>(operator, 0, 0));
						}
					} else {
						// don't count stopped operators
						if (!isRunning) {
							estimation.setDetailCost(new OperatorDetailCost<IPhysicalOperator>(operator, 0, 0));

						}
					}

					estimatedOperators.put(operator, estimation);
				} catch (final Throwable t) {
					LOG.error("Could not estimate costs for physical operator {}", operator, t);
				}
			}

		});

		// aggregate costs
		final AggregatedCost aggCost = AGGREGATOR.aggregate(estimatedOperators);
		return new OperatorCost<IPhysicalOperator>(estimatedOperators, aggCost.getMemCost(), aggCost.getCpuCost());
	}

	// sucht aus einem pr�dikat alle Attribute raus
	private static void fillWithAttributes(IPredicate<?> predicate, List<SDFAttribute> attributes) {
		if (predicate instanceof IRelationalPredicate) {
			final IRelationalPredicate pred = (IRelationalPredicate) predicate;
			final List<SDFAttribute> attributeList = pred.getAttributes();

			for (final SDFAttribute attribute : attributeList) {
				if (!attributes.contains(attribute)) {
					attributes.add(attribute);
				}
			}
		} else if (predicate instanceof ComplexPredicate) {
			final ComplexPredicate<?> comp = (ComplexPredicate<?>) predicate;
			fillWithAttributes(comp.getLeft(), attributes);
			fillWithAttributes(comp.getRight(), attributes);
		} else {
			LOG.warn("Unknown type of predicate : {}", predicate.getClass());
		}
	}

	// holt eine Liste der Histogramme der Attribute, die in der Anfrage
	// verarbeitet werden
	private static <T> Map<SDFAttribute, IHistogram> getBaseHistograms(List<T> operators) {
		final Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();
		
		List<SDFAttribute> relevantAttributes = new ArrayList<SDFAttribute>();
		
		if(operators.get(0) instanceof IPhysicalOperator) {
			LOG.debug("Fetching relevant attributes for physical operators");
			relevantAttributes = getRelevantAttributesForBaseHistogramsPhysical(operators);
		} else if(operators.get(0) instanceof ILogicalOperator) {
			LOG.debug("Fetching relevant attributes for logical operators");
			relevantAttributes = getRelevantAttributesForBaseHistogramsLogical(operators);
		}
		
		if (relevantAttributes.isEmpty()) {
			LOG.info("No relevant attributes found. Selectivity-estimation finished");
			return histograms;
		}
		LOG.debug("Relevant attributes are {}", relevantAttributes);

		// getting histograms
		for (final SDFAttribute attribute : relevantAttributes) {

			final IHistogram hist = DataSourceManager.getInstance().getHistogram(attribute);
			if (hist != null) {
				histograms.put(attribute, hist);
				LOG.debug("Got histogram for attribute {} ", attribute);
			} else {
				LOG.warn("No histogram for attribute {}", attribute);
			}
		}

		return histograms;
	}
	
	@SuppressWarnings("rawtypes")
	private static <T> List<SDFAttribute> getRelevantAttributesForBaseHistogramsPhysical(List<T> physicalOperators) {
		// Find relevant operators.
		// Relevant operators are Select and Join
		final List<IPhysicalOperator> relevantOperators = new ArrayList<IPhysicalOperator>();
		for (int i = 0; i < physicalOperators.size(); i++) {
			final Object op = physicalOperators.get(i);
			if (op instanceof SelectPO || op instanceof JoinTIPO
					|| op instanceof AntiJoinTIPO) {
				relevantOperators.add((IPhysicalOperator) op);
			}
		}


		final List<SDFAttribute> relevantAttributes = new ArrayList<SDFAttribute>();
		
		if (relevantOperators.isEmpty()) {
			LOG.info("No relevant operators for selectivity-estimation found. ");
			return relevantAttributes;
		}

		LOG.debug("Relevant operators are {}", relevantOperators);

		// find relevant attributes
		// relevant attributes are attributes which are used
		// in select- and join-operators
		for (final IPhysicalOperator op : relevantOperators) {
			if (op instanceof SelectPO) {
				final SelectPO<?> selectPO = (SelectPO) op;
				final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(selectPO.getPredicate(), attributes);

				for (final SDFAttribute a : attributes) {
					if (a.getDatatype().isNumeric()
							&& !relevantAttributes.contains(a)) {
						relevantAttributes.add(a);
					}
				}

			} else if (op instanceof JoinTIPO) {
				final JoinTIPO<?, ?> joinPO = (JoinTIPO) op;
				final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(joinPO.getPredicate(), attributes);

				for (final SDFAttribute a : attributes) {
					if (a.getDatatype().isNumeric()
							&& !relevantAttributes.contains(a)) {
						relevantAttributes.add(a);
					}
				}

			} else {
				final AntiJoinTIPO<?, ?> antiJoin = (AntiJoinTIPO) op;
				for (final SDFAttribute a : antiJoin.getOutputSchema()) {
					if (a.getDatatype().isNumeric()
							&& !relevantAttributes.contains(a)) {
						relevantAttributes.add(a);
					}
				}
			}
		}
		return relevantAttributes;
	}
	
	private static <T> List<SDFAttribute> getRelevantAttributesForBaseHistogramsLogical(List<T> logicalOperators) {
		// Find relevant operators.
		// Relevant operators are Select and Join
		final List<ILogicalOperator> relevantOperators = new ArrayList<ILogicalOperator>();
		for (int i = 0; i < logicalOperators.size(); i++) {
			final Object op = logicalOperators.get(i);
			if (op instanceof SelectAO || op instanceof JoinAO) {
				relevantOperators.add((ILogicalOperator) op);
			}
		}
		
		final List<SDFAttribute> relevantAttributes = new ArrayList<SDFAttribute>();

		if (relevantOperators.isEmpty()) {
			LOG.info("No relevant operators for selectivity-estimation found. ");
			return relevantAttributes;
		}

		LOG.debug("Relevant operators are {}", relevantOperators);

		// find relevant attributes
		// relevant attributes are attributes which are used
		// in select- and join-operators
		for (final ILogicalOperator op : relevantOperators) {
			if (op instanceof SelectAO) {
				final SelectAO selectAO = (SelectAO) op;
				final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(selectAO.getPredicate(), attributes);

				for (final SDFAttribute a : attributes) {
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a)) {
						relevantAttributes.add(a);
					}
				}

			} else if (op instanceof JoinAO) {
				final JoinAO joinAO = (JoinAO) op;
				final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
				fillWithAttributes(joinAO.getPredicate(), attributes);

				for (final SDFAttribute a : attributes) {
					if (a.getDatatype().isNumeric() && !relevantAttributes.contains(a)) {
						relevantAttributes.add(a);
					}
				}

			}
		}
		LOG.debug("Relevant attributes are {}", relevantAttributes);
		return relevantAttributes;
	}
}