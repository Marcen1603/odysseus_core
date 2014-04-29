package de.uniol.inf.is.odysseus.costmodel.physical;

import java.util.Map;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;

public abstract class StandardPhysicalOperatorEstimator<T extends IPhysicalOperator> implements IPhysicalOperatorEstimator<T> {

	public static final double DEFAULT_MEMORY_COST_BYTES = 16;
	public static final double DEFAULT_CPU_COST = 0.0005;
	public static final double DEFAULT_NETWORK_COST_BYTES = 0;
	public static final double DEFAULT_SELECTIVITY = 1.0;
	public static final double DEFAULT_DATARATE = 20;
	public static final double DEFAULT_WINDOW_SIZE = Double.MAX_VALUE;
	
	private Map<IPhysicalOperator, DetailCost> prevCostMap;
	private T operator;
	private Map<SDFAttribute, IHistogram> histogramMap;
	
	@Override
	public void estimatePhysical(T operator, Map<IPhysicalOperator, DetailCost> previousCostMap, Map<SDFAttribute, IHistogram> histogramMap) {
		Preconditions.checkNotNull(operator, "operator must not be null!");
		Preconditions.checkNotNull(previousCostMap, "Map of previous costs must not be null!");
		Preconditions.checkNotNull(histogramMap, "Map of histograms must not be null!");
		
		this.prevCostMap = previousCostMap;
		this.operator = operator;
		this.histogramMap = histogramMap;
	}
	
	protected final T getOperator() {
		return operator;
	}
	
	protected final Map<IPhysicalOperator, DetailCost> getPrevCostMap() {
		return prevCostMap;
	}
	
	protected Map<SDFAttribute, IHistogram> getHistogramMap() {
		return histogramMap;
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
