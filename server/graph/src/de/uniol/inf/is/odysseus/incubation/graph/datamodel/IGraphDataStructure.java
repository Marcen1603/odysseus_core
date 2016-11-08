package de.uniol.inf.is.odysseus.incubation.graph.datamodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphEdge;
import de.uniol.inf.is.odysseus.incubation.graph.graphobject.GraphNode;

/**
 * Interface for creating a new GraphDataStructure.
 * 
 * @author Kristian Bruns
 */
public interface IGraphDataStructure<M extends IMetaAttribute> {
	
	public IGraphDataStructure<IMetaAttribute> newInstance(String name);
	
	public void setName(String name);
	
	public String getName();
	
	public String getType();
	
	public void editDataSet(KeyValueObject<M> object);
	
	public void deleteDataSet(KeyValueObject<M> object);
	
	public void addDataSet(KeyValueObject<M> object);
	
	public Map<String, GraphNode> getGraphNodes();
	
	public void addGraphNode(GraphNode node);
	
	public GraphNode getGraphNode(String nodeId);
	
	public void removeGraphNode(String nodeId);
	
	public GraphEdge getEdge(String n1Id, String n2Id);
	
	public Map<Pair<String, String>, GraphEdge> getRelations();

	public IGraphDataStructure<IMetaAttribute> cloneDataStructure();

	public void clearDataStructure();

	public void addRelation(GraphNode node1, GraphNode node2, GraphEdge edge);
}
