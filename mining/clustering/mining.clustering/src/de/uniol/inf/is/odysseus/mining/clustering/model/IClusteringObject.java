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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.mining.distance.IMetricFunctionValues;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This interface represents objects that can be clustered by the different
 * physical clustering operators.
 * 
 * @author kolja
 * 
 */
public interface IClusteringObject<U extends IMetaAttribute,O> extends IMetaAttributeContainer<U>, IMetricFunctionValues<O> {

	/**
	 * Sets the id of the cluster this object belongs to.
	 * 
	 * @param clusterId
	 *            the clusters id.
	 */
	public void setClusterId(int clusterId);

	/**
	 * Returns the id of the cluster this object belongs to. Should return -1 if
	 * the cluster is unknown.
	 * 
	 * @return the clusters id.
	 */
	public int getClusterId();

	/**
	 * Returns the attribute values of this object.
	 * 
	 * @return the attribute values.
	 */
	public O[] getAttributes();

	/**
	 * Returns the attribute values of this object that should be used for the
	 * clustering process.
	 * 
	 * 
	 * @return the attribute values used for the clustering process.
	 */
	public O[] getClusterAttributes();

	/**
	 * Returns a Tuple representing this object. The tuple contains
	 * all the object's attributes as well as the an extra attribute for the
	 * object's clusterID.
	 * 
	 * @return the Tuple representing this object labeled with a
	 *         clusterID.
	 */
	public Tuple<U> getLabeledTuple();

	/**
	 * Returns the number of attributes that should be used to cluster this
	 * object.
	 * 
	 * @return the number of attributes.
	 */
	public int getClusterAttributeCount();
}
