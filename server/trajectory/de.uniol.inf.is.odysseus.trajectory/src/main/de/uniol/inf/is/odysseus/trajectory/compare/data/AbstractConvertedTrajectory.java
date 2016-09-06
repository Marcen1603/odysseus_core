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
 * Abstract implementation of <tt>IConvertedTrajectory</tt>. This class encapsulated
 * the <tt>RawQueryTrajectory</tt> specified by <tt>IConvertedTrajectory</tt>
 * 
 * @author marcus
 *
 * @param <E> the type of the trajectory data
 * @param <T> the type of the encapsulated <tt>RawQueryTrajectory</tt>
 */
public abstract class AbstractConvertedTrajectory<E, T extends RawQueryTrajectory> implements IConvertedTrajectory<E, T> {
	
	/** the encapsulated <tt>RawQueryTrajectory</tt>  */
	private final T rawTrajectory;
	
	/** the converted data */
	private final E convertedData;
	
	/**
	 * Creates an <tt>AbstractConvertedTrajectory</tt>
	 * 
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * 
	 * @throws IllegalArgumentException if <tt>rawTrajectory == null</tt>
	 *         or <tt>convertedData == null</tt>
	 */
	protected AbstractConvertedTrajectory(final T rawTrajectory, final E convertedData) {
		if(rawTrajectory == null) {
			throw new IllegalArgumentException("rawTrajectory is null");
		}
		if(convertedData == null) {
			throw new IllegalArgumentException("convertedData is null");
		}
		this.rawTrajectory = rawTrajectory;
		this.convertedData = convertedData;
	}
	
	@Override
	public T getRawTrajectory() {
		return this.rawTrajectory;
	}
	
	@Override
	public E getData() {
		return this.convertedData;
	}
}
