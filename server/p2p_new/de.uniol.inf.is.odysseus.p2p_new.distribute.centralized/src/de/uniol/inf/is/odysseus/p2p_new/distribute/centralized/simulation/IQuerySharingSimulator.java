package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;

public interface IQuerySharingSimulator {
	public SimulationResult simulateQuerySharing(Graph graph, OptimizationConfiguration conf);
}
