/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.mining.clustering.model;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.mining.distance.IMetricFunctionValues;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This class represents an abstract cluster and can be used as a super class to
 * implement concrete clusters for different clustering algorithms. A cluster
 * contains an id and a ClusteringFeature.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractCluster<T extends IMetaAttribute, O> implements IMetricFunctionValues<O> {

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
	 * Returns a Tuple representing the cluster.
	 * The tuple contains the clusters id, the size of the cluster
	 * and the clusters center.
	 * 
	 * @return
	 */
	public Tuple<T> getRelationalCluster() {
		Object[] attributes = new Object[getCenter().getClusterAttributeCount() + 2];
		attributes[0] = getId();
		attributes[1] = getClusteringFeature().getSize();
		System.arraycopy(getCenter().getAttributes(), 0, attributes, 2,
				attributes.length - 2);

		return new Tuple<T>(attributes, false);
	}

	/**
	 * Returns the clusters center as an IClusteringObject.
	 * 
	 * @return the clusters center.
	 */
	public abstract IClusteringObject<T,O> getCenter();

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
	public void addTuple(IClusteringObject<T,O> element) {
		clusteringFeature.add(element.getClusterAttributes());
	}
	
	@Override
	public O[] getValues() {
		return getCenter().getClusterAttributes();		
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
	public void addCluster(AbstractCluster<T,O> cluster) {
		clusteringFeature.add(cluster.getClusteringFeature());
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy original cluster to copy.
	 */
	public AbstractCluster(AbstractCluster<T,O> copy) {
		this.id = copy.id;
		this.numberOfAttributes = copy.numberOfAttributes;
		this.clusteringFeature = copy.clusteringFeature.clone();
	}
}
