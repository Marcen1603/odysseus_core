package de.uniol.inf.is.odysseus.costmodel.logical;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;

public abstract class StandardLogicalOperatorEstimator<T extends ILogicalOperator> implements ILogicalOperatorEstimator<T> {

	public static final double DEFAULT_MEMORY_COST_BYTES = 16;
	public static final double DEFAULT_CPU_COST = 0.0005;
	public static final double DEFAULT_NETWORK_COST_BYTES = 0;
	public static final double DEFAULT_SELECTIVITY = 1.0;
	public static final double DEFAULT_DATARATE = 20;
	public static final double DEFAULT_WINDOW_SIZE = Double.POSITIVE_INFINITY;
	
	private Map<ILogicalOperator, DetailCost> prevCostMap;
	private T operator;
	private Map<SDFAttribute, IHistogram> histogramMap;
	private ICostModelKnowledge knowledge;
	
	@Override
	public Collection<Class<? extends T>> getOperatorClasses() {
		Collection<Class<? extends T>> clazzList = Lists.newArrayList();
		Class<? extends T> operatorClass = getOperatorClass();
		if( operatorClass != null ) {
			clazzList.add(operatorClass);
		}
		return clazzList;
	}
	
	protected Class<? extends T> getOperatorClass() {
		return null;
	}
	
	@Override
	public void estimateLogical(T operator, Map<ILogicalOperator, DetailCost> previousCostMap, Map<SDFAttribute, IHistogram> histogramMap, ICostModelKnowledge knowledge) {
		Preconditions.checkNotNull(operator, "Operator must not be null!");
		Preconditions.checkNotNull(previousCostMap, "Map of prev. costs must not be null!");
		Preconditions.checkNotNull(histogramMap, "Map of histograms must not be null!");
		Preconditions.checkNotNull(knowledge, "Knowledge must not be null!");
		
		this.prevCostMap = previousCostMap;
		this.operator = operator;
		this.histogramMap = histogramMap;
		this.knowledge = knowledge;
	}
	
	protected final T getOperator() {
		return operator;
	}
	
	protected final Map<ILogicalOperator, DetailCost> getPrevCostMap() {
		return prevCostMap;
	}
	
	protected final Map<SDFAttribute, IHistogram> getHistogramMap() {
		return histogramMap;
	}
	
	protected final ICostModelKnowledge getCostModelKnowledge() {
		return knowledge;
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
