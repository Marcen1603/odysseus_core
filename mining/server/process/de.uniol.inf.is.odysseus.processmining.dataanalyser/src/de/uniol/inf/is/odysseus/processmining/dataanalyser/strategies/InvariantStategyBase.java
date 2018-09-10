package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTupleHelper;

public abstract class InvariantStategyBase<T extends IMetaAttribute> implements IInvariantStrategy<T>{

	Tuple<T> mostRecentModel = null;
	Tuple<T> currentModel = null;
	InductiveMinerTransferTupleHelper<T> transferHelper = new InductiveMinerTransferTupleHelper<T>();
	
	protected InvariantStategyBase() {

	}
	protected void updateData(Tuple<T> mostRecent, Tuple<T> current){
		this.mostRecentModel = mostRecent;
		this.currentModel = current;
	}
	/**
	 * Creates a directly follow graph base on the given directly follow
	 * relations
	 * 
	 * @param dfrel
	 * @return
	 */
	protected DirectedWeightedPseudograph<String, DefaultWeightedEdge> getGraph(
			HashMap<Object, AbstractLCTuple<T>> dfrel) {

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> dfg = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		for (Map.Entry<Object, AbstractLCTuple<T>> entry : dfrel.entrySet()) {
			DFRTuple<T> t = (DFRTuple<T>) entry.getValue();
			String startNode = t.getActivity();
			String endNode = t.getFollowActivity();
			// Graph
			dfg.addVertex(startNode);
			dfg.addVertex(endNode);
			DefaultWeightedEdge e = dfg.addEdge(t.getActivity(),
					t.getFollowActivity());
			dfg.setEdgeWeight(e, t.getFrequency());
		}
		return dfg;
	}
	
	public Tuple<T> getMostRecentModel() {
		return mostRecentModel;
	}

	public void setMostRecentModel(
			Tuple<T> mostRecent) {
		this.mostRecentModel = mostRecent;
	}

	public Tuple<T> getCurrentModel() {
		return currentModel;
	}

	public void setCurrentModel(Tuple<T> current) {
		this.currentModel = current;
	}
}
