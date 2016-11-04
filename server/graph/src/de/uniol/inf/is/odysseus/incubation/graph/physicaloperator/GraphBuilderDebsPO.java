package de.uniol.inf.is.odysseus.incubation.graph.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.GraphBuilderDebsAO;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;

public class GraphBuilderDebsPO<M extends ITimeInterval> extends AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	private GraphBuilderDebsAO graphBuilderAo;
	private String actualDataStructure;
	
	// Map with post_id and actual structure reference.
	private Map<String, String> postDataStructures = new HashMap<String, String>();
	// Entities added with this Element
	private List<String> addedEntities = new ArrayList<String>();
	
	private Map<String, List<String>> commentsRelatedToPosts = new HashMap<String, List<String>>();
	
	public GraphBuilderDebsPO(GraphBuilderDebsAO ao) {
		super();
		this.graphBuilderAo = ao;
	}
	
	public GraphBuilderDebsPO(GraphBuilderDebsPO<M> graphBuilder) {
		super(graphBuilder);
		this.graphBuilderAo = graphBuilder.graphBuilderAo;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(KeyValueObject<M> object, int port) {
		// Datastructure for all posts and a datastructure for each post.
		IGraphDataStructure<IMetaAttribute> structure;
		IGraphDataStructure<IMetaAttribute> post = null;
		
		//Existiert bereits eine Datenstruktur für diesen Namen, wird die aktuelle Datenstruktur geklont, ansonsten wird eine neue Datenstruktur erstellt.
		if (this.actualDataStructure != null) {
			structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.actualDataStructure).cloneDataStructure();
		} else {
			structure = GraphBuilderDebsAO.dataStructureTypes.get(graphBuilderAo.getDataType()).newInstance(graphBuilderAo.getStructureName());
		}
		
		// Name of social network and set actual datastructure.
		String netName = GraphDataStructureProvider.getInstance().addGraphDataStructure(structure, object.getMetadata().getStart());
		this.actualDataStructure = netName;
		
		// Für einzelne Posts auch eigene Graphen anlegen
		String n1Id = (String) object.getAttribute("n1_id");
		String n2Id = (String) object.getAttribute("n2_id");

		String regExp = "[a-zA-Z]+[\\_]{1}[\\d]+";
		
		// Kombination von User und Comment wird nicht betrachtet.
		// Beide Ids müssen den Richtlinien einer ID entsprechen. 
		if (!(n1Id.contains("user") && n2Id.contains("comment")) && !(n1Id.contains("comment") && n2Id.contains("user")) && Pattern.matches(regExp, n1Id) && Pattern.matches(regExp, n2Id)) {
			String name="";
			
			// Welche posts sind betroffen. Hole aktuelle Datenstruktur und speichere Namen in der Map.
			// In dieser Aufgabe können nur folgende Möglichkeiten auftreten {post, comment}, {comment, post}, {comment, comment}
			if (n1Id != null && n1Id.contains("post")) {
				if (postDataStructures.containsKey(n1Id)) {
					post = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.postDataStructures.get(n1Id));
				} else {
					post = GraphBuilderDebsAO.dataStructureTypes.get(graphBuilderAo.getDataType()).newInstance(n1Id);
				}
				
				name = GraphDataStructureProvider.getInstance().addGraphDataStructure(post, object.getMetadata().getStart());
				postDataStructures.put(n1Id, name);
				if (n2Id != null) {
					if (commentsRelatedToPosts.containsKey(n1Id)) {
						commentsRelatedToPosts.get(n1Id).add(n2Id);
					} else {
						List<String> relatedComments = new ArrayList<String>();
						relatedComments.add(n2Id);
						commentsRelatedToPosts.put(n1Id, relatedComments);
					}
				}
			} else if (n2Id != null && n2Id.contains("post")) {
				if (postDataStructures.containsKey(n2Id)) {
					post = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.postDataStructures.get(n2Id));
				} else {
					post = GraphBuilderDebsAO.dataStructureTypes.get(graphBuilderAo.getDataType()).newInstance(n2Id);
				}
				
				name = GraphDataStructureProvider.getInstance().addGraphDataStructure(post, object.getMetadata().getStart());
				postDataStructures.put(n2Id, name);
				
				if (n1Id != null) {
					if (commentsRelatedToPosts.containsKey(n2Id)) {
						commentsRelatedToPosts.get(n2Id).add(n1Id);
					} else {
						List<String> relatedComments = new ArrayList<String>();
						relatedComments.add(n1Id);
						commentsRelatedToPosts.put(n2Id, relatedComments);
					}
				}
			}  else {
				// Werden zwei Kommentare übermittelt, muss geguckt werden zu welchem Post diese Kommentare gehören.
				String relatedPost = "";
				for (Map.Entry<String, List<String>> postList : commentsRelatedToPosts.entrySet()) {
					String toAdd = "";
					boolean postFound = false;
					for (String comm : postList.getValue()) {
						if (n1Id != null && n1Id.equals(comm)) {
							toAdd = n2Id;
							postFound = true;
							break;
						} else if (n2Id != null && n2Id.equals(comm)) {
							toAdd = n1Id;
							postFound = true;
							break;
						}
					}
					
					if (postFound) {
						postList.getValue().add(toAdd);
						relatedPost = postList.getKey();
						break;
					}
				}
				
				post = GraphDataStructureProvider.getInstance().getGraphDataStructure(postDataStructures.get(relatedPost));
				name = GraphDataStructureProvider.getInstance().addGraphDataStructure(post, object.getMetadata().getStart());
			}
			
			// Füge gefundene Relation sowohl der Gesamtdatenstruktur, als auch der Datenstruktur für den Post hinzu.
			if (object.getAttribute("method").equals("+")) {
				structure.addDataSet((KeyValueObject<IMetaAttribute>) object);
				if (post != null) {
					post.addDataSet((KeyValueObject<IMetaAttribute>) object);
				}
			} else if (object.getAttribute("method").equals("-")) {
				structure.deleteDataSet((KeyValueObject<IMetaAttribute>) object);
				if (post != null) {
					post.deleteDataSet((KeyValueObject<IMetaAttribute>) object);
				}
			} else if (object.getAttribute("method").equals("c")) {
				structure.editDataSet((KeyValueObject<IMetaAttribute>) object);
				if (post != null) {
					post.editDataSet((KeyValueObject<IMetaAttribute>) object);
				}
			}
			
			// Sende Tuple mit beiden Graphstrukturen weiter.
			Tuple<M> newTuple = new Tuple<M>(2, false);
			Graph graph;
			if (addedEntities.contains(n1Id) && addedEntities.contains(n2Id)) {
				graph = new Graph(netName);
			} else if (addedEntities.contains(n1Id) || n1Id == null) {
				graph = new Graph(netName, n2Id, null);
				addedEntities.add(n2Id);
			} else if (addedEntities.contains(n2Id) || n2Id == null) {
				graph = new Graph(netName, n1Id, null);
				addedEntities.add(n1Id);
			} else {
				graph = new Graph(netName, n1Id, n2Id);
				addedEntities.add(n1Id);
				addedEntities.add(n2Id);
			}
			
			Graph graph2 = new Graph(name, post.getName(), null);
			
			newTuple.setAttribute(0, graph);
			newTuple.setAttribute(1, graph2);

			newTuple.setMetadata((M) object.getMetadata().clone());
			
			transfer(newTuple);
		}
	}

}
