package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.base;

import java.util.Collection;
import java.util.List;

public interface ICostCalculator<T> {
	public ICost<T> calculateCost(T candidate);
	public T pickBest(Collection<T> candidates);
	public List<T> pickBest(Collection<T> candidates, int num);
}
