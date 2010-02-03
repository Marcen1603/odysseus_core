package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Implements a merge function for RelationalTuples. A full outer join is
 * implemented (left or right can be null), so make sure you pass the right
 * parameters to merge, if you don't want outer joins to happen.
 * 
 * @author Jonas Jacobi
 */
public class RelationalMergeFunction<M extends IMetaAttribute> implements
		IDataMergeFunction<RelationalTuple<M>> {

	private int resultSchemasize;


	public RelationalMergeFunction(int resultSchemasize) {
		this.resultSchemasize = resultSchemasize;
	}

	public RelationalTuple<M> merge(RelationalTuple<M> left,
			RelationalTuple<M> right) {
		Object[] newAttributes = new Object[resultSchemasize];
		if (left != null) {
			Object[] leftAttributes = left.getAttributes();
			System.arraycopy(leftAttributes, 0, newAttributes, 0,
					leftAttributes.length);
		}
		if (right != null) {
			Object[] rightAttributes = right.getAttributes();
			System.arraycopy(rightAttributes, 0, newAttributes, resultSchemasize
					- rightAttributes.length, rightAttributes.length);
		}
		RelationalTuple<M> r = new RelationalTuple<M>(newAttributes);
		return r;
	}
	
	public void init(){
	}
	
	public RelationalMergeFunction<M> clone() throws CloneNotSupportedException{
		return new RelationalMergeFunction<M>(resultSchemasize);
	}
	
}
