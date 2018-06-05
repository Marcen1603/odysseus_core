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

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An interface for all algorithm that can compare trajectories.
 * 
 * @author marcus
 *
 */
public interface ITrajectoryCompareAlgorithm<T extends IConvertedDataTrajectory<E>, E> {

	/**
	 * Inserts the specified <i>trajectory</i> and returns the k-nearest trajectories.
	 * @param trajectory the trajectory to insert
	 * @return the k-nearest trajectories
	 */
	public List<T> getKNearest(RawDataTrajectory trajectory);
	
	/**
	 * Removes all trajectories which end timestamp is less than <i>time</i>.
	 * 
	 * @param time
	 */
	public void removeBefore(PointInTime time);
}
