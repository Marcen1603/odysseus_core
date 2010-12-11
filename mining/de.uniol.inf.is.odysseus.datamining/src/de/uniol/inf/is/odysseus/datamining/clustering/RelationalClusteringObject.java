package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalClusteringObject<T extends IMetaAttribute> implements
		IClusteringObject<T> {

	private RelationalTuple<T> tuple;
	private RelationalTuple<T> restrictedTuple;

	int clusterId;

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public RelationalClusteringObject(RelationalTuple<T> tuple, int[] restrictList) {
		clusterId = -1;
		this.tuple = tuple;
		this.restrictedTuple = tuple.restrict(restrictList, true);
	}

	public RelationalClusteringObject(RelationalTuple<T> restrictedTuple, int clusterid) {
		this.clusterId = clusterid;
		this.tuple =restrictedTuple;
		this.restrictedTuple = restrictedTuple;
	}
	
	public int getClusterId() {

		return clusterId;
	}

	@Override
	public Object[] getAttributes() {
		return tuple.getAttributes();
	}

	@Override
	public Object[] getClusterAttributes() {
		return restrictedTuple.getAttributes();
	}

	@Override
	public RelationalTuple<T> getLabeledTuple() {
		Object[] attributes = new Object[tuple.getAttributeCount() + 1];
		attributes[0] = getClusterId();

		System.arraycopy(tuple.getAttributes(), 0, attributes, 1,
				attributes.length - 1);

		return new RelationalTuple<T>(attributes);
	}

	@Override
	public int getClusterAttributeCount() {
		return restrictedTuple.getAttributeCount();
	}

}
