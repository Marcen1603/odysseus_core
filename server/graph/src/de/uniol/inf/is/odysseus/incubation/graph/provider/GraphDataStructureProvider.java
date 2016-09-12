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
	private Map<IGraphListener, String> listeners = new HashMap<IGraphListener, String>();
	
	public static GraphDataStructureProvider getInstance() {
		if (instance == null) {
			instance = new GraphDataStructureProvider();
		}
		return instance;
	}
	
	public String addGraphDataStructure(IGraphDataStructure<IMetaAttribute> structure, PointInTime ts) {
		String key = structure.getName() + "_" + ts;
		this.dataStructures.put(key, structure);
		
		Graph graph = new Graph(key);
		
		for (Map.Entry<IGraphListener, String> listener : listeners.entrySet()) {
			if (key.contains(listener.getValue())) {
				listener.getKey().graphDataStructureChange(graph);
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
		this.listeners.put(listener, structureName);
	}
	
	public void removeListener(IGraphListener listener) {
		this.listeners.remove(listener);
	}
}
