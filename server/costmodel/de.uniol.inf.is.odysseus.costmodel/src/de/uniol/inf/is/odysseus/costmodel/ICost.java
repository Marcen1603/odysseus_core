package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;

public interface ICost<T> {
	
	public double getMemorySum();
	public double getCpuSum();
	public double getNetworkSum();

	public Collection<T> getOperators();
	public boolean containsOperator(T operator);

	public double getMemory(T operator);
	public double getCpu(T operator);
	public double getNetwork(T operator);
	public double getDatarate(T operator);
	public double getWindowSize(T operator);
	public double getSelectivity(T operator);

}
