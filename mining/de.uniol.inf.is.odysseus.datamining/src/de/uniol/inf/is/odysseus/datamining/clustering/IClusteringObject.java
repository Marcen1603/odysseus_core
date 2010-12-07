package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public interface IClusteringObject<U extends IMetaAttribute> {

	public void setClusterId(int clusterId);
	public int getClusterId();
	public Object[] getAttributes();
	public Object[] getClusterAttributes();
	public RelationalTuple<U> getLabeledTuple();
	public int getClusterAttributeCount();
}
