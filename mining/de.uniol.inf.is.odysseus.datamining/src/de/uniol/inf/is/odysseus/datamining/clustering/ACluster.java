package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class ACluster<T extends IMetaAttribute> {

	protected int id;
	protected ClusteringFeature clusteringFeature;
	
	public ACluster(int attributeCount) {
		this.clusteringFeature = new ClusteringFeature(attributeCount);
	}
	public ClusteringFeature getClusteringFeature() {
		return clusteringFeature;
	}

	public abstract RelationalTuple<T> getCentre();

	public int getId() {
		return id;
	}
	
	public void addTuple(RelationalTuple<T> tuple){
		clusteringFeature.add(tuple.getAttributes());
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
