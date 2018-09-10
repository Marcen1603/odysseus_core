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

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedTrajectory;

/**
 * An interface for calculating the <i>spatial distance</i> between a <tt>IConvertedTrajectory</tt>
 * and a <tt>IConvertedTrajectory</tt>.
 * 
 * @author marcus
 *
 * @param <T> the type of the data of the <tt>IConvertedTrajectory</tt> and the <tt>IConvertedTrajectory</tt>
 */
public interface ISpatialDistance<T> {

	/**
	 * Calculates and returns the <i>spatial distance</i> between a <i>queryTrajectory</i> and a 
	 * <i>dataTrajectory</i>. By contract this returns a value in range of <i>[0,1]</i>. If the distance returned
	 * is <i>0</i> both trajectories are <i>identical</i>. The higher the returned value the more apart 
	 * are both trajectories. A distance value of <i>1</i> means that the distance between both trajectories
	 * is at its most possible.
	 * 
	 * @param queryTrajectory the query trajectory
	 * @param dataTrajectory the data trajectory
	 * @return the <i>spatial distance</i> in range of [0,1]
	 */
	public double getDistance(IConvertedTrajectory<T, ?> queryTrajectory, IConvertedTrajectory<T, ?> dataTrajectory);
}
