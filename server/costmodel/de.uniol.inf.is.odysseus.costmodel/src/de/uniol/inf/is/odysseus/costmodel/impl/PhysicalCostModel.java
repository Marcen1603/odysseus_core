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
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.PhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.util.CostModelUtil;

public class PhysicalCostModel implements IPhysicalCostModel {

	private static final Logger LOG = LoggerFactory.getLogger(PhysicalCostModel.class);
	
	@Override
	public PhysicalCost estimateCost(Collection<IPhysicalOperator> physicalOperators) {
		Preconditions.checkNotNull(physicalOperators, "Collection of physical operators must not be null!");
		Preconditions.checkArgument(!physicalOperators.isEmpty(), "Collection of physical operators must not be empty!");
		
		Collection<IPhysicalOperator> allOperators = collectAllOperators(physicalOperators);
		Collection<IPhysicalOperator> sources = CostModelUtil.filterForSources(allOperators);
		
		LOG.debug("Beginning cost estimation of physical operators");
		List<IPhysicalOperator> operatorsToVisit = Lists.newLinkedList(sources);
		Map<IPhysicalOperator, DetailCost> resultMap = Maps.newHashMap();
		while( !operatorsToVisit.isEmpty() ) {
			IPhysicalOperator visitingOperator = operatorsToVisit.remove(0);

			@SuppressWarnings("unchecked")
			IPhysicalOperatorEstimator<IPhysicalOperator> estimator = (IPhysicalOperatorEstimator<IPhysicalOperator>) OperatorEstimatorRegistry.getPhysicalOperatorEstimator(visitingOperator.getClass());
			Map<IPhysicalOperator, DetailCost> prevCostMap = createPrevCostMap(visitingOperator, resultMap);

			try {
				estimator.estimatePhysical(visitingOperator, prevCostMap);
			} catch( Throwable t ) {
				LOG.error("Exception in physical operator estimator", t);
				estimator = OperatorEstimatorRegistry.getStandardPhysicalOperatorEstimator();
				estimator.estimatePhysical(visitingOperator, prevCostMap);
			}
			
			double memCost = estimator.getMemory();
			if( memCost < 0 ) {
				LOG.error("Estimated memcost for operator {} is negative. Using default value.", visitingOperator);
				memCost = StandardPhysicalOperatorEstimator.DEFAULT_MEMORY_COST_BYTES;
			}
			
			double cpuCost = estimator.getCpu();
			if( cpuCost < 0 ) {
				LOG.error("Estimated cpucost for operator {} is negative. Using default value.", visitingOperator);
				cpuCost = StandardPhysicalOperatorEstimator.DEFAULT_CPU_COST;
			}
			
			double netCost = estimator.getNetwork();
			if( cpuCost < 0 ) {
				LOG.error("Estimated netcost for operator {} is negative. Using default value.", visitingOperator);
				netCost = StandardPhysicalOperatorEstimator.DEFAULT_NETWORK_COST_BYTES;
			}
		
			double selectivity = estimator.getSelectivity();
			if( selectivity < 0 ) {
				LOG.error("Estimated selectivity for operator {} is negative. Using default value.", visitingOperator);
				selectivity = StandardPhysicalOperatorEstimator.DEFAULT_SELECTIVITY;
			}

			double datarate = estimator.getDatarate();
			if( datarate < 0 ) {
				LOG.error("Estimated datarate for operator {} is negative. Using default value.", visitingOperator);
				datarate = StandardPhysicalOperatorEstimator.DEFAULT_DATARATE;
			}

			double windowSize = estimator.getWindowSize();
			if( windowSize < 0 ) {
				LOG.error("Estimated windowSize for operator {} is negative. Using default value.", visitingOperator);
				windowSize = StandardPhysicalOperatorEstimator.DEFAULT_WINDOW_SIZE;
			}
			
			DetailCost detailCost = new DetailCost(memCost, cpuCost, netCost, selectivity, datarate, windowSize);
			
			resultMap.put(visitingOperator, detailCost);
			
			// TODO: determine next operators to visit
		}
		LOG.debug("Estimation finished");
		
		return null;
	}

	private Map<IPhysicalOperator, DetailCost> createPrevCostMap(IPhysicalOperator visitingOperator, Map<IPhysicalOperator, DetailCost> resultMap) {
		Map<IPhysicalOperator, DetailCost> prevCostMap = Maps.newHashMap();
		
		if( visitingOperator.isSink() ) {
			ISink<?> asSink = (ISink<?>)visitingOperator;
			for( PhysicalSubscription<?> physSub : asSink.getSubscribedToSource() ) {
				IPhysicalOperator target = (IPhysicalOperator) physSub.getTarget();
				DetailCost prevDetailCost = resultMap.get(target);
				if( prevDetailCost == null ) {
					throw new RuntimeException("No detail cost of previous operator " + target + " available.");
				}
				prevCostMap.put(target, prevDetailCost);
			}
		}
		return prevCostMap;
	}

	private static Collection<IPhysicalOperator> collectAllOperators(Collection<IPhysicalOperator> physicalOperators) {
		Collection<IPhysicalOperator> allOperators = Lists.newArrayList();
		for( IPhysicalOperator physicalOperator : physicalOperators ) {
			
			Collection<IPhysicalOperator> collectedOperators = CostModelUtil.getAllPhysicalOperators(physicalOperator);
			for( IPhysicalOperator collectedOperator : collectedOperators ) {
				if( !allOperators.contains(collectedOperator) ) {
					allOperators.add(collectedOperator);
				}
			}
		}
		
		return allOperators;
	}

}
