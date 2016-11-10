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

/**
 * Provider saving versions from all graphs and remove versions if they are not used.
 * 
 * @author Kristian Bruns
 */
public class GraphDataStructureProvider {
	
	private static GraphDataStructureProvider instance;
	
	private Map<String, IGraphDataStructure<IMetaAttribute>> dataStructures = new HashMap<String, IGraphDataStructure<IMetaAttribute>>();
	private Map<String, List<IGraphListener>> listeners = new HashMap<String, List<IGraphListener>>();
	private Map<String, Map<String, Long>> operatorReads = new HashMap<String, Map<String, Long>>();
	
	public static GraphDataStructureProvider getInstance() {
		if (instance == null) {
			instance = new GraphDataStructureProvider();
		}
		return instance;
	}
	
	/**
	 * Save a version of a graph.
	 * 
	 * @param structure Graph datastructure.
	 * @param ts Graph creation time.
	 * 
	 * @return version reference.
	 */
	public String addGraphDataStructure(IGraphDataStructure<IMetaAttribute> structure, PointInTime ts) {
		String key = structure.getName() + "_" + ts;
		this.dataStructures.put(key, structure);
		
		Graph graph = new Graph(key);
		
		if (this.listeners.get(structure.getName()) != null) {
			for (IGraphListener listener : this.listeners.get(structure.getName())) {
				listener.graphDataStructureChange(graph);
			}
		}
		
		return key;
	}
	
	/**
	 * Delete a graph version.
	 * 
	 * @param name Name of graph version.
	 */
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
	
	/**
	 * Get graphDataStructure by version name.
	 * 
	 * @param name Name of graph version.
	 * 
	 * @return Datastructure saved under this name.
	 */
	public IGraphDataStructure<IMetaAttribute> getGraphDataStructure(String name) {
		return this.dataStructures.get(name);
	}
	
	/**
	 * Get all saved structures.
	 * 
	 * @return Map of structures.
	 */
	public Map<String, IGraphDataStructure<IMetaAttribute>> getStructuresMap() {
		return this.dataStructures;
	}
	
	/**
	 * Get all structure names.
	 * 
	 * @return Set of names.
	 */
	public Set<String> getStructureNames() {
		return this.dataStructures.keySet();
	}
	
	/**
	 * Is there a structure for the given name?
	 * 
	 * @param name Version name.
	 * 
	 * @return Boolean value, if structure exists.
	 */
	public boolean structureNameExists(String name) {
		return this.dataStructures.containsKey(name);
	}
	
	/**
	 * Add a listener related to the given graph.
	 * 
	 * @param listener Listener class.
	 * @param structureName Name of graph.
	 */
	public void addListener(IGraphListener listener, String structureName) {
		List<IGraphListener> structureListener = this.listeners.get(structureName);
		structureListener.add(listener);
		this.listeners.put(structureName, structureListener);
	}
	
	/**
	 * remove Listener.
	 * @param listener Listener class.
	 */
	public void removeListener(IGraphListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Remove one graph version.
	 * 
	 * @param versionName Name of version.
	 */
	public void removeDataStructureVersion (String versionName) {
		this.dataStructures.remove(versionName);
	}
	
	/**
	 * Method calling by operators to communicate which graph version this operator has read.
	 * 
	 * @param versionName Name of graph version.
	 * @param className Name of operator.
	 */
	public void setGraphVersionRead(String versionName, String className) {
		String[] parts = versionName.split("_");
		String graphName = parts[0];
		Long timestamp = Long.valueOf(parts[1]);
		
		Map<String, Long> operatorVersions;
		if (this.operatorReads.containsKey(graphName)) {
			operatorVersions = this.operatorReads.get(graphName);
		} else {
			operatorVersions = new HashMap<String, Long>();
		}
		
		operatorVersions.put(className, timestamp);
		this.operatorReads.put(graphName, operatorVersions);
		
		List<String> removeDataStructures = new ArrayList<String>();
		for (String key : this.dataStructures.keySet()) {
			if (key.contains(graphName)) {
				String [] versionParts = key.split("_");
				Long ts = Long.valueOf(versionParts[1]);
				
				boolean remove = true;
				for (Long tsRead : operatorVersions.values()) {
					if (tsRead <= ts) {
						remove = false;
					}
				}
				
				if (remove == true) {
					removeDataStructures.add(key);
				}
			}
		}
		
		// Muss über extra Liste geregelt werden, da ansonsten ConcurrentModificationException.
		for (String name : removeDataStructures) {
			this.removeDataStructureVersion(name);
		}
	}
}
