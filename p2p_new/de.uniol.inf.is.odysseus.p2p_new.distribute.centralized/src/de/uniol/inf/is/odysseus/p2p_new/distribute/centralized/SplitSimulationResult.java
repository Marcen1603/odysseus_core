package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.SimulationResult;

public class SplitSimulationResult {
	private SimulationResult placeableResult;
	private Graph surplusNodes;
	private List<PlanJunction> junctions = new ArrayList<PlanJunction>();
	
	public void addJunction(PlanJunction pj) {
		this.junctions.add(pj);
	}
	
	public void setJunctions(List<PlanJunction> junctions) {
		this.junctions = junctions;
	}
	
	public List<PlanJunction> getJunctions() {
		return junctions;
	}

	public Graph getSurplusNodes() {
		return surplusNodes;
	}

	public void setSurplusNodes(Graph surplusNodes) {
		this.surplusNodes = surplusNodes;
	}

	public SimulationResult getPlaceableResult() {
		return placeableResult;
	}

	public void setPlaceableResult(SimulationResult placeableResult) {
		this.placeableResult = placeableResult;
	}
}
