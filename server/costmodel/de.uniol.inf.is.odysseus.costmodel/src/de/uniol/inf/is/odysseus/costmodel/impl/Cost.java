package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Cost<T> {

	private final Map<T, DetailCost> detailCostMap = Maps.newHashMap();
	
	private final double memorySum;
	private final double cpuSum;
	private final double networkSum;
	
	public Cost(Map<T, DetailCost> detailCostMap) {
		Preconditions.checkNotNull(detailCostMap, "Map of detail costs must not be null!");
		Preconditions.checkArgument(detailCostMap.isEmpty(), "Map of detail costs must not be empty!");
		
		this.detailCostMap.putAll(detailCostMap);
		
		double memSum = 0.0;
		double cpuSum2 = 0.0;
		double netSum = 0.0;
		for( T key : detailCostMap.keySet() ) {
			DetailCost dc = detailCostMap.get(key);
			memSum += dc.getMemCost();
			cpuSum2 += dc.getCpuCost();
			netSum += dc.getNetCost();
		}
		
		this.memorySum = memSum;
		this.cpuSum = cpuSum2;
		this.networkSum = netSum;
	}
	
	public double getMemorySum() {
		return memorySum;
	}
	
	public double getCpuSum() {
		return cpuSum;
	}
	
	public double getNetworkSum() {
		return networkSum;
	}
	
	public Collection<T> getOperators() {
		return Lists.newArrayList(detailCostMap.keySet());
	}
	
	public boolean containsOperator( T operator ) {
		Preconditions.checkNotNull(operator, "Operator to check existence must not be null!");
		
		return detailCostMap.containsKey(operator);
	}
	
	public double getMemory( T operator ) {
		Preconditions.checkNotNull(operator, "Operator to get the memory cost must not be null!");
		DetailCost detailCost = detailCostMap.get(operator);
		if( detailCost == null ) {
			throw new RuntimeException("Could not get the memory cost of an unknown operator :" + operator.toString());
		}
		
		return detailCost.getMemCost();
	}
	
	public double getCpu( T operator ) {
		Preconditions.checkNotNull(operator, "Operator to get the cpu cost must not be null!");
		DetailCost detailCost = detailCostMap.get(operator);
		if( detailCost == null ) {
			throw new RuntimeException("Could not get the cpu cost of an unknown operator :" + operator.toString());
		}
		
		return detailCost.getCpuCost();
	}
	
	public double getNetwork( T operator ) {
		Preconditions.checkNotNull(operator, "Operator to get the network cost must not be null!");
		DetailCost detailCost = detailCostMap.get(operator);
		if( detailCost == null ) {
			throw new RuntimeException("Could not get the network cost of an unknown operator :" + operator.toString());
		}
		
		return detailCost.getNetCost();
	}
}
