package de.uniol.inf.is.odysseus.costmodel.physical;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;

public abstract class StandardPhysicalOperatorEstimator<T extends IPhysicalOperator> implements IPhysicalOperatorEstimator<T> {

	public static final double DEFAULT_MEMORY_COST_BYTES = 16;
	public static final double DEFAULT_CPU_COST = 0.0005;
	public static final double DEFAULT_NETWORK_COST_BYTES = 0;
	public static final double DEFAULT_SELECTIVITY = 1.0;
	
	//TODO Changed for Loadbalancing, was 20 before ( is too high for Rasperry Pis)
	public static final double DEFAULT_DATARATE = 4;
	public static final double DEFAULT_WINDOW_SIZE = Double.POSITIVE_INFINITY;
	
	private Map<IPhysicalOperator, DetailCost> prevCostMap;
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
	public void estimatePhysical(T operator, Map<IPhysicalOperator, DetailCost> previousCostMap, Map<SDFAttribute, IHistogram> histogramMap, ICostModelKnowledge knowledge) {
		Preconditions.checkNotNull(operator, "operator must not be null!");
		Preconditions.checkNotNull(previousCostMap, "Map of previous costs must not be null!");
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
	
	protected final Map<IPhysicalOperator, DetailCost> getPrevCostMap() {
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
		Optional<Double> optCpu = EstimatorHelper.getCpuTimeMetadata(getOperator());
		return ( optCpu.isPresent() ? optCpu.get() : DEFAULT_CPU_COST ) * getDatarate();
	}

	@Override
	public double getNetwork() {
		return DEFAULT_NETWORK_COST_BYTES;
	}

	@Override
	public double getSelectivity() {
		Optional<Double> optSelectivity = EstimatorHelper.getSelectivityMetadata(getOperator());
		return optSelectivity.isPresent() ? optSelectivity.get() : DEFAULT_SELECTIVITY;
	}

	@Override
	public double getDatarate() {
		Optional<Double> optDatarate = EstimatorHelper.getDatarateMetadata(getOperator());
		if( optDatarate.isPresent() ) {
			return optDatarate.get();
		}
		
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
