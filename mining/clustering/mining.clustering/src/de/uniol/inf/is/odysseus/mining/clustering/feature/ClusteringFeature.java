/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.clustering.feature;

import de.uniol.inf.is.odysseus.mining.memory.ISnapshot;

/**
 * 
 * @author Dennis Geesen
 * Created at: 06.09.2011
 */
public abstract class ClusteringFeature<T> implements ISnapshot{

	private int numberOfPoints;
	private T linearSum;
	private T squareSum;
	
	public abstract void add(T toAdd);
	public abstract ClusteringFeature<T> merge(ClusteringFeature<T> right);
	
	public T getSquareSum() {
		return squareSum;
	}

	public void setSquareSum(T squareSum) {
		this.squareSum = squareSum;
	}

	public T getLinearSum() {
		return linearSum;
	}

	public void setLinearSum(T linearSum) {
		this.linearSum = linearSum;
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}	
	
	@Override
	public String toString() {
		return "\n\tLS: "+this.linearSum.toString()+"\n\tSS: "+this.squareSum.toString()+"\n\tN: "+this.numberOfPoints;		
	}
}
