package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;

public class KeyValueObjectMergeFunction<T extends KeyValueObject<M>, M extends IMetaAttribute> implements IDataMergeFunction<T,M> {
	
	@SuppressWarnings("unchecked")
	@Override
	public T merge(T left, T right, IMetadataMergeFunction<M> m, Order order) {
		return (T) left.merge(left,right, m, order);
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public KeyValueObjectMergeFunction<T,M> clone(){
		return this;
	}

}
