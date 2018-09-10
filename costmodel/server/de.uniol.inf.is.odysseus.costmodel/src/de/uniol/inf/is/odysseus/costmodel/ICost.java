package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;

public interface ICost<T> {
	
	public double getMemorySum();
	public double getCpuSum();
	public double getNetworkSum();

	public Collection<T> getOperators();
	public boolean containsOperator(T operator);
	public DetailCost getDetailCost( T operator );

}
