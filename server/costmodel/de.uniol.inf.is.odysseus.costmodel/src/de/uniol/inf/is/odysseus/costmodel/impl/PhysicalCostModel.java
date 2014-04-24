package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.util.CostModelUtil;

public class PhysicalCostModel implements IPhysicalCostModel {

	private static final Logger LOG = LoggerFactory.getLogger(PhysicalCostModel.class);

	@Override
	public IPhysicalCost estimateCost(Collection<IPhysicalOperator> physicalOperators) {
		Preconditions.checkNotNull(physicalOperators, "Collection of physical operators must not be null!");
		Preconditions.checkArgument(!physicalOperators.isEmpty(), "Collection of physical operators must not be empty!");

		Collection<IPhysicalOperator> allOperators = collectAllOperators(physicalOperators);
		Collection<IPhysicalOperator> sources = CostModelUtil.filterForPhysicalSources(allOperators);

		List<IPhysicalOperator> operatorsToVisit = Lists.newLinkedList(sources);
		Map<IPhysicalOperator, DetailCost> resultMap = Maps.newHashMap();
		
		LOG.debug("Beginning cost estimation of physical operators");
		while (!operatorsToVisit.isEmpty()) {
			IPhysicalOperator visitingOperator = operatorsToVisit.remove(0);
			LOG.debug("Visiting operator {}", visitingOperator);
			tryEstimateOperator(resultMap, visitingOperator);

			if (visitingOperator instanceof ISource) {
				ISource<?> opAsSource = (ISource<?>) visitingOperator;
				for (PhysicalSubscription<? extends ISink<?>> physSub : opAsSource.getSubscriptions()) {
					ISink<?> target = physSub.getTarget();

					if (!resultMap.containsKey(target) && areAllSourcesVisited(resultMap, target)) {
						operatorsToVisit.add(target);
					}
				}
			}
		}
		LOG.debug("Estimation finished");

		IPhysicalCost totalCost = determineTotalCost(physicalOperators, resultMap);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Total cost for given operators");
			LOG.debug("\tCPU = {}", totalCost.getCpuSum());
			LOG.debug("\tMEM = {} Bytes", totalCost.getMemorySum());
			LOG.debug("\tNET = {} Bytes/sec", totalCost.getNetworkSum());
		}
		return totalCost;
	}

	private static void tryEstimateOperator(Map<IPhysicalOperator, DetailCost> resultMap, IPhysicalOperator visitingOperator) {
		@SuppressWarnings("unchecked")
		IPhysicalOperatorEstimator<IPhysicalOperator> estimator = (IPhysicalOperatorEstimator<IPhysicalOperator>) OperatorEstimatorRegistry.getPhysicalOperatorEstimator(visitingOperator.getClass());
		LOG.debug("Using estimator {}", estimator.getClass().getName());
		Map<IPhysicalOperator, DetailCost> prevCostMap = createPrevCostMap(visitingOperator, resultMap);

		try {
			estimator.estimatePhysical(visitingOperator, prevCostMap);
		} catch (Throwable t) {
			LOG.error("Exception in physical operator estimator", t);
			estimator = OperatorEstimatorRegistry.getStandardPhysicalOperatorEstimator();
			estimator.estimatePhysical(visitingOperator, prevCostMap);
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

	private static IPhysicalCost determineTotalCost(Collection<IPhysicalOperator> physicalOperators, Map<IPhysicalOperator, DetailCost> resultMap) {
		Map<IPhysicalOperator, DetailCost> costMap = Maps.newHashMap();

		for (IPhysicalOperator physicalOperator : physicalOperators) {
			costMap.put(physicalOperator, resultMap.get(physicalOperator));
		}

		return new PhysicalCost(costMap);
	}

	private static boolean areAllSourcesVisited(Map<IPhysicalOperator, DetailCost> resultMap, ISink<?> target) {
		for (PhysicalSubscription<? extends ISource<?>> physSubToSource : target.getSubscribedToSource()) {
			ISource<?> toSourceTarget = physSubToSource.getTarget();
			if (!resultMap.containsKey(toSourceTarget)) {
				return false;
			}
		}
		return true;
	}

	private static Map<IPhysicalOperator, DetailCost> createPrevCostMap(IPhysicalOperator visitingOperator, Map<IPhysicalOperator, DetailCost> resultMap) {
		Map<IPhysicalOperator, DetailCost> prevCostMap = Maps.newHashMap();

		if (visitingOperator.isSink()) {
			ISink<?> asSink = (ISink<?>) visitingOperator;
			for (PhysicalSubscription<?> physSub : asSink.getSubscribedToSource()) {
				IPhysicalOperator target = (IPhysicalOperator) physSub.getTarget();
				DetailCost prevDetailCost = resultMap.get(target);
				if (prevDetailCost == null) {
					throw new RuntimeException("No detail cost of previous operator " + target + " available.");
				}
				prevCostMap.put(target, prevDetailCost);
			}
		}
		return prevCostMap;
	}

	private static void logEstimationResults(IPhysicalOperator visitingOperator, DetailCost detailCost) {
		LOG.debug("Estimation result for operator {}", visitingOperator);
		LOG.debug("\tCPU = {}", detailCost.getCpuCost());
		LOG.debug("\tMEM = {} Bytes", detailCost.getMemCost());
		LOG.debug("\tNET = {} Bytes/sec", detailCost.getNetCost());
		LOG.debug("\tWND = {}", detailCost.getWindowSize());
		LOG.debug("\tSEL = {}", detailCost.getSelectivity());
		LOG.debug("\tDAT = {}", detailCost.getWindowSize());
	}

	private static Collection<IPhysicalOperator> collectAllOperators(Collection<IPhysicalOperator> physicalOperators) {
		Collection<IPhysicalOperator> allOperators = Lists.newArrayList();
		for (IPhysicalOperator physicalOperator : physicalOperators) {

			Collection<IPhysicalOperator> collectedOperators = CostModelUtil.getAllPhysicalOperators(physicalOperator);
			for (IPhysicalOperator collectedOperator : collectedOperators) {
				if (!allOperators.contains(collectedOperator)) {
					allOperators.add(collectedOperator);
				}
			}
		}

		return allOperators;
	}

}
