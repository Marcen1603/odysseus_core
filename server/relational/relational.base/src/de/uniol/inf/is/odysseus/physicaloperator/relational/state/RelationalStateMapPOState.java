package de.uniol.inf.is.odysseus.physicaloperator.relational.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;

public class RelationalStateMapPOState<T extends IMetaAttribute> extends AbstractOperatorState {
	
	private static final long serialVersionUID = 3107458255925732610L;
	
	private Map<Object, List<Tuple<T>>> groupsLastObjects = new HashMap<>();

	public Map<Object, List<Tuple<T>>> getGroupsLastObjects() {
		return groupsLastObjects;
	}

	public void setGroupsLastObjects(Map<Object, List<Tuple<T>>> groupsLastObjects) {
		this.groupsLastObjects = groupsLastObjects;
	}
	

	@Override
	public Serializable getSerializedState() {
		return this;
	}

	@Override
	public long estimateSizeInBytes() {
		//As this state does not tend to be really big we return the real size
		return getSizeInBytesOfSerializable(this);
	}

}
