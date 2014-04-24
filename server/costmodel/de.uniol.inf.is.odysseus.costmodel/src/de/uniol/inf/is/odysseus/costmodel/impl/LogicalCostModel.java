package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.ILogicalCost;
import de.uniol.inf.is.odysseus.costmodel.ILogicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.ILogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.util.CostModelUtil;

public class LogicalCostModel implements ILogicalCostModel {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalCostModel.class);

	@Override
	public ILogicalCost estimateCost(Collection<ILogicalOperator> logicalOperators) {
		Preconditions.checkNotNull(logicalOperators, "list of logical operators for cost estimation must not be null!");
		Preconditions.checkArgument(!logicalOperators.isEmpty(), "List of logical operators must not be empty!");

		Collection<ILogicalOperator> allOperators = collectAllOperators(logicalOperators);
		Collection<ILogicalOperator> sources = CostModelUtil.filterForLogicalSources(allOperators);

		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(sources);
		Map<ILogicalOperator, DetailCost> resultMap = Maps.newHashMap();

		LOG.debug("Beginning cost estimation of logical operators");
		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator operator = operatorsToVisit.remove(0);
			LOG.debug("Visiting operator {}", operator);

			tryEstimateOperator(resultMap, operator);

			for (LogicalSubscription logSub : operator.getSubscriptions()) {
				ILogicalOperator target = logSub.getTarget();

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

	private static void tryEstimateOperator(Map<ILogicalOperator, DetailCost> resultMap, ILogicalOperator visitingOperator) {
		@SuppressWarnings("unchecked")
		ILogicalOperatorEstimator<ILogicalOperator> estimator = (ILogicalOperatorEstimator<ILogicalOperator>) OperatorEstimatorRegistry.getLogicalOperatorEstimator(visitingOperator.getClass());
		LOG.debug("Using estimator {}", estimator.getClass().getName());
		Map<ILogicalOperator, DetailCost> prevCostMap = createPrevCostMap(visitingOperator, resultMap);

		try {
			estimator.estimateLogical(visitingOperator, prevCostMap);
		} catch (Throwable t) {
			LOG.error("Exception in physical operator estimator", t);
			estimator = OperatorEstimatorRegistry.getStandardLogicalOperatorEstimator();
			estimator.estimateLogical(visitingOperator, prevCostMap);
		}

		double memCost = estimator.getMemory();
		if (memCost < 0) {
			LOG.error("Estimated memcost for operator {} is negative. Using default value.", visitingOperator);
			memCost = StandardPhysicalOperatorEstimator.DEFAULT_MEMORY_COST_BYTES;
		}

		double cpuCost = estimator.getCpu();
		if (cpuCost < 0) {
			LOG.error("Estimated cpucost for operator {} is negative. Using default value.", visitingOperator);
			cpuCost = StandardPhysicalOperatorEstimator.DEFAULT_CPU_COST;
		}

		double netCost = estimator.getNetwork();
		if (cpuCost < 0) {
			LOG.error("Estimated netcost for operator {} is negative. Using default value.", visitingOperator);
			netCost = StandardPhysicalOperatorEstimator.DEFAULT_NETWORK_COST_BYTES;
		}

		double selectivity = estimator.getSelectivity();
		if (selectivity < 0) {
			LOG.error("Estimated selectivity for operator {} is negative. Using default value.", visitingOperator);
			selectivity = StandardPhysicalOperatorEstimator.DEFAULT_SELECTIVITY;
		}

		double datarate = estimator.getDatarate();
		if (datarate < 0) {
			LOG.error("Estimated datarate for operator {} is negative. Using default value.", visitingOperator);
			datarate = StandardPhysicalOperatorEstimator.DEFAULT_DATARATE;
		}

		double windowSize = estimator.getWindowSize();
		if (windowSize < 0) {
			LOG.error("Estimated windowSize for operator {} is negative. Using default value.", visitingOperator);
			windowSize = StandardPhysicalOperatorEstimator.DEFAULT_WINDOW_SIZE;
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
			DetailCost prevDetailCost = resultMap.get(logSub.getTarget());
			if (prevDetailCost == null) {
				throw new RuntimeException("No detail cost of previous operator " + logSub.getTarget() + " available.");
			}
			prevCostMap.put(logSub.getTarget(), prevDetailCost);
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
		LOG.debug("\tDAT = {}", detailCost.getWindowSize());
	}
	
	private static boolean areAllSourcesVisited(Map<ILogicalOperator, DetailCost> resultMap, ILogicalOperator target) {
		for (LogicalSubscription logSubToSource : target.getSubscribedToSource()) {
			ILogicalOperator toSourceTarget = logSubToSource.getTarget();
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
