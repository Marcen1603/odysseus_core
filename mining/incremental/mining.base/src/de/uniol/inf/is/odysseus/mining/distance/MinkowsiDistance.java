/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.distance;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * Class to calculate the minkowski distance, also referred to as Lp norm,
 * between an IClusteringObject and an AbstractCluster represented by its
 * center.
 * 
 * @author Kolja Blohm
 * 
 */
public class MinkowsiDistance<T extends IMetaAttribute> implements IDissimilarity<T> {

	int p;

	/**
	 * Creates a new MinkowsiDistance.
	 * 
	 * @param p
	 *            the p-value for this Lp norm
	 */
	public MinkowsiDistance(int p) {
		this.p = p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.clustering.IDissimilarity#
	 * getDissimilarity
	 * (de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject,
	 * de.uniol.inf.is.odysseus.core.server.datamining.clustering.AbstractCluster)
	 */
	@Override
	public Double getDissimilarity(IMetricFunctionValues<T> element, IMetricFunctionValues<T> cluster) {
		Double distance = 0D;
		Object[] elementAttributes = element.getValues();
		Object[] clusterAttributes = cluster.getValues();
		//Object[] clusterAttributes = cluster.getCenter().getClusterAttributes();

		for (int i = 0; i < elementAttributes.length; i++) {

			distance += Math.pow(
					Math.abs(Double.valueOf(elementAttributes[i].toString())
							- Double.valueOf(clusterAttributes[i].toString())),
					p);
		}
		return Math.pow(distance, 1.0 / p);
	}

}
