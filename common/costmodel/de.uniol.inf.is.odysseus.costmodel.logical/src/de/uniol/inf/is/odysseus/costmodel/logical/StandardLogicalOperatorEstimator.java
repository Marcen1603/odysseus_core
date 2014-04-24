package de.uniol.inf.is.odysseus.costmodel.logical;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;

public abstract class StandardLogicalOperatorEstimator<T extends ILogicalOperator> implements ILogicalOperatorEstimator<T> {

	public static final double DEFAULT_MEMORY_COST_BYTES = 16;
	public static final double DEFAULT_CPU_COST = 0.0005;
	public static final double DEFAULT_NETWORK_COST_BYTES = 0;
	public static final double DEFAULT_SELECTIVITY = 1.0;
	public static final double DEFAULT_DATARATE = 20;
	public static final double DEFAULT_WINDOW_SIZE = Double.MAX_VALUE;
	
	private Map<ILogicalOperator, DetailCost> prevCostMap;
	private T operator;
	
	@Override
	public void estimateLogical(T operator, Map<ILogicalOperator, DetailCost> previousCostMap) {
		this.prevCostMap = previousCostMap;
		this.operator = operator;
	}
	
	protected final T getOperator() {
		return operator;
	}
	
	protected final Map<ILogicalOperator, DetailCost> getPrevCostMap() {
		return prevCostMap;
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
			for( ILogicalOperator prevOperator : prevCostMap.keySet() ) {
				datarate += prevCostMap.get(prevOperator).getDatarate();
			}
			return datarate;
		}
		return DEFAULT_DATARATE;
	}

	@Override
	public double getWindowSize() {
		if( !prevCostMap.isEmpty() ) {
			for( ILogicalOperator prevOperator : prevCostMap.keySet() ) {
				return prevCostMap.get(prevOperator).getWindowSize();
			}
		}
		return DEFAULT_WINDOW_SIZE;
	}
}
