package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;



public class RelationalLeftMergeFunction<M extends IMetaAttribute> implements ILeftMergeFunction<Tuple<M>> {
	
	protected int schemaSize;

	protected SDFSchema leftSchema;
	protected SDFSchema rightSchema;
	protected SDFSchema resultSchema;

	
	public RelationalLeftMergeFunction(SDFSchema leftSchema, SDFSchema rightSchema, SDFSchema resultSchema){
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
	public Tuple<M> merge(Tuple<M> left,
			Tuple<M> right) {
		Object[] newAttributes = this.mergeAttributes(left != null ? left.getAttributes(): null, 
				right != null ? right.getAttributes() : null);
		Tuple<M> r = new Tuple<M>(newAttributes);
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
	@Override
    public Tuple<M> createLeftFilledUp(Tuple<M> original){
		// copy the original element, because otherwise the hashmap
		// in the left join will not work any more
		Tuple<M> retVal = new Tuple<M>(this.schemaSize);
		retVal.setMetadata(original.getMetadata());
		
		// add attribute values from left
		for(int i = 0; i<this.leftSchema.size(); i++){
			retVal.setAttribute(i, original.getAttribute(i));
		}
		
		// null values for the rest of the attributes are set automatically
		// since an array is always initialized with null values
		
		return retVal;
	}

	@Override
    public void init(){
	}
	
	@Override
    public RelationalLeftMergeFunction<M> clone(){
		return new RelationalLeftMergeFunction<M>(this);
	}
}
