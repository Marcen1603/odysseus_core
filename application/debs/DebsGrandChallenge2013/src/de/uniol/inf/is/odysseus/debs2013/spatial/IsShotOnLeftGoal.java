
/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
 ******************************************************************************/
package de.uniol.inf.is.odysseus.debs2013.spatial;

import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Philipp Rudolph, Andreas Harre, Jan Sören Schwarz
 */

public class IsShotOnLeftGoal extends ShotOnGoalDetection{
	
	private static final long serialVersionUID = -7862407733680877781L;
	
	
	public IsShotOnLeftGoal() {
		super("IsShotOnLeftGoal");
		//GeometryFactory gf = new GeometryFactory();
		Coordinate[] coordinates = new Coordinate[2];
		coordinates[0] = new Coordinate(29880, -33968);
		coordinates[1] = new Coordinate(22560, -33968);
//		setGoal(gf.createLineString(coordinates));
	}

}