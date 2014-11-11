package de.uniol.inf.is.odysseus.physicaloperator.relational.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class RelationalStateMapPOState<T extends IMetaAttribute> implements Serializable {
	
	private static final long serialVersionUID = 3107458255925732610L;
	
	private Map<Long, LinkedList<Tuple<T>>> groupsLastObjects = new HashMap<>();

	public Map<Long, LinkedList<Tuple<T>>> getGroupsLastObjects() {
		return groupsLastObjects;
	}

	public void setGroupsLastObjects(Map<Long, LinkedList<Tuple<T>>> groupsLastObjects) {
		this.groupsLastObjects = groupsLastObjects;
	}
	

}
