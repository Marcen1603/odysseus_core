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
 * A trajectory which has been converted from its raw representation to
 * a processable representation. This is utilized by various <tt>SpatialDistance</tt>
 * implementations. So <tt>E</tt> could be of whatever type that is useful
 * for further processing.
 * 
 * @author marcus
 *
 * @param <E> the type of the trajectory data
 * @param <T> the type of the encapsulated <tt>RawQueryTrajectory</tt>
 */
public interface IConvertedTrajectory<E, T extends IRawTrajectory> extends IHasTextualAttributes {

	/**
	 * Returns the data of the converted trajectory.
	 * 
	 * @return the data of the converted trajectory
	 */
	public E getData();
	
	/**
	 * Returns the encapsulated <tt>RawQueryTrajectory</tt>.
	 * 
	 * @return the encapsulated <tt>RawQueryTrajectory</tt>
	 */
	public T getRawTrajectory();
}
