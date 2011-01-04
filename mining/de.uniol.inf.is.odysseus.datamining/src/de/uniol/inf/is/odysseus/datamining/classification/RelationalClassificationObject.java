package de.uniol.inf.is.odysseus.datamining.classification;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalClassificationObject<T extends IMetaAttribute> implements
		IClassificationObject<T> {

	private RelationalTuple<T> tuple;
	private RelationalTuple<T> restrictedTuple;
	private Object classLabel;
	private int labelAttributePosition;

	public RelationalClassificationObject(RelationalTuple<T> tuple, int[] restrictList, int labelAttributePosition) {
		this.labelAttributePosition = labelAttributePosition;
		this.tuple = tuple;
		this.restrictedTuple = tuple.restrict(restrictList, true);
	}
	
	@Override 
	
public RelationalTuple<T> getRestrictedTuple(){
		return restrictedTuple;
	}

	@Override
	public void setClassLabel(Object classLabel) {
		this.classLabel = classLabel;
	}

	@Override
	public Object getClassLabel() {
		return classLabel;
	}

	@Override
	public Object[] getAttributes() {
	return tuple.getAttributes();
	}

	@Override
	public Object[] getClassificationAttributes() {
		return restrictedTuple.getAttributes();
	}



	@Override
	public int getClassificationAttributeCount() {
		return restrictedTuple.getAttributeCount();
	}

	public RelationalTuple<T> getClassifiedTuple() {
		if(labelAttributePosition  < tuple.getAttributeCount()){
			RelationalTuple<T> newTuple = tuple.clone();
			newTuple.setAttribute(labelAttributePosition, getClassLabel());
			return newTuple;
		}
		else{
			return tuple.append(getClassLabel(),true);
		}
	}

}
