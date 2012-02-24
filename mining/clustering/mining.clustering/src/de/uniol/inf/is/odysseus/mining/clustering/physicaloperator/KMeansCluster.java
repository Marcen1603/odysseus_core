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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.mining.clustering.model.AbstractCluster;
import de.uniol.inf.is.odysseus.mining.clustering.model.IClusteringObject;
import de.uniol.inf.is.odysseus.mining.clustering.model.RelationalClusteringObject;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class represents a cluster for the simple single pass k-means algorithm
 * implemented in the {@link SimpleSinglePassKMeansPO}.
 * 
 * 
 * @author Kolja Blohm
 * 
 */
public class KMeansCluster<T extends IMetaAttribute> extends AbstractCluster<T,Object> {

	/**
	 * Creates a new KMeansCluster.
	 * 
	 * @param numberOfAttributes
	 *            the number of attributes the inner ClusterinFeature can store.
	 */

	public KMeansCluster(int numberOfAttributes) {
		super(numberOfAttributes);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.datamining.clustering.AbstractCluster#getCenter
	 * ()
	 */
	@Override
	public IClusteringObject<T,Object> getCenter() {
		return new RelationalClusteringObject<T>(new RelationalTuple<T>(
				clusteringFeature.getMean()), getId());
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the KMeansCluster to copy.
	 */
	private KMeansCluster(KMeansCluster<T> copy) {
		super(copy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public KMeansCluster<T> clone() {
		return new KMeansCluster<T>(this);
	}
}
