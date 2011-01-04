package de.uniol.inf.is.odysseus.datamining.classification;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public interface IClassificationObject<U extends IMetaAttribute> {

	public void setClassLabel(Object classLabel);
	public Object getClassLabel();
	public Object[] getAttributes();
	public Object[] getClassificationAttributes();
	//public RelationalTuple<U> getLabeledTuple();
	public int getClassificationAttributeCount();
	RelationalTuple<U> getRestrictedTuple();
}
