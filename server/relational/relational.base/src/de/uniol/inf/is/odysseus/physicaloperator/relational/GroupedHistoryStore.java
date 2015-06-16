package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.relational.IProvidesMaxHistoryElements;

public class GroupedHistoryStore<T extends IStreamObject<? extends IMetaAttribute>> {

	private Map<Long, LinkedList<T>> groupsLastObjects = new HashMap<>();
	final private IGroupProcessor<T, T> groupProcessor;
	private int maxHistoryElements = 0;

	public GroupedHistoryStore(
			IGroupProcessor<T, T> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	public void determineMaxHistoryElements(
			IProvidesMaxHistoryElements[] expressions) {
		for (IProvidesMaxHistoryElements e: expressions){
			if (e.getMaxHistoryElements() > maxHistoryElements){
				maxHistoryElements = e.getMaxHistoryElements();
			}
		}
	}

	public void determineMaxHistoryElement(
			IProvidesMaxHistoryElements expression) {
		maxHistoryElements = expression.getMaxHistoryElements();
	}

	
	public void init() {
		IGroupProcessor<T, T> g = this.groupProcessor;
		synchronized (g) {
			g.init();
		}		
	}
	
	public LinkedList<T> process(T object){
		Long groupId = groupProcessor.getGroupID(object);
		LinkedList<T> lastObjects = groupsLastObjects.get(groupId);

		if (lastObjects == null) {
			lastObjects = new LinkedList<>();
			groupsLastObjects.put(groupId, lastObjects);
		}

		if (lastObjects.size() > maxHistoryElements) {
			lastObjects.removeLast();
		}
		lastObjects.addFirst(object);
		return lastObjects;
	}
	
	public Map<Long, LinkedList<T>> getGroupsLastObjects() {
		return groupsLastObjects;
	}
	
	public void setGroupsLastObjects(Map<Long, LinkedList<T>> groupsLastObjects) {
		this.groupsLastObjects = groupsLastObjects;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupProcessor == null) ? 0 : groupProcessor.hashCode());
		result = prime
				* result
				+ ((groupsLastObjects == null) ? 0 : groupsLastObjects
						.hashCode());
		result = prime * result + maxHistoryElements;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		GroupedHistoryStore other = (GroupedHistoryStore) obj;
		if (groupProcessor == null) {
			if (other.groupProcessor != null)
				return false;
		} else if (!groupProcessor.equals(other.groupProcessor))
			return false;
		if (groupsLastObjects == null) {
			if (other.groupsLastObjects != null)
				return false;
		} else if (!groupsLastObjects.equals(other.groupsLastObjects))
			return false;
		if (maxHistoryElements != other.maxHistoryElements)
			return false;
		return true;
	}

	

}
