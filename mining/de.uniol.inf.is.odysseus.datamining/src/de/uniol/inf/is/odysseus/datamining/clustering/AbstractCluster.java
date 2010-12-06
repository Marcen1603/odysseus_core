package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractCluster<T extends IMetaAttribute> {

	protected int id;
	protected ClusteringFeature clusteringFeature;
	protected int attributeCount;
	
	public int getAttributeCount() {
		return attributeCount;
	}
	public AbstractCluster(int attributeCount) {
		this.clusteringFeature = new ClusteringFeature(attributeCount);
		this.attributeCount = attributeCount;
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
	
	public void addCluster(AbstractCluster<T> cluster){
		clusteringFeature.add(cluster.getClusteringFeature());
	}

	public AbstractCluster(AbstractCluster<T> copy) {
		this.id = copy.id;
		this.clusteringFeature = copy.clusteringFeature.clone();
	}
}
