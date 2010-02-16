package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.base.ICostCalculator;

/**
 * 
 * @author Tobias Witt
 *
 */
public class PlanExecutionCostCalculator implements ICostCalculator<IPhysicalOperator> {
	
	// TODO: Gewichtungen einstellbar machen

	// TODO: historische metadaten wie datenrate und selektivitaet einfuegen
	@Override
	public ICost<IPhysicalOperator> calculateCost(IPhysicalOperator candidate) {
		// TODO: visitor fuer Kostenberechnung an jedem Operator
		PlanExecutionCost c = new PlanExecutionCost(24000, 60, 12, 0);
		c.setScore((int)(0.1 * c.getMemoryConsumption()
				+ 3.0 * c.getCpuTime()
				+ 0.5 * c.getLatency()
				+ 0.1 * c.getNetworkBandwidth()));
		c.setScore(Integer.parseInt(candidate.toString().replaceAll("[^0-9]", "")));
		return (ICost<IPhysicalOperator>)c;
	}
	
	@Override
	public IPhysicalOperator pickBest(Collection<IPhysicalOperator> candidates) {
		IPhysicalOperator optimal = null;
		ICost<IPhysicalOperator> bestScore = null;
		for (IPhysicalOperator root: candidates) {
			ICost<IPhysicalOperator> c = calculateCost(root);
			if (optimal == null || bestScore.getScore() > c.getScore()) {
				bestScore = c;
				optimal = root;
			}
		}
		return optimal;
	}

	@Override
	public List<IPhysicalOperator> pickBest(Collection<IPhysicalOperator> candidates, int num) {
		final Map<IPhysicalOperator, ICost<IPhysicalOperator>> costs = new LinkedHashMap<IPhysicalOperator, ICost<IPhysicalOperator>>(candidates.size());
		for (IPhysicalOperator root : candidates) {
			ICost<IPhysicalOperator> c = calculateCost(root);
			costs.put(root, c);
		}
		List<IPhysicalOperator> optimal = new ArrayList<IPhysicalOperator>(candidates.size());
		optimal.addAll(candidates);
		Collections.sort(optimal, new Comparator<IPhysicalOperator>() {
			@Override
			public int compare(IPhysicalOperator o1, IPhysicalOperator o2) {
				return costs.get(o1).getScore() - costs.get(o2).getScore();
			}
		});
		if (num < optimal.size()) {
			optimal.subList(num, optimal.size()).clear();
		}
		return optimal;
	}
	
}
