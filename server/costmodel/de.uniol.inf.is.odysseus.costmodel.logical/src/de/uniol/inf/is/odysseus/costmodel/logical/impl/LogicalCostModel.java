package de.uniol.inf.is.odysseus.costmodel.logical.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalCost;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.util.CostModelUtil;

public class LogicalCostModel implements ILogicalCostModel {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalCostModel.class);
	
	private static ICostModelKnowledge knowledge;

	// called by OSGi-DS
	public static void bindCostModelKnowledge(ICostModelKnowledge serv) {
		knowledge = serv;
	}

	// called by OSGi-DS
	public static void unbindCostModelKnowledge(ICostModelKnowledge serv) {
		if (knowledge == serv) {
			knowledge = null;
		}
	}
	
	@Override
	public ILogicalCost estimateCost(Collection<ILogicalOperator> logicalOperators) {
		Preconditions.checkNotNull(logicalOperators, "list of logical operators for cost estimation must not be null!");
		Preconditions.checkArgument(!logicalOperators.isEmpty(), "List of logical operators must not be empty!");

		Collection<ILogicalOperator> allOperators = collectAllOperators(logicalOperators);
		Collection<ILogicalOperator> sources = CostModelUtil.filterForLogicalSources(allOperators);

		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(sources);
		Map<ILogicalOperator, DetailCost> resultMap = Maps.newHashMap();
		Map<SDFAttribute, IHistogram> histogramMap = createHistogramMap();

		LOG.debug("Beginning cost estimation of logical operators");
		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator operator = operatorsToVisit.remove(0);
			LOG.debug("Visiting operator {} of type {}", operator, operator.getClass().getSimpleName());

			tryEstimateOperator(resultMap, operator, histogramMap);

			for (LogicalSubscription logSub : operator.getSubscriptions()) {
				ILogicalOperator target = logSub.getSink();

				if (!resultMap.containsKey(target) && areAllSourcesVisited(resultMap, target)) {
					operatorsToVisit.add(target);
				}
			}
		}
		LOG.debug("Estimation finished");

		ILogicalCost totalCost = determineTotalCost(logicalOperators, resultMap);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Total cost for given operators");
			LOG.debug("\tCPU = {}", totalCost.getCpuSum());
			LOG.debug("\tMEM = {} Bytes", totalCost.getMemorySum());
			LOG.debug("\tNET = {} Bytes/sec", totalCost.getNetworkSum());
		}
		return totalCost;
	}
	
	private static Map<SDFAttribute, IHistogram> createHistogramMap() {
		Map<SDFAttribute, IHistogram> histogramMap = Maps.newHashMap();
		
		for( SDFAttribute attribute : knowledge.getHistogramAttributes() ) {
			Optional<IHistogram> optHistogram = knowledge.getHistogram(attribute);
			if( optHistogram.isPresent() ) {
				histogramMap.put(attribute, optHistogram.get());
			}
		}
		
		return histogramMap;
	}


	private static Collection<ILogicalOperator> collectAllOperators(Collection<ILogicalOperator> logicalOperators) {
		Collection<ILogicalOperator> allOperators = Lists.newArrayList();

		for (ILogicalOperator logicalOperator : logicalOperators) {

			Collection<ILogicalOperator> operators = CostModelUtil.getAllLogicalOperators(logicalOperator);

			for (ILogicalOperator operator : operators) {
				if (!allOperators.contains(operator)) {
					allOperators.add(operator);
				}
			}
		}

		return allOperators;
	}

	private static void tryEstimateOperator(Map<ILogicalOperator, DetailCost> resultMap, ILogicalOperator visitingOperator, Map<SDFAttribute, IHistogram> histogramMap) {
		@SuppressWarnings("unchecked")
		ILogicalOperatorEstimator<ILogicalOperator> estimator = (ILogicalOperatorEstimator<ILogicalOperator>) OperatorEstimatorRegistry.getLogicalOperatorEstimator(visitingOperator.getClass());
		LOG.debug("Using estimator {}", estimator.getClass().getName());
		Map<ILogicalOperator, DetailCost> prevCostMap = createPrevCostMap(visitingOperator, resultMap);

		try {
			estimator.estimateLogical(visitingOperator, prevCostMap, histogramMap, knowledge);
		} catch (Throwable t) {
			LOG.error("Exception in physical operator estimator", t);
			estimator = OperatorEstimatorRegistry.getStandardLogicalOperatorEstimator();
			estimator.estimateLogical(visitingOperator, prevCostMap, histogramMap, knowledge);
		}

		double memCost = estimator.getMemory();
		if (memCost < 0 || Double.isNaN(memCost)) {
			LOG.error("Estimated memcost for operator {} is negative or NaN. Using default value.", visitingOperator);
			memCost = StandardLogicalOperatorEstimator.DEFAULT_MEMORY_COST_BYTES;
		}

		double datarate = estimator.getDatarate();
		if (datarate < 0 || Double.isNaN(datarate)) {
			LOG.error("Estimated datarate for operator {} is negative or NaN. Using default value.", visitingOperator);
			datarate = StandardLogicalOperatorEstimator.DEFAULT_DATARATE;
		}
		
		Optional<Double> optCpuCost = knowledge.getCpuTime(visitingOperator.getClass().getSimpleName());
		double cpuCost = optCpuCost.isPresent() ? ( optCpuCost.get() / 1000000000 ) : estimator.getCpu();
		if (cpuCost < 0 || Double.isNaN(cpuCost)) {
			LOG.error("Estimated cpucost for operator {} is negative or NaN. Using default value.", visitingOperator);
			cpuCost = StandardLogicalOperatorEstimator.DEFAULT_CPU_COST;
		}

		double netCost = estimator.getNetwork();
		if (netCost < 0 || Double.isNaN(netCost)) {
			LOG.error("Estimated netcost for operator {} is negative or NaN. Using default value.", visitingOperator);
			netCost = StandardLogicalOperatorEstimator.DEFAULT_NETWORK_COST_BYTES;
		}

		double selectivity = estimator.getSelectivity();
		if (selectivity < 0 || Double.isNaN(selectivity)) {
			LOG.error("Estimated selectivity for operator {} is negative or NaN. Using default value.", visitingOperator);
			selectivity = StandardLogicalOperatorEstimator.DEFAULT_SELECTIVITY;
		}

		double windowSize = estimator.getWindowSize();
		if (windowSize < 0 || Double.isNaN(windowSize)) {
			LOG.error("Estimated windowSize for operator {} is negative or NaN. Using default value.", visitingOperator);
			windowSize = StandardLogicalOperatorEstimator.DEFAULT_WINDOW_SIZE;
		}

		DetailCost detailCost = new DetailCost(memCost, cpuCost, netCost, selectivity, datarate, windowSize);
		resultMap.put(visitingOperator, detailCost);
		if (LOG.isDebugEnabled()) {
			logEstimationResults(visitingOperator, detailCost);
		}
	}

	private static Map<ILogicalOperator, DetailCost> createPrevCostMap(ILogicalOperator visitingOperator, Map<ILogicalOperator, DetailCost> resultMap) {
		Map<ILogicalOperator, DetailCost> prevCostMap = Maps.newHashMap();

		for (LogicalSubscription logSub : visitingOperator.getSubscribedToSource()) {
			DetailCost prevDetailCost = resultMap.get(logSub.getSource());
			if (prevDetailCost == null) {
				throw new RuntimeException("No detail cost of previous operator " + logSub.getSource() + " available.");
			}
			prevCostMap.put(logSub.getSource(), prevDetailCost);
		}
		return prevCostMap;
	}

	private static void logEstimationResults(ILogicalOperator visitingOperator, DetailCost detailCost) {
		LOG.debug("Estimation result for operator {}", visitingOperator);
		LOG.debug("\tCPU = {}", detailCost.getCpuCost());
		LOG.debug("\tMEM = {} Bytes", detailCost.getMemCost());
		LOG.debug("\tNET = {} Bytes/sec", detailCost.getNetCost());
		LOG.debug("\tWND = {}", detailCost.getWindowSize());
		LOG.debug("\tSEL = {}", detailCost.getSelectivity());
		LOG.debug("\tRAT = {}", detailCost.getDatarate());
	}
	
	private static boolean areAllSourcesVisited(Map<ILogicalOperator, DetailCost> resultMap, ILogicalOperator target) {
		for (LogicalSubscription logSubToSource : target.getSubscribedToSource()) {
			ILogicalOperator toSourceTarget = logSubToSource.getSource();
			if (!resultMap.containsKey(toSourceTarget)) {
				return false;
			}
		}
		return true;
	}
	
	private static ILogicalCost determineTotalCost(Collection<ILogicalOperator> physicalOperators, Map<ILogicalOperator, DetailCost> resultMap) {
		Map<ILogicalOperator, DetailCost> costMap = Maps.newHashMap();

		for (ILogicalOperator physicalOperator : physicalOperators) {
			costMap.put(physicalOperator, resultMap.get(physicalOperator));
		}

		return new LogicalCost(costMap);
	}
}
