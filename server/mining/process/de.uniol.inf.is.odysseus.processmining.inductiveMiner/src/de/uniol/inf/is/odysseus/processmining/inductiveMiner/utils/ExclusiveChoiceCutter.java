package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.List;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.OperatorType;

public class ExclusiveChoiceCutter extends AbstractCutter {

	/**
	 * Returns a XOR Cut if possible otherwise null will be returned
	 * 
	 * @param dGraph
	 * @return xor Cut if possible otherwise null
	 */

	@Override
	public Cut getCut(Partition partition) {

		ConnectivityInspector<String, DefaultWeightedEdge> connectivityInspector = new ConnectivityInspector<String, DefaultWeightedEdge>(
				partition.getGraph());
		List<Set<String>> connectedSets = connectivityInspector
				.connectedSets();
		
		return new Cut(getNewPartitionList(connectedSets, partition), partition,OperatorType.XOR);
	}
}
