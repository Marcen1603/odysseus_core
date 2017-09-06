package de.uniol.inf.is.odysseus.costmodel.physical.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.util.CostModelUtil;

public class PhysicalCostModel implements IPhysicalCostModel {

	private static final Logger LOG = LoggerFactory.getLogger(PhysicalCostModel.class);
	
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
	public IPhysicalCost estimateCost(Collection<IPhysicalOperator> physicalOperators) {
		Preconditions.checkNotNull(physicalOperators, "Collection of physical operators must not be null!");
		Preconditions.checkArgument(!physicalOperators.isEmpty(), "Collection of physical operators must not be empty!");

		Collection<IPhysicalOperator> allOperators = collectAllOperators(physicalOperators);
		Collection<IPhysicalOperator> sources = CostModelUtil.filterForPhysicalSources(allOperators);

		List<IPhysicalOperator> operatorsToVisit = Lists.newLinkedList(sources);
		Map<IPhysicalOperator, DetailCost> resultMap = Maps.newHashMap();
		Map<SDFAttribute, IHistogram> histogramMap = createHistogramMap();
		
		LOG.debug("Beginning cost estimation of physical operators");
		while (!operatorsToVisit.isEmpty()) {
			IPhysicalOperator visitingOperator = operatorsToVisit.remove(0);
			LOG.debug("Visiting operator {}", visitingOperator);
			tryEstimateOperator(resultMap, visitingOperator, histogramMap);

			if (visitingOperator instanceof ISource) {
				ISource<?> opAsSource = (ISource<?>) visitingOperator;
				for (AbstractPhysicalSubscription<?,? extends ISink<?>> physSub : opAsSource.getSubscriptions()) {
					ISink<?> target = physSub.getSink();

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

	private static void tryEstimateOperator(Map<IPhysicalOperator, DetailCost> resultMap, IPhysicalOperator visitingOperator, Map<SDFAttribute, IHistogram> histogramMap) {
		@SuppressWarnings("unchecked")
		IPhysicalOperatorEstimator<IPhysicalOperator> estimator = (IPhysicalOperatorEstimator<IPhysicalOperator>) OperatorEstimatorRegistry.getPhysicalOperatorEstimator(visitingOperator.getClass());
		LOG.debug("Using estimator {}", estimator.getClass().getName());
		Map<IPhysicalOperator, DetailCost> prevCostMap = createPrevCostMap(visitingOperator, resultMap);

		try {
			estimator.estimatePhysical(visitingOperator, prevCostMap, histogramMap, knowledge);
		} catch (Throwable t) {
			LOG.error("Exception in physical operator estimator", t);
			estimator = OperatorEstimatorRegistry.getStandardPhysicalOperatorEstimator();
			estimator.estimatePhysical(visitingOperator, prevCostMap, histogramMap, knowledge);
		}

		double memCost = estimator.getMemory();
		if (memCost < 0 || Double.isNaN(memCost)) {
			LOG.error("Estimated memcost for operator {} is negative or NaN. Using default value.", visitingOperator);
			memCost = StandardPhysicalOperatorEstimator.DEFAULT_MEMORY_COST_BYTES;
		}

		Optional<Double> optDatarate = EstimatorHelper.getDatarateMetadata(visitingOperator);
		double datarate = optDatarate.isPresent() ? optDatarate.get() : estimator.getDatarate();
		if (datarate < 0 || Double.isNaN(datarate)) {
			LOG.error("Estimated datarate for operator {} is negative or NaN. Using default value.", visitingOperator);
			datarate = StandardPhysicalOperatorEstimator.DEFAULT_DATARATE;
		}

		Optional<Double> optCpu = EstimatorHelper.getCpuTimeMetadata(visitingOperator);
		if( !optCpu.isPresent() ) {
			optCpu = knowledge.getCpuTime(visitingOperator.getClass().getSimpleName());
			if( optCpu.isPresent() ) {
				optCpu = Optional.of( optCpu.get() / 1000000000 );
			}
		}
		double cpuCost = optCpu.isPresent() ? optCpu.get() * datarate : estimator.getCpu();
		if (cpuCost < 0 || Double.isNaN(cpuCost)) {
			LOG.error("Estimated cpucost for operator {} is negative or NaN. Using default value.", visitingOperator);
			cpuCost = StandardPhysicalOperatorEstimator.DEFAULT_CPU_COST * datarate;
		}

		double netCost = estimator.getNetwork();
		if (netCost < 0 || Double.isNaN(netCost)) {
			LOG.error("Estimated netcost for operator {} is negative or NaN. Using default value.", visitingOperator);
			netCost = StandardPhysicalOperatorEstimator.DEFAULT_NETWORK_COST_BYTES;
		}

		Optional<Double> optSelectivity = EstimatorHelper.getSelectivityMetadata(visitingOperator);
		double selectivity = optSelectivity.isPresent() ? optSelectivity.get() : estimator.getSelectivity();
		if (selectivity < 0 || Double.isNaN(selectivity)) {
			LOG.error("Estimated selectivity for operator {} is negative or NaN. Using default value.", visitingOperator);
			selectivity = StandardPhysicalOperatorEstimator.DEFAULT_SELECTIVITY;
		}
		
		double windowSize = estimator.getWindowSize();
		if (windowSize < 0 || Double.isNaN(windowSize)) {
			LOG.error("Estimated windowSize for operator {} is negative or NaN. Using default value.", visitingOperator);
			windowSize = StandardPhysicalOperatorEstimator.DEFAULT_WINDOW_SIZE;
		}

		DetailCost detailCost = new DetailCost(memCost, cpuCost, netCost, selectivity, datarate, windowSize);
		resultMap.put(visitingOperator, detailCost);
		if (LOG.isDebugEnabled()) {
			logEstimationResults(visitingOperator, detailCost);
		}
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

	private static IPhysicalCost determineTotalCost(Collection<IPhysicalOperator> physicalOperators, Map<IPhysicalOperator, DetailCost> resultMap) {
		Map<IPhysicalOperator, DetailCost> costMap = Maps.newHashMap();

		for (IPhysicalOperator physicalOperator : physicalOperators) {
			costMap.put(physicalOperator, resultMap.get(physicalOperator));
		}

		return new PhysicalCost(costMap);
	}

	private static boolean areAllSourcesVisited(Map<IPhysicalOperator, DetailCost> resultMap, ISink<?> target) {
		for (AbstractPhysicalSubscription<? extends ISource<?>,?> physSubToSource : target.getSubscribedToSource()) {
			ISource<?> toSourceTarget = physSubToSource.getSource();
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
			for (AbstractPhysicalSubscription<?,?> physSub : asSink.getSubscribedToSource()) {
				IPhysicalOperator target = (IPhysicalOperator) physSub.getSource();
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
		LOG.debug("\tRAT = {}", detailCost.getDatarate());
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
