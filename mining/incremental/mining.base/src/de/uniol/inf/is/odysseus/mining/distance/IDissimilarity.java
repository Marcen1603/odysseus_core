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
package de.uniol.inf.is.odysseus.mining.distance;


/**
 * This interface represents dissimilarity functions to calculate
 * the dissimilarity between an IClusteringObject and an AbstractCluster.
 * 
 * 
 * @author Kolja Blohm
 *
 */
public interface IDissimilarity<T> {


	

	/**
	 * Calculates the dissimilarity between an IClusteringObject and
	 * and an AbstractCluster.
	 * 
	 * @param element the IClusteringObject.
	 * @param cluster the AbstractCluster.
	 * @return the dissimilarity between the cluster and the element.
	 */
	public Double getDissimilarity(IMetricFunctionValues<T> element, IMetricFunctionValues<T> cluster);
}
