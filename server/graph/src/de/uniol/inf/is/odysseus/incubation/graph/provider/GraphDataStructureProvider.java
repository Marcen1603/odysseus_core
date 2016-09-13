package de.uniol.inf.is.odysseus.incubation.graph.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.listener.IGraphListener;

public class GraphDataStructureProvider {
	
	private static GraphDataStructureProvider instance;
	
	private Map<String, IGraphDataStructure<IMetaAttribute>> dataStructures = new HashMap<String, IGraphDataStructure<IMetaAttribute>>();
	private Map<String, String> actualDataStructures = new HashMap<String, String>();
	private Map<String, List<IGraphListener>> listeners = new HashMap<String, List<IGraphListener>>();
	
	public static GraphDataStructureProvider getInstance() {
		if (instance == null) {
			instance = new GraphDataStructureProvider();
		}
		return instance;
	}
	
	public String addGraphDataStructure(IGraphDataStructure<IMetaAttribute> structure, PointInTime ts) {
		String key = structure.getName() + "_" + ts;
		this.dataStructures.put(key, structure);
		this.actualDataStructures.put(structure.getName(), key);
		
		Graph graph = new Graph(key);
		
		if (this.listeners.get(structure.getName()) != null) {
			for (IGraphListener listener : this.listeners.get(structure.getName())) {
				listener.graphDataStructureChange(graph);
			}
		}
		
		return key;
	}
	
	public void removeGraphDataStructure(String name) {
		List<String> keysToRemove = new ArrayList<String>();
		for (String key : dataStructures.keySet()) {
			if (key.contains(name)) {
				keysToRemove.add(key);
			}
		}
		
		for (String key : keysToRemove) {
			this.dataStructures.remove(key);
		}
	}
	
	public IGraphDataStructure<IMetaAttribute> getGraphDataStructure(String name) {
		return this.dataStructures.get(name);
	}
	
	public Map<String, IGraphDataStructure<IMetaAttribute>> getStructuresMap() {
		return this.dataStructures;
	}
	
	public Set<String> getStructureNames() {
		return this.dataStructures.keySet();
	}
	
	public boolean structureNameExists(String name) {
		return this.dataStructures.containsKey(name);
	}
	
	public void addListener(IGraphListener listener, String structureName) {
		List<IGraphListener> structureListener = this.listeners.get(structureName);
		structureListener.add(listener);
		this.listeners.put(structureName, structureListener);
	}
	
	public void removeListener(IGraphListener listener) {
		this.listeners.remove(listener);
	}
	
	public String getActualStructure (String name) {
		return this.actualDataStructures.get(name);
	}
}
