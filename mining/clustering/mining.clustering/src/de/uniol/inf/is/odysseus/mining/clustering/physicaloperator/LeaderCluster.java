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
package de.uniol.inf.is.odysseus.mining.clustering.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.mining.clustering.model.AbstractCluster;
import de.uniol.inf.is.odysseus.mining.clustering.model.IClusteringObject;

/**
 * This class represents a cluster for the the leader algorithm implemented in
 * the {@link LeaderPO}.
 * 
 * 
 * @author Kolja Blohm
 * 
 */

public class LeaderCluster<T extends IMetaAttribute> extends AbstractCluster<T, Object> {

	IClusteringObject<T, Object> leader;

	/**
	 * Creates a new LeaderCluster.
	 * 
	 * @param numberOfAttributes
	 *            the number of attributes the inner ClusterinFeature can store.
	 */
	public LeaderCluster(int attributeCount) {
		super(attributeCount);

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster#getCenter()
	 */
	@Override
	public IClusteringObject<T, Object> getCenter() {
		return leader;
	}


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster#addTuple(de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject)
	 */
	@Override
	public void addTuple(IClusteringObject<T, Object> element) {
		super.addTuple(element);
		//sets the first element as this clusters leader
		if (leader == null) {
			leader = element;
		}
	}

}
