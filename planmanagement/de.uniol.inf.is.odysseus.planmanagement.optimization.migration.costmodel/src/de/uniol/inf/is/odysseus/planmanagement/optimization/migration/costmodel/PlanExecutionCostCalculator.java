package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

/**
 * 
 * @author Tobias Witt
 *
 */
public class PlanExecutionCostCalculator {
	
	// TODO: Gewichtungen einstellbar machen
	
	// TODO: historische metadaten wie datenrate und selektivitaet einfuegen
	public static PlanExecutionCost calculateCost(IPhysicalOperator root) {
		// TODO: visitor fuer Kostenberechnung an jedem Operator
		PlanExecutionCost c = new PlanExecutionCost(24000, 60, 12, 0);
		c.setScore((int)(0.1 * c.getMemoryConsumption()
				+ 3.0 * c.getCpuTime()
				+ 0.5 * c.getLatency()
				+ 0.1 * c.getNetworkBandwidth()));
		return c;
	}
	
	// TODO: Liste zurueckliefern. schwellwert fuer 'gleichgute' plaene
	public static IPhysicalOperator pickOptimalPlan(Collection<IPhysicalOperator> plans) {
		IPhysicalOperator optimal = null;
		PlanExecutionCost bestScore = null;
		for (IPhysicalOperator root: plans) {
			PlanExecutionCost c = calculateCost(root);
			if (optimal == null || bestScore.getScore() > c.getScore()) {
				bestScore = c;
				optimal = root;
			}
		}
		return optimal;
	}
	
}
