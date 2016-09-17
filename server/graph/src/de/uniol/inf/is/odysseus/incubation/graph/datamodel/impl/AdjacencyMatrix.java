package de.uniol.inf.is.odysseus.incubation.graph.datamodel.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;

public class AdjacencyMatrix<M extends IMetaAttribute> implements IGraphDataStructure<M> {
	
	private Table<String, String, GraphEdge> matrix = HashBasedTable.create();
	private Map<String, GraphNode> graphNodes = new HashMap<String, GraphNode>();
	
	private String name;
	
	@SuppressWarnings("unchecked")
	@Override
	public IGraphDataStructure<IMetaAttribute> newInstance(String name) {
		AdjacencyMatrix<M> instance = new AdjacencyMatrix<M>();
		instance.name = name;
		return (IGraphDataStructure<IMetaAttribute>) instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void addDataSet(KeyValueObject<M> object) {
		String n1Id = (String) object.getAttribute("n1_id");
		String n2Id = (String) object.getAttribute("n2_id");
		
		String regExp = "[a-zA-Z]+[\\_]{1}[\\d]+";
		
		Map<String, Object> attributes = object.getAttributesAsNestedMap();
		
		GraphNode node1 = null;
		GraphNode node2 = null;
		
		// Node 1 exists?
		if (n1Id != null && Pattern.matches(regExp, n1Id)) {
			if (graphNodes.containsKey(n1Id)) {
				node1 = graphNodes.get(n1Id);
			} else {
				node1 = new GraphNode(n1Id, (String) attributes.get("n1_label"));
			}
			
			if (attributes.get("n1_props") != null) {
				node1.setProps((Map<String, Object>) attributes.get("n1_props"));
			}
			graphNodes.put(n1Id, node1);
		}
		
		// Node2 exists?
		if (n2Id!= null && Pattern.matches(regExp, n2Id)) {
			if (graphNodes.containsKey(n2Id)) {
				node2 = graphNodes.get(n2Id);
			} else {
				node2 = new GraphNode(n2Id, (String) attributes.get("n2_label"));
			}
			
			if (attributes.get("n2_props") != null) {
				node2.setProps((Map<String, Object>) attributes.get("n2_props"));
			}
			graphNodes.put(n2Id, node2);
		}
		
		// Node1 and Node2 than create Edge.
		if (node1 != null && node2 != null) {
			String eLabel = (String) object.getAttribute("e_label");
			List<GraphNode> startingNodes = Collections.singletonList(node1);
			List<GraphNode> endingNodes = Collections.singletonList(node2);
			
			GraphEdge edge = new GraphEdge(eLabel, startingNodes, endingNodes);
			
			node1.addOutgoingEdge(edge, node2);
			node2.addIncomingEdge(edge, node1);
			
			this.matrix.put(n1Id, n2Id, edge);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void editDataSet(KeyValueObject<M> object) {
		String n1Id = (String) object.getAttribute("n1_id");
		String n2Id = (String) object.getAttribute("n2_id");
		
		Map<String, Object> attributes = object.getAttributesAsNestedMap();
		
		String regExp = "[a-zA-Z]+[\\_]{1}[\\d]+";
		
		if (Pattern.matches(regExp, n1Id) && Pattern.matches(regExp, n2Id)) {
			GraphEdge edge = matrix.get(n1Id, n2Id);
			edge.setLabel((String) attributes.get("e_label"));
			edge.setProps((Map<String, String>) attributes.get("e_props"));
			
		} else if (Pattern.matches(regExp, n1Id)) {
			graphNodes.get(n1Id).setProps((Map<String, Object>) attributes.get("n1_props"));
		}
	}

	@Override
	public synchronized void deleteDataSet(KeyValueObject<M> object) {
		String n1Id = (String) object.getAttribute("n1_id");
		String n2Id = (String) object.getAttribute("n2_id");
		
		String regExp = "[a-zA-Z]+[\\_]{1}[\\d]+";
		
		if (Pattern.matches(regExp, n1Id) && Pattern.matches(regExp, n2Id)) {
			this.matrix.remove(n1Id, n2Id);
			this.graphNodes.remove(n1Id);
			this.graphNodes.remove(n2Id);
		} else if (Pattern.matches(regExp, n2Id)) {
			this.graphNodes.remove(n1Id);
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getType() {
		return "AdjacencyMatrix";
	}

	@Override
	public IGraphDataStructure<IMetaAttribute> cloneDataStructure() {
		AdjacencyMatrix<IMetaAttribute> newMatrix = new AdjacencyMatrix<IMetaAttribute>();
		newMatrix.matrix = HashBasedTable.create(this.matrix);
		newMatrix.name = new String(this.name);
		newMatrix.graphNodes = new HashMap<String, GraphNode>(this.graphNodes);
		return newMatrix;
	}

	@Override
	public Map<String, GraphNode> getGraphNodes() {
		return this.graphNodes;
	}

	@Override
	public GraphEdge getEdge(String n1Id, String n2Id) {
		return this.matrix.get(n1Id, n2Id);
	}

	@Override
	public Map<Pair<String, String>, GraphEdge> getRelations() {
		Map<Pair<String, String>, GraphEdge> relations = new HashMap<Pair<String, String>, GraphEdge>();
		for (Cell<String, String, GraphEdge> cell : this.matrix.cellSet()) {
			relations.put(new Pair<String, String>(cell.getRowKey(), cell.getColumnKey()), cell.getValue());
		}
		
		return relations;
	}
	
	@Override
	public void clearDataStructure() {
		this.matrix.clear();
		this.graphNodes.clear();
	}
	
	@Override
	public synchronized void addRelation(GraphNode node1, GraphNode node2, GraphEdge edge) {
		this.matrix.put(node1.getId(), node2.getId(), edge);
		this.graphNodes.put(node1.getId(), node1);
		this.graphNodes.put(node2.getId(), node2);
		
		node1.addOutgoingEdge(edge, node2);
		node2.addIncomingEdge(edge, node1);
	}

	@Override
	public GraphNode getGraphNode(String nodeId) {
		return this.graphNodes.get(nodeId);
	}

	@Override
	public synchronized void removeGraphNode(String nodeId) {
		if (this.graphNodes.containsKey(nodeId)) {
			this.graphNodes.remove(nodeId);
		}
	
		if (matrix.containsRow(nodeId)) {
			matrix.row(nodeId).clear();
		}
		
		if (matrix.containsColumn(nodeId)) {
			matrix.column(nodeId).clear();
		}
	}

	@Override
	public synchronized void addGraphNode(GraphNode node) {
		this.graphNodes.put(node.getId(), node);
	}

	@Override
	public String toString() {
		return "AdjacencyMatrix [name=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		AdjacencyMatrix<IMetaAttribute> other = (AdjacencyMatrix<IMetaAttribute>) obj;
		if (graphNodes == null) {
			if (other.graphNodes != null)
				return false;
		} else if (!graphNodes.equals(other.graphNodes))
			return false;
		if (matrix == null) {
			if (other.matrix != null)
				return false;
		} else if (!matrix.equals(other.matrix))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
