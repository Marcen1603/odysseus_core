package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCostCalculator<T> implements ICostCalculator<T> {
	
	@Override
	public T pickBest(Collection<T> candidates) {
		T optimal = null;
		ICost<T> bestScore = null;
		for (T root: candidates) {
			ICost<T> c = calculateCost(root);
			if (optimal == null || bestScore.getScore() > c.getScore()) {
				bestScore = c;
				optimal = root;
			}
		}
		return optimal;
	}

	@Override
	public List<T> pickBest(Collection<T> candidates, int num) {
		final Map<T, ICost<T>> costs = new LinkedHashMap<T, ICost<T>>(candidates.size());
		for (T root : candidates) {
			ICost<T> c = calculateCost(root);
			costs.put(root, c);
		}
		List<T> optimal = new ArrayList<T>(candidates.size());
		optimal.addAll(candidates);
		Collections.sort(optimal, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return costs.get(o1).getScore() - costs.get(o2).getScore();
			}
		});
		if (num < optimal.size()) {
			optimal.subList(num, optimal.size()).clear();
		}
		return optimal;
	}
	
}
