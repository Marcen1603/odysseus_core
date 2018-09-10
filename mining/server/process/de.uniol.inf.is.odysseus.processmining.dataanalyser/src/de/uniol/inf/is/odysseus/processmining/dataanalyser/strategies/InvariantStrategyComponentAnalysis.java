package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTupleHelper;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantResultComponentAnalysis;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.IInvariantResult;

public class InvariantStrategyComponentAnalysis<T extends IMetaAttribute> extends InvariantStategyBase<T>{

	Tuple<T> mostRecentModel = null;
	Tuple<T> currentModel = null;
	InductiveMinerTransferTupleHelper<T> transferHelper;
	public InvariantStrategyComponentAnalysis(){
		super();
	}
	
	@Override
	public IInvariantResult calculateStrategy(Tuple<T> mostRecent, Tuple<T> current) {
		updateData(mostRecent, current);
		transferHelper = new InductiveMinerTransferTupleHelper<T>();
		InvariantResultComponentAnalysis result = new InvariantResultComponentAnalysis();
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentDFG = getGraph(transferHelper
				.getDirectlyFollowRelations(mostRecent));
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentDFG = getGraph(transferHelper
				.getDirectlyFollowRelations(current));
		
		StrongConnectivityInspector<String, DefaultWeightedEdge> mostRecentSCI = new StrongConnectivityInspector<String, DefaultWeightedEdge>(mostRecentDFG);
		StrongConnectivityInspector<String, DefaultWeightedEdge> currentSCI = new StrongConnectivityInspector<String, DefaultWeightedEdge>(currentDFG);
		
		result.setSciEqual(sciSetsEqual(mostRecentSCI,currentSCI));
		return result;
	}

	
	private boolean sciSetsEqual (StrongConnectivityInspector<String, DefaultWeightedEdge> mostRecentSCI,StrongConnectivityInspector<String, DefaultWeightedEdge> currentSCI ){
		return mostRecentSCI.stronglyConnectedSets().equals(currentSCI.stronglyConnectedSets());
	}

}
