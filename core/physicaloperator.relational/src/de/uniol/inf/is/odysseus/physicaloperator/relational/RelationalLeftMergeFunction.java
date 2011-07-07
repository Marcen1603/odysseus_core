package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



public class RelationalLeftMergeFunction<M extends IMetaAttribute> implements ILeftMergeFunction<RelationalTuple<M>> {
	
	protected int schemaSize;

	protected SDFAttributeList leftSchema;
	protected SDFAttributeList rightSchema;
	protected SDFAttributeList resultSchema;

	
	public RelationalLeftMergeFunction(SDFAttributeList leftSchema, SDFAttributeList rightSchema, SDFAttributeList resultSchema){
		this.schemaSize = resultSchema.size();
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.resultSchema = resultSchema;
	}
	
	public RelationalLeftMergeFunction(RelationalLeftMergeFunction<M> mf){
		this.schemaSize = resultSchema.size();
		this.leftSchema = mf.leftSchema.clone();
		this.rightSchema = mf.rightSchema.clone();
		this.resultSchema = mf.resultSchema.clone();
	}

	
	@Override
	public RelationalTuple<M> merge(RelationalTuple<M> left,
			RelationalTuple<M> right) {
		Object[] newAttributes = this.mergeAttributes(left != null ? left.getAttributes(): null, 
				right != null ? right.getAttributes() : null);
		RelationalTuple<M> r = new RelationalTuple<M>(newAttributes);
		return r;
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
	
	/**
	 * This method creates a list of nodes in which the attributes of
	 * the left schema are set an the attributes of the right schema are
	 * null. The metadata is used from the original.
	 */
	public RelationalTuple<M> createLeftFilledUp(RelationalTuple<M> original){
		// copy the original element, because otherwise the hashmap
		// in the left join will not work any more
		RelationalTuple<M> retVal = new RelationalTuple<M>(this.schemaSize);
		retVal.setMetadata(original.getMetadata());
		
		// add attribute values from left
		for(int i = 0; i<this.leftSchema.size(); i++){
			retVal.setAttribute(i, original.getAttribute(i));
		}
		
		// null values for the rest of the attributes are set automatically
		// since an array is always initialized with null values
		
		return retVal;
	}

	public void init(){
	}
	
	public RelationalLeftMergeFunction<M> clone(){
		return new RelationalLeftMergeFunction<M>(this);
	}
}
