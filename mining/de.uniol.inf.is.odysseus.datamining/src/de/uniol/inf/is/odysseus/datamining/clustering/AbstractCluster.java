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

	public RelationalTuple<T> getRelationalCluster() {
		Object[] attributes = new Object[getCentre().getClusterAttributeCount() + 2];
		attributes[0] = getId();
		attributes[1] = getClusteringFeature().getCount();
		System.arraycopy(getCentre().getAttributes(), 0, attributes, 2,
				attributes.length - 2);

		return new RelationalTuple<T>(attributes);
	}

	public abstract IClusteringObject<T> getCentre();

	public int getId() {
		return id;
	}

	public void addTuple(IClusteringObject<T> element) {
		clusteringFeature.add(element.getClusterAttributes());
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addCluster(AbstractCluster<T> cluster) {
		clusteringFeature.add(cluster.getClusteringFeature());
	}

	public AbstractCluster(AbstractCluster<T> copy) {
		this.id = copy.id;
		this.clusteringFeature = copy.clusteringFeature.clone();
	}
}
