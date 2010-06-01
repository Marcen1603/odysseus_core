package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AbstractRelationalMergeFunction;

public class MVRelationalMergeFunction<M extends IProbability> extends AbstractRelationalMergeFunction<MVRelationalTuple<M>, M> implements IDataMergeFunction<MVRelationalTuple<M>> {

	
	public MVRelationalMergeFunction(int resultSchemaSize){
		super(resultSchemaSize);
	}
	
	public MVRelationalMergeFunction(MVRelationalMergeFunction<M> original){
		super(original.schemaSize);
	}
	
	@Override
	public MVRelationalTuple<M> merge(MVRelationalTuple<M> left, MVRelationalTuple<M> right){
		Object[] newAttributes = super.mergeAttributes(left != null ? left.getAttributes(): null, 
				right != null ? right.getAttributes() : null);
		MVRelationalTuple<M> r = new MVRelationalTuple<M>(newAttributes);
		return r;
	}
	
	public void init(){
	}
	
	public MVRelationalMergeFunction<M> clone(){
		return new MVRelationalMergeFunction<M>(this);
	}
}
