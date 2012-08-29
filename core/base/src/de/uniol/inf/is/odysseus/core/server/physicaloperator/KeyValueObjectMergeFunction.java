package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class KeyValueObjectMergeFunction<T extends KeyValueObject<M>, M extends IMetaAttribute> implements IDataMergeFunction<T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public T merge(T left, T right) {
		return (T) KeyValueObject.merge(left,right);
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public KeyValueObjectMergeFunction<T,M> clone(){
		return this;
	}

}
