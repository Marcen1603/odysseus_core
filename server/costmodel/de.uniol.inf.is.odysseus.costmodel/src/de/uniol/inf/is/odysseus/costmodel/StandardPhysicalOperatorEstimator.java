package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class StandardPhysicalOperatorEstimator implements IPhysicalOperatorEstimator<IPhysicalOperator> {

	public static final double DEFAULT_MEMORY_COST_BYTES = 16;
	public static final double DEFAULT_CPU_COST = 0.0005;
	public static final double DEFAULT_NETWORK_COST_BYTES = 0;
	public static final double DEFAULT_SELECTIVITY = 1.0;
	public static final double DEFAULT_DATARATE = 20;
	public static final double DEFAULT_WINDOW_SIZE = Double.MAX_VALUE;
	
	private Map<IPhysicalOperator, DetailCost> prevCostMap;
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<IPhysicalOperator>> getOperatorClasses() {
		return Lists.newArrayList(IPhysicalOperator.class);
	}

	@Override
	public void estimatePhysical(IPhysicalOperator operator, Map<IPhysicalOperator, DetailCost> previousCostMap) {
		this.prevCostMap = previousCostMap;
	}
	
	@Override
	public double getMemory() {
		return DEFAULT_MEMORY_COST_BYTES;
	}

	@Override
	public double getCpu() {
		return DEFAULT_CPU_COST;
	}

	@Override
	public double getNetwork() {
		return DEFAULT_NETWORK_COST_BYTES;
	}

	@Override
	public double getSelectivity() {
		return DEFAULT_SELECTIVITY;
	}

	@Override
	public double getDatarate() {
		if( !prevCostMap.isEmpty() ) {
			double datarate = 0.0;
			for( IPhysicalOperator prevOperator : prevCostMap.keySet() ) {
				datarate += prevCostMap.get(prevOperator).getDatarate();
			}
			return datarate;
		}
		return DEFAULT_DATARATE;
	}

	@Override
	public double getWindowSize() {
		if( !prevCostMap.isEmpty() ) {
			for( IPhysicalOperator prevOperator : prevCostMap.keySet() ) {
				return prevCostMap.get(prevOperator).getWindowSize();
			}
		}
		return DEFAULT_WINDOW_SIZE;
	}
}
