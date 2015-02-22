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

import java.util.Map;

/**
 * Abstract base class for all <i>converted data trajectories</i>. This class encapsulated 
 * the <i>converted data</i> and the <i>distance</i>.
 * @author marcus
 *
 * @param <E> the type of the data trajectory data
 */
public abstract class AbstractConvertedDataTrajectory<E> extends AbstractConvertedTrajectory<E, RawDataTrajectory> implements IConvertedDataTrajectory<E> {

	
	/** the distance */
	private double distance;
	
	/**
	 * Creates an instance of <tt>AbstractConvertedDataTrajectory</tt>.
	 * 
	 * @param rawTrajectory the <tt>RawDataTrajectory</tt> to encapsulate
	 * @param convertedData the converted data
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt>
	 */
	protected AbstractConvertedDataTrajectory(final RawDataTrajectory rawTrajectory, final E convertedData) {
		super(rawTrajectory, convertedData);
	}
	

	@Override
	public double getDistance() {
		return this.distance;
	}

	@Override
	public void setDistance(final double distance) {
		this.distance = distance;
	}
	
	@Override
	public Map<String, String> getTextualAttributes() {
		return this.getRawTrajectory().getTextualAttributes();
	}
	
	@Override
	public String toString() {
		return "[VehId: " + this.getRawTrajectory().getVehicleId() + "|" 
				+ "TrajId: " + this.getRawTrajectory().getTrajectoryNumber() + "|"
				+ "Dist: " + this.distance + "]";
	}

	@Override
	public int numAttributes() {
		return this.getRawTrajectory().numAttributes();
	}
}
