package de.uniol.inf.is.odysseus.keyvalue.mergefunction;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

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
