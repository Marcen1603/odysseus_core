/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;

/**
 * An interface for finding the k-nearest data trajectories to a query trajectory.
 * 
 * @author marcus
 *
 * @param <T> the type of the data for the trajectories
 */
public interface IDistanceService<T> {

	/**
	 * Adds a <tt>IConvertedQueryTrajectory</tt> to this <i>IDistanceService</i> with the <i>k</i>
	 * as the maximum trajectories to find and <i>lambda</i> as the importance between spatial and 
	 * textual distance. By contract this has to be called first before <tt>getDistance</tt>
	 * can be invoked for the specified <i>queryTrajectory</i>.
	 * 
	 * @param queryTrajectory the query trajectory to add
	 * @param k the k-nearest trajectories to find
	 * @param lambda the importance between spatial and textual distance
	 */
	public void addQueryTrajectory(IConvertedQueryTrajectory<T> queryTrajectory, int k, double lambda);
	
	/**
	 * Adds the <i>dataTrajectory</i> to the  specified <i>queryTrajectory</i> and returns the k-nearest data
	 * trajectories.
	 * 
	 * @param queryTrajectory the corresponding query trajectory
	 * @param dataTrajectory the data trajectory to add
	 * @return the k-nearest data trajectories
	 */
	public List<IConvertedDataTrajectory<T>> getDistance(IConvertedQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
	
	/**
	 * Removes the stored <i>dataTrajectory</i> for the corresponding <i>queryTrajectory</i>.
	 * Trajectories may get removed when their <tt>TimeInterval</i> is illegal.
	 * 
	 * @param queryTrajectory the corresponding <i>queryTrajectory</i>
	 * @param dataTrajectory the data trajectory to be removed
	 */
	public void removeTrajectory(IConvertedQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
}
