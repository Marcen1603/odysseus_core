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

package de.uniol.inf.is.odysseus.trajectory.compare.data;


/**
 * An extension of <tt>IConvertedTrajectory</tt>. This trajectory is converted from 
 * a <tt>RawDataTrajectory</tt> which was built from a <tt>Tuple</tt> in a stream of
 * trajectories. This kind of trajectory has a <i>distance</i> which can be read and set.
 * 
 * @author marcus
 *
 * @param <E> the type of the trajectory data
 */
public interface IConvertedDataTrajectory<E> extends IConvertedTrajectory<E, RawDataTrajectory> {

	/**
	 * Returns the distance of this <tt>IConvertedDataTrajectory</tt>.
	 * 
	 * @return the distance of this <tt>IConvertedDataTrajectory</tt>.
	 */
	public double getDistance();
	
	/**
	 * Sets the distance of this <tt>IConvertedDataTrajectory</tt>.
	 */
	public void setDistance(double distance);
}
