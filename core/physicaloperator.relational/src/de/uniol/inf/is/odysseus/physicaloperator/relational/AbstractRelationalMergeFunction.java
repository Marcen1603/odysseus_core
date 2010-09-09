package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractRelationalMergeFunction<T extends RelationalTuple<M>, M extends IMetaAttribute> implements IDataMergeFunction<T> {

	protected int schemaSize;
	
	protected AbstractRelationalMergeFunction(int outputSchemaSize){
		this.schemaSize = outputSchemaSize;
	}
	
	protected Object[] mergeAttributes(Object[] leftAttributes, Object[] rightAttributes){
		Object[] newAttributes = new Object[this.schemaSize];
		if (leftAttributes != null) {
			System.arraycopy(leftAttributes, 0, newAttributes, 0,
					leftAttributes.length);
		}
		if (rightAttributes != null) {
			System.arraycopy(rightAttributes, 0, newAttributes, this.schemaSize
					- rightAttributes.length, rightAttributes.length);
		}
		return newAttributes;
	}
	
	
	public abstract AbstractRelationalMergeFunction<T, M> clone();
}
