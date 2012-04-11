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

import java.util.LinkedList;

import org.eclipse.swt.graphics.GC;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class Layer {

	private LinkedList<Geometry> geometries = null;
	private MapTransformation transformation = null;
	private SDFAttribute sdfAttribute = null;
	private Style style = null;

	public Layer(MapTransformation transformation, SDFAttribute sdfAttribute,
			Style style) {
		this.geometries = new LinkedList<Geometry>();
		this.transformation = transformation;
		this.sdfAttribute = sdfAttribute;
		this.style = style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public Style getStyle() {
		return style;
	}

	public SDFAttribute getSdfAttribute() {
		return sdfAttribute;
	}

	public void drawGeometries(GC gc) {
		synchronized (geometries) {
			for (Geometry geometry : geometries) {
				drawGeometry(geometry, gc);
			}
		}
	}

	private void drawGeometry(Geometry geometry, GC gc) {
		if (geometry instanceof Point) {
			drawPoint((Point) geometry, gc);
		} else if (geometry instanceof LineString) {
			drawLineString((LineString) geometry, gc);
		} else if (geometry instanceof Polygon) {
			drawPolygon((Polygon) geometry, gc);
		} else if (geometry instanceof GeometryCollection) {
			drawGeometryCollection((GeometryCollection) geometry, gc);
		}
	}

	private void drawGeometryCollection(GeometryCollection geometryCollection,
			GC gc) {
		for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
			drawGeometry(geometryCollection.getGeometryN(i), gc);
		}

	}

	private void drawPoint(Point point, GC gc) {
		int[] uv = transformation.transformCoord(point.getCoordinate());
		this.style.draw(gc, uv);
	}

	private void drawLineString(LineString lineString, GC gc) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints()
				+ lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord);
			path[i++] = uv[0];
			path[i++] = uv[1];
		}
		this.style.draw(gc, path);
	}

	private int[] drawLinearRing(LineString lineString, GC gc) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints()
				+ lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord);
			path[i++] = uv[0];
			path[i++] = uv[1];
		}
		return path;
	}

	private void drawPolygon(Polygon polygon, GC gc) {
		int[][] list = new int[polygon.getNumInteriorRing() + 1][];
		list[0] = drawLinearRing(polygon.getExteriorRing(), gc);
		for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
			list[n + 1] = drawLinearRing(polygon.getInteriorRingN(n), gc);
		}
		if (this.style instanceof PolygonStyle)
			((PolygonStyle) this.style).draw(gc, list);
	}

	public void addGeometry(Geometry geometry) {
		synchronized (geometries) {
			this.geometries.offer(geometry);
		}
	}

	public void removeLast() {
		synchronized (geometries) {
			this.geometries.poll();
		}
	}
	
	public void clean() {
		synchronized (geometries) {
			this.geometries.clear();
		}
	}
}
