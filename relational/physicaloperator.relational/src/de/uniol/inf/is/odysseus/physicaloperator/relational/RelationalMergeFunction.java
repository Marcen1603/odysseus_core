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
public class RelationalMergeFunction<M extends IMetaAttribute> extends AbstractRelationalMergeFunction<RelationalTuple<M>, M> implements
		IDataMergeFunction<RelationalTuple<M>> {


	public RelationalMergeFunction(int resultSchemaSize) {
		super(resultSchemaSize);
	}
	
	private RelationalMergeFunction(RelationalMergeFunction<M> original){
		super(original.schemaSize);
	}
	

	public RelationalTuple<M> merge(RelationalTuple<M> left,
			RelationalTuple<M> right) {
		Object[] newAttributes = super.mergeAttributes(left != null ? left.getAttributes(): null, 
				right != null ? right.getAttributes() : null);
		RelationalTuple<M> r = new RelationalTuple<M>(newAttributes);
		return r;
	}
	
	public void init(){
	}
	
	public RelationalMergeFunction<M> clone(){
		return new RelationalMergeFunction<M>(this);
	}
}
