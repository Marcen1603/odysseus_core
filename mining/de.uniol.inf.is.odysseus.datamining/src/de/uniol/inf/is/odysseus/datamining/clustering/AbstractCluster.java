package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class represents an abstract cluster and can be used as a super class to
 * implement concrete clusters for different clustering algorithms. A cluster
 * contains an id and a ClusteringFeature.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractCluster<T extends IMetaAttribute> {

	protected int id;
	protected ClusteringFeature clusteringFeature;
	protected int numberOfAttributes;

	/**
	 * Returns the number of attributes the inner ClusteringFeature can store.
	 * 
	 * @return the number of attributes.
	 */
	public int getNumberOfAttributes() {
		return numberOfAttributes;
	}

	/**
	 * Constructor initializing a new AbstractCluster.
	 * 
	 * 
	 * @param numberOfAttributes
	 *            the number of attributes the inner ClusterinFeature can store.
	 */
	public AbstractCluster(int numberOfAttributes) {
		this.clusteringFeature = new ClusteringFeature(numberOfAttributes);
		this.numberOfAttributes = numberOfAttributes;
	}

	/**
	 * Returns the clusters ClusteringFeature.
	 * 
	 * @return the ClusteringFeature.
	 */
	public ClusteringFeature getClusteringFeature() {
		return clusteringFeature;
	}

	/**
	 * Returns a RelationalTuple representing the cluster.
	 * The tuple contains the clusters id, the size of the cluster
	 * and the clusters center.
	 * 
	 * @return
	 */
	public RelationalTuple<T> getRelationalCluster() {
		Object[] attributes = new Object[getCenter().getClusterAttributeCount() + 2];
		attributes[0] = getId();
		attributes[1] = getClusteringFeature().getSize();
		System.arraycopy(getCenter().getAttributes(), 0, attributes, 2,
				attributes.length - 2);

		return new RelationalTuple<T>(attributes);
	}

	/**
	 * Returns the clusters center as an IClusteringObject.
	 * 
	 * @return the clusters center.
	 */
	public abstract IClusteringObject<T> getCenter();

	/**
	 * Returns the clusters id.
	 * 
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Assigns a tuple to the cluster.
	 * 
	 * @param element the tuple to add.
	 */
	public void addTuple(IClusteringObject<T> element) {
		clusteringFeature.add(element.getClusterAttributes());
	}

	/**
	 * Sets the clusters id.
	 * 
	 * @param id the id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Merges an other cluster into this cluster by
	 * merging both ClusteringFeatures.
	 * 
	 * @param cluster the cluster to add.
	 */
	public void addCluster(AbstractCluster<T> cluster) {
		clusteringFeature.add(cluster.getClusteringFeature());
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy original cluster to copy.
	 */
	public AbstractCluster(AbstractCluster<T> copy) {
		this.id = copy.id;
		this.numberOfAttributes = copy.numberOfAttributes;
		this.clusteringFeature = copy.clusteringFeature.clone();
	}
}
