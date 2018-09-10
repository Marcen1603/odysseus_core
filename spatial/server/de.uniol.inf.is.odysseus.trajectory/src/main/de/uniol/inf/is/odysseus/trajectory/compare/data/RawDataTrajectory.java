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

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * A special type of a <tt>RawQueryTrajectory</tt>. This kind of trajectory consists of all
 * data that is contained in one <tt>Tuple</tt> of a stream of query trajectories including
 * meta data.
 * 
 * @author marcus
 *
 */
public class RawDataTrajectory extends RawQueryTrajectory implements IHasTextualAttributes {

	/** the vehicles id of this trajectory */
	private final String vehicleId;
	
	/** the number indicating how many trajectories has been done before for this vehicleId */
	private final int trajectoryNumber;
	
	/** the textual attributes of this trajectory */
	private final Map<String, String> textualAttributes;
	
	/** the time interval of this trajectory */
	private final ITimeInterval timeInterval;
		
	/**
	 * Creates an instance of <tt>RawDataTrajectory</tt>.
	 * 
	 * @param points points the <tt>Points</tt> from which the internal <i>unmodifiable</i> 
	 *               <tt>List</tt> will be created
	 * @param vehicleId the vehicles id of this trajectory
	 * @param trajectoryNumber the number indicating how many trajectories has been done before for this vehicleId
	 * @param textualAttributes the textual attributes of this trajectory 
	 * @param timeInterval the time interval of this trajectory
	 * 
	 * @throws IllegalArgumentException if <tt>points == null</tt> or <tt>points.size() == 0</tt>
	 *         <tt>vehicleId == null</tt> or <tt>trajectoryNumber < 0</tt> or <tt>timeInterval == null</tt>
	 */
	public RawDataTrajectory(final List<Point> points, final String vehicleId, final int trajectoryNumber, final Map<String, String> textualAttributes,
			final ITimeInterval timeInterval) {
		
		super(points);
		
		if(vehicleId == null) {
			throw new IllegalArgumentException("vehicleId is null");
		}
		
		if(vehicleId.isEmpty()) {
			throw new IllegalArgumentException("vehicleId is empty");
		}
		
		if(trajectoryNumber < 0) {
			throw new IllegalArgumentException("trajectoryNumber is less than 0");
		}
		
		if(timeInterval == null) {
			throw new IllegalArgumentException("timeInterval is null");
		}
		
		this.vehicleId = vehicleId;
		this.trajectoryNumber = trajectoryNumber;
		this.textualAttributes = textualAttributes;
		this.timeInterval = timeInterval;
	}

	/**
	 * Returns the corresponding id of the <i>vehicle</i> belonging to this trajectory.
	 * 
	 * @return the corresponding id of the <i>vehicle</i> belonging to this trajectory
	 */
	public String getVehicleId() {
		return this.vehicleId;
	}


	/**
	 * Returns the number indicating how many trajectories has been done before for this vehicleId.
	 * 
	 * @return the number indicating how many trajectories has been done before for this vehicleId
	 */
	public int getTrajectoryNumber() {
		return this.trajectoryNumber;
	}

	/**
	 * Returns the time interval of this trajectory.
	 * 
	 * @return the time interval of this trajectory
	 */
	public ITimeInterval getTimeInterval() {
		return this.timeInterval;
	}
	
	@Override
	public Map<String, String> getTextualAttributes() {
		return this.textualAttributes;
	}

	@Override
	public int numAttributes() {
		return this.textualAttributes.size();
	}
}
