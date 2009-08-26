package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Implements a merge function for RelationalTuples. A full outer join is
 * implemented (left or right can be null), so make sure you pass the right
 * parameters to merge, if you don't want outer joins to happen.
 * 
 * @author Jonas Jacobi
 */
public class RelationalMergeFunction<M extends IClone> implements
		IDataMergeFunction<RelationalTuple<M>> {

	private SDFAttributeList schema;


	public RelationalMergeFunction(SDFAttributeList resultSchema) {
		this.schema = resultSchema;
	}

	public RelationalTuple<M> merge(RelationalTuple<M> left,
			RelationalTuple<M> right) {
		Object[] newAttributes = new Object[schema.size()];
		if (left != null) {
			Object[] leftAttributes = left.getAttributes();
			System.arraycopy(leftAttributes, 0, newAttributes, 0,
					leftAttributes.length);
		}
		if (right != null) {
			Object[] rightAttributes = right.getAttributes();
			System.arraycopy(rightAttributes, 0, newAttributes, schema.size()
					- rightAttributes.length, rightAttributes.length);
		}
		RelationalTuple<M> r = new RelationalTuple<M>(newAttributes);
		return r;
	}
	
	public void init(){
	}
}
