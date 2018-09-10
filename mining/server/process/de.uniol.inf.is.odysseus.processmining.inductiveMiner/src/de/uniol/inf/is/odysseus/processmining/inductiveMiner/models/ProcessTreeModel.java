package de.uniol.inf.is.odysseus.processmining.inductiveMiner.models;

import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.SimpleDirectedGraph;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;

@SuppressWarnings("serial")
public class ProcessTreeModel extends SimpleDirectedGraph<Cut, DefaultEdge>
		implements Graph<Cut, DefaultEdge> {

	Cut rootCut;
	DirectedWeightedPseudograph<String, DefaultWeightedEdge> directlyFollowGraph;
	Multimap<Cut, Cut> processTreeMap;

	public ProcessTreeModel(
			Multimap<Cut, Cut> pTMap,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> directlyFollowGraph) {
		super(DefaultEdge.class);
		this.processTreeMap = HashMultimap.create(pTMap);
		createTree(pTMap);
		this.directlyFollowGraph = directlyFollowGraph;
	}

	/**
	 * Creates the process tree of the given map
	 * 
	 * @param processTreeMap
	 */
	private void createTree(Multimap<Cut, Cut> processTreeMap) {
		// Collect all nodes
		Set<Cut> nodes = Sets.newHashSet();
		for (Cut cut : processTreeMap.keySet()) {
			nodes.add(cut);
			for (Cut followerNode : processTreeMap.get(cut)) {
				nodes.add(followerNode);
			}
		}
		// create the process tree
		for (Cut node : nodes) {
			this.addVertex(node);
		}

		for (Cut cut : processTreeMap.keySet()) {
			for (Cut childCut : processTreeMap.get(cut)) {
				this.addEdge(cut, childCut);
			}
		}

		determineRootNode();
	}

	/**
	 * determines the root node by searching the node which has no incoming
	 * edges
	 */
	private void determineRootNode() {
		int size =0;
		for (Cut cut : this.vertexSet()) {
			if (cut.getAllNodes().size() > size) {
				rootCut = cut;
				size = cut.getAllNodes().size();
			}
		}
	}
	


	public DirectedWeightedPseudograph<String, DefaultWeightedEdge> getDirectlyFollowGraph() {
		return directlyFollowGraph;
	}

	public void setDirectlyFollowGraph(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> directlyFollowGraph) {
		this.directlyFollowGraph = directlyFollowGraph;
	}

	public Multimap<Cut, Cut> getProcessTreeMap() {
		return processTreeMap;
	}

	public void setProcessTreeMap(Multimap<Cut, Cut> processTreeMap) {
		this.processTreeMap = processTreeMap;
	}

	public Cut getRootCut() {
		if(rootCut == null){
			determineRootNode();
		}
		return rootCut;
	}

	public void setRootCut(Cut rootCut) {
		this.rootCut = rootCut;
	}

	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PROCESSTREE:\n");
		Multimap<Integer, Cut> levelMap = ArrayListMultimap.create();
		int lvl = 0;
		levelList(lvl,getRootCut(), levelMap);
		for(int i = 0 ; i< levelMap.keySet().size();i++){
			for(Cut cut : levelMap.get(i)){
				sb.append(cut.getOperator() +":"+cut.getAllNodes()+" ... ");
			}
			sb.append("\n");
		}
		return sb.toString();
		
	}
	
	/**
	 * Marks the nodes by their level 
	 * @param lvl
	 * @param cut
	 * @param levelMap
	 */
	private void levelList(int lvl,Cut cut, Multimap<Integer, Cut> levelMap) {
		levelMap.put(lvl, cut);
			for (Partition p : cut.getCutPartitions()) {
				for (Cut nextCut : processTreeMap.get(cut)) {		
				if (nextCut.getAllNodes().equals(p.getGraph().vertexSet())) {
					lvl++;
					if (!nextCut.getOperator().equals(
							OperatorType.SILENT)) {
						levelList(lvl,nextCut, levelMap);
					} else {
						levelMap.put(lvl, nextCut);
					}
					lvl--;
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((processTreeMap == null) ? 0 : processTreeMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessTreeModel other = (ProcessTreeModel) obj;
		if (processTreeMap == null) {
			if (other.processTreeMap != null)
				return false;
		} else if (!processTreeMap.equals(other.processTreeMap))
			return false;
		return true;
	}



	
}
