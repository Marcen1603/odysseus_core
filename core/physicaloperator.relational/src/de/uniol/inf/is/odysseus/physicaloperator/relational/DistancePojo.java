/** Copyright [2011] [The Odysseus Team]
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
/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 * 
 */
public class DistancePojo {
	private final static Double RADIUS = 6367000.0;

	@Init(meta = false)
	public List<Position> init(Object[] attr) {
		Position pos = new Position((Double) attr[0], (Double) attr[1]);
		List<Position> positions = new ArrayList<Position>();
		positions.add(pos);
		return positions;
	}

	@Merge(meta = false)
	public List<Position> merge(List<Position> partial, Object... attr) {
		Position pos = new Position((Double) attr[0], (Double) attr[1]);
		partial.add(pos);
		return partial;
	}

	@Evaluate
	public Double evaluate(List<Position> partial) {
		Double distance = 0.0;
		Double tmpLat = partial.get(0).latitude;
		Double tmpLng = partial.get(0).longitude;
		Double radian = Math.PI / 180;
		for (Position pos : partial) {
			Double deltaLatitude = Math.sin(radian * (tmpLat - pos.latitude)
					/ 2);
			Double deltaLongitude = Math.sin(radian * (tmpLng - pos.longitude)
					/ 2);
			Double circleDistance = 2 * Math.asin(Math.min(1, Math
					.sqrt(deltaLatitude * deltaLatitude
							+ Math.cos(radian * tmpLat)
							* Math.cos(radian * pos.latitude) * deltaLongitude
							* deltaLongitude)));
			distance += Math.abs(RADIUS * circleDistance);
			tmpLat = pos.latitude;
			tmpLng = pos.longitude;
		}
		return distance;
	}

	class Position {
		public final Double latitude;
		public final Double longitude;

		public Position(Double latitude, Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

	}
}
