package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.association.merge;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class AssociationDataMergeFunction<M extends IProbability & IPredictionFunction<RelationalTuple<M>, M> & ITimeInterval> 
		implements IDataMergeFunction<MVRelationalTuple<M>>{

	/**
	 * This is the resultschema of elements
	 * that have been merged
	 */
	SDFAttributeList schema;
	
	/**
	 * This is the schema of the left input stream
	 */
	SDFAttributeList leftSchema;
	
	/**
	 * This is the schema of the right input stream
	 */
	SDFAttributeList rightSchema;
	
	public AssociationDataMergeFunction(SDFAttributeList leftSchema, SDFAttributeList rightSchema, SDFAttributeList resultSchema){
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.schema = resultSchema;
	}
	
	@Override
	public void init() {
		// nothing to do
		
	}

	@Override
	/**
	 * Metadata will not be merged. Returned object contains no metadata
	 */
	public MVRelationalTuple<M> merge(MVRelationalTuple<M> left,
			MVRelationalTuple<M> right) {
		Object[] newAttributes = new Object[schema.size()];
		if (left != null && right != null) {
			Object[] leftAttributes = left.getAttributes();
			
			if(left.getMetadata().getStart().before(right.getMetadata().getStart())){
				System.arraycopy(left.getMetadata().predictData(this.leftSchema, left, right.getMetadata().getStart()).getAttributes(), 0, newAttributes, 0,
						leftAttributes.length);
				
				System.arraycopy(right.getAttributes(), 0, newAttributes, schema.size()
						- right.getAttributes().length, right.getAttributes().length);
			}
			else if(right.getMetadata().getStart().before(left.getMetadata().getStart())){
				System.arraycopy(left.getAttributes(), 0, newAttributes, 0,
						left.getAttributes().length);
				
				System.arraycopy(right.getMetadata().predictData(this.rightSchema, right, left.getMetadata().getStart()).getAttributes(), 0, newAttributes, schema.size()
						- right.getAttributes().length, right.getAttributes().length);
			}
			else{
				System.arraycopy(left.getAttributes(), 0, newAttributes, 0,
						left.getAttributes().length);
				System.arraycopy(right.getAttributes(), 0, newAttributes, schema.size()
						- right.getAttributes().length, right.getAttributes().length);
			}
		}

		MVRelationalTuple<M> r = new MVRelationalTuple<M>(newAttributes);
		r.findMeasurementValuePositions(this.schema);
		
		return r;
	}

}
