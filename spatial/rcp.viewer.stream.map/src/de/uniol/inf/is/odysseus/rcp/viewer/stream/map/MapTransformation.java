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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class MapTransformation {
	private Coordinate min = null;
	private Double scale = null;
	private Point center = null;
	private Rectangle map = null;

	private static final Logger LOG = LoggerFactory
			.getLogger(MapTransformation.class);

	public void update(Rectangle rectangle) {
		if (this.min != null) {
			min.x = ((rectangle.x + rectangle.width / 2 - this.center.x) * scale)
					+ min.x;
			min.y = ((rectangle.y + rectangle.height / 2 - this.center.y) * scale)
					+ min.y;
			try {
				scale = scale
						/ (Math.min(map.width / rectangle.width, map.height
								/ rectangle.height));
			} catch (ArithmeticException e) {
				LOG.debug(e.getMessage());
			}
		}
	}

	public void updateMapSize(Rectangle clientArea) {
		this.map = clientArea;
		center = new Point(clientArea.width / 2, clientArea.height / 2);
	}

	public void zoomin() {
		scale /= 2;
	}

	public void zoomout() {
		scale += scale;
	}

	public void panNorth() {
		min.y += scale;
	}

	public void panSouth() {
		min.y -= scale;
	}

	public void panWest() {
		min.x -= scale;
	}

	public void panEast() {
		min.x += scale;
	}

	public int[] transformCoord(Coordinate coord) {
		if (this.min == null) {
			this.min = (Coordinate) coord.clone();
			int[] uv = { new Double(center.x).intValue(),
					new Double(center.y).intValue() };
			return uv;
		} else {
			if (scale == null) {
				scale = (Math.abs(coord.x - min.x) + Math.abs(coord.y - min.y)
						/ this.map.width + this.map.height) * 2;
			}
			int[] uv = {
					new Double(center.x + (coord.x - min.x) / scale).intValue(),
					new Double(center.y + (coord.y - min.y) / scale).intValue() };
			return uv;
		}
	}

}