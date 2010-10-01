package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class LeftMergeFunction<T extends IClone> implements  IDataMergeFunction<T>{

	protected SDFAttributeList leftSchema;
	protected SDFAttributeList rightSchema;
	protected SDFAttributeList resultSchema;

	
	public LeftMergeFunction(SDFAttributeList leftSchema, SDFAttributeList rightSchema, SDFAttributeList resultSchema){
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.resultSchema = resultSchema;
	}
	
	public LeftMergeFunction(LeftMergeFunction<T> mf){
		this.leftSchema = mf.leftSchema.clone();
		this.rightSchema = mf.rightSchema.clone();
		this.resultSchema = mf.resultSchema.clone();
	}
	
	public T merge(T left, T right){
		return leftMerge(left, right);
	}
	
	public abstract T leftMerge(T left, T right);
	
	public abstract T createLeftFilledUp(T left);
	
	public LeftMergeFunction<T> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}
}
