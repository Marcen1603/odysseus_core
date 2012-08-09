/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.swt.graphics.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;


/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ScreenTransformation {

	private static final Logger LOG = LoggerFactory
			.getLogger(ScreenTransformation.class);

	
    private AtomicLong zoomStamp = new AtomicLong();
	
	private boolean update = false;
	private Point min = new Point(0, 0);
	
	
	
	public Point getMin() {
		return min;
	}

	public void setMin(Point min) {
		this.min = min;
	}

	public boolean isUpdate() {
		return update;
	}

	public int[] transformCoord(Coordinate coordinate, int srid) {
		int[] transformedCoordinate = new int[2];
		//LOG.debug("SRID: " + srid);
		
//		if(map != null){
//			Point point = map.computePosition(new PointD(coordinate.x, coordinate.y));
//			Point offset = map.getMapPosition();
//			transformedCoordinate[0] = point.x - offset.x;
//			transformedCoordinate[1] = point.y - offset.y;		
//		}
		
		
		return transformedCoordinate;
	}

	public void panNorth(int steps) {
		min.y += steps;
		update = true;
		LOG.debug("Pan North: " + min.y);
	}

	public void panSouth(int steps) {
		min.y -= steps;
		update = true;
		LOG.debug("Pan South: " + min.y);
	}

	public void panWest(int steps) {
		min.x += steps;
		update = true;
		LOG.debug("Pan West: " + min.x);
	}

	public void panEast(int steps) {
		min.x -= steps;
		update = true;
		LOG.debug("Pan East: " + min.x);
	}

	public AtomicLong getZoomStamp() {
		return zoomStamp;
	}

	public void setZoomStamp(AtomicLong zoomStamp) {
		this.zoomStamp = zoomStamp;
	}

}

//
//
// private Point center = new Point(0,0);
// private Coordinate min = new Coordinate(0, 0);
//
// private double scale = 1;
// private boolean update = true;
//
// private Rectangle currentScreen = new Rectangle(0, 0, 0, 0);
// private Rectangle originScreen = new Rectangle(0, 0, 0, 0);
//
// //-180.0, -85.051128779799996, 180.0, 85.051128779799996
//
// //http://www.openstreetmap.org/?minlon=0.0&minlat=40.0&maxlon=10.0&maxlat=50&box=yes
//
// private double minLat = 40;
// private double maxLat = 50;
// private double minLon = 0;
// private double maxLon = 10;
//
//
// //private double minLat = 85.051128779799996;
// //private double maxLat = -85.051128779799996;
// //private double minLon = -180;
// //private double maxLon = 180;
//
// //private BoundingBox bbox = new BoundingBox("EPSG:3875",minLon ,minLat ,
// maxLon, maxLat);
// private BoundingBox bbox = new BoundingBox("EPSG:4326",minLon ,minLat ,
// maxLon, maxLat);
//
// public BoundingBox getBbox() {
// return bbox;
// }
//
// public void setBbox(BoundingBox bbox) {
// this.bbox = bbox;
// }
//
// public void updateOrigin(Rectangle rectangle) {
// LOG.debug("Update Origin");
// originScreen = rectangle;
// updateCurrent(rectangle);
// update = true;
// }
//
// public void updateCurrent(Rectangle rectangle) {
// LOG.debug("Update Current");
// currentScreen = rectangle;
//
// //center.x = currentScreen.width / 2;
// //center.y = currentScreen.height / 2;
//
// min.x = ((rectangle.x + rectangle.width / 2 - this.center.x) * scale)
// + min.x;
// min.y = ((rectangle.y + rectangle.height / 2 - this.center.y) * scale)
// + min.y;
// try {
// scale = scale
// / (Math.min(currentScreen.width / rectangle.width,
// currentScreen.height / rectangle.height));
//
// if (scale == 0)
// scale = 1;
//
// } catch (ArithmeticException e) {
// LOG.debug(e.getMessage());
// }
// update = true;
// }
//
// public int[] transformCoord(Coordinate coord, int srid) {
// if (coord.z != Double.MAX_VALUE) {
// coord.z = Double.MAX_VALUE;
// int[] scCoord = convertGeoToPixel(coord.x, coord.y);
//
// coord.x = new Integer(scCoord[0]);
// coord.y = new Integer(scCoord[1]);
// }
//
// if (this.min == null) {
// this.min = (Coordinate) coord.clone();
// int[] uv = { new Double(center.x).intValue(),
// new Double(center.y).intValue() };
// return uv;
// } else {
// // if (Double.MAX_VALUE == Double.MAX_VALUE) {
// // scale = (int) (Math.abs(coord.x - min.x) + Math.abs(coord.y - min.y) /
// this.currentScreen.width + this.currentScreen.height) * 2;
// // }
// int[] uv = {
// new Double(center.x + (coord.x - min.x) / scale).intValue(),
// new Double(center.y + (coord.y - min.y) / scale).intValue() };
// //new Double(center.x + (coord.x - min.x) / scale).intValue(),
// //new Double(center.y + (coord.y - min.y) / scale).intValue() };
// return uv;
// }
// }
//
// public void zoomin(double factor) {
// LOG.debug("Scale: " + scale);
// scale -= factor;
// // currentScreen.y -= scale;
// // currentScreen.height -= scale;
// // currentScreen.x -= scale;
// // currentScreen.width -= scale;
// update = true;
// LOG.debug("Zoom In: " + scale);
// }
//
// public void zoomout(double factor) {
// LOG.debug("Scale: " + scale);
// scale += factor;
// // currentScreen.y += scale;
// // currentScreen.height += scale;
// // currentScreen.x += scale;
// // currentScreen.width += scale;
// update = true;
// LOG.debug("Zoom Out: " + scale);
// }
//
// public void panNorth() {
// currentScreen.y += scale;
// currentScreen.height += scale;
// min.y += scale;
// update = true;
// LOG.debug("Pan North: " + min.y);
// }
//
// public void panSouth() {
// currentScreen.y -= scale;
// currentScreen.height -= scale;
// min.y -= scale;
// update = true;
// LOG.debug("Pan South: " + min.y);
// }
//
// public void panWest() {
// currentScreen.x -= scale;
// currentScreen.width -= scale;
// min.x -= scale;
// update = true;
// LOG.debug("Pan West: " + min.x);
// }
//
// public void panEast() {
// currentScreen.x += scale;
// currentScreen.width += scale;
// min.x += scale;
// update = true;
// LOG.debug("Pan East: " + min.x);
// }
//
// public double getScale() {
// return scale;
// }
//
// public Rectangle getCurrentScreen() {
// return currentScreen;
// }
//
// public Rectangle getOriginScreen() {
// return originScreen;
// }
//
// public void update(boolean update) {
// this.update = update;
// }
//
// public boolean hasUpdate() {
// return this.update;
// }
//
// public double getMinLat() {
// return minLat;
// }
//
// public void setMinLat(double minLat) {
// this.minLat = minLat;
// }
//
// public double getMaxLat() {
// return maxLat;
// }
//
// public void setMaxLat(double maxLat) {
// this.maxLat = maxLat;
// }
//
// public double getMinLon() {
// return minLon;
// }
//
// public void setMinLon(double minLon) {
// this.minLon = minLon;
// }
//
// public double getMaxLon() {
// return maxLon;
// }
//
// public void setMaxLon(double maxLon) {
// this.maxLon = maxLon;
// }
//
// public double[] epsg4326To(int epsg, double x, double y) {
// // based on http://lists.osgeo.org/pipermail/proj4j/2010-May/000041.html
//
// double[] coordinates = new double[2];
//
// CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
// CRSFactory csFactory = new CRSFactory();
//
// /*
// * Create {@link CoordinateReferenceSystem} & CoordinateTransformation.
// * Normally this would be carried out once and reused for all
// * transformations
// */
//
// CoordinateReferenceSystem crs1 = csFactory.createFromName("EPSG:" + 4326);
// CoordinateReferenceSystem crs2 = csFactory.createFromName("EPSG:"+ epsg);
//
// LOG.debug("Transformation");
// LOG.debug("(source) Datum: " + crs1.getDatum());
// LOG.debug("(source) Name: " + crs1.getName());
// LOG.debug("(source) Parameter: " + crs1.getParameterString());
// LOG.debug("(source) Projection: " + crs1.getProjection());
//
// LOG.debug("(target) Datum: " + crs2.getDatum());
// LOG.debug("(target) Name: " + crs2.getName());
// LOG.debug("(target) Parameter: " + crs2.getParameterString());
// LOG.debug("(target) Projection: " + crs2.getProjection());
//
// CoordinateTransform trans = ctFactory.createTransform(crs1, crs2);
//
// /*
// * Create input and output points. These can be constructed once per
// * thread and reused.
// */
//
// ProjCoordinate source = new ProjCoordinate();
// ProjCoordinate target = new ProjCoordinate();
// source.x = x;
// source.y = y;
// //LOG.debug("---------------------------------------------------");
// //LOG.debug("(source) Coordinate: " + source.x + ", " + source.y);
//
// /*
// * Transform point
// */
// trans.transform(source, target);
// coordinates[0] = target.x;
// coordinates[1] = target.y;
// // LOG.debug("(target) Coordinate: " + target.x + ", " + target.y);
// // LOG.debug("---------------------------------------------------");
//
// return coordinates;
// }
//
// public double[] screenToEpsg4326(int x, int y) {
// double coordinate[] = new double[2];
// // Quadrant 1: -/+
// if ((x < (currentScreen.width / 2)) && (y < (currentScreen.height / 2))) {
// // X-Negative
// coordinate[0] = minLat - ((x * minLat) / (currentScreen.width / 2));
// // Y-Positive
// coordinate[1] = minLon
// - ((y * minLon) / (currentScreen.height / 2));
// return coordinate;
// }
//
// // Quadrant 3: +/-
// if ((x >= (currentScreen.width / 2))
// && (y >= (currentScreen.height / 2))) {
// // X-Positive
// coordinate[0] = (-1)
// * (maxLat - ((x * maxLat) / (currentScreen.width / 2)));
// // Y-Negative
// coordinate[1] = (-1)
// * (maxLon - ((y * maxLon) / (currentScreen.height / 2)));
// return coordinate;
// }
//
// // Quadrant 2: +/+
// if ((x >= (currentScreen.width / 2))
// && (y < (currentScreen.height / 2))) {
// // X-Positive
// coordinate[0] = (-1)
// * (maxLat - ((x * maxLat) / (currentScreen.width / 2)));
// // Y-Positive
// coordinate[1] = (minLon - ((y * minLon) / (currentScreen.height / 2)));
// return coordinate;
// }
//
// // Quadrant 4: -/-
// if ((x < (currentScreen.width / 2))
// && (y >= (currentScreen.height / 2))) {
// // X-Negative
// coordinate[0] = minLat - ((x * minLat) / (currentScreen.width / 2));
// // Y-Negative
// coordinate[1] = (-1)
// * (maxLon - ((y * maxLon) / (currentScreen.height / 2)));
// return coordinate;
// }
// return coordinate;
// }
//
// public int[] epsg4326ToScreen(double x, double y) {
// int coordinate[] = new int[2];
// int width = (currentScreen.width / 2);
// int height = (currentScreen.height / 2);
//
// // Quadrant 2: +/+
// if ((x >= 0) && (y >= 0)) {
// // X-Positive
// coordinate[0] = (int) (width + ((x * width) / maxLat));
// // Y-Negative
// coordinate[1] = (int) (height + ((y * height) / maxLon));
// return coordinate;
// }
//
// // Quadrant 4: -/-
// if ((x < 0) && (y < 0)) {
// // X-Negative
// coordinate[0] = (currentScreen.width / 2)
// - (int) ((x * (currentScreen.width / 2)) / minLat);
// // Y-Positive
// coordinate[1] = (currentScreen.height / 2)
// + (int) ((y * (currentScreen.height / 2)) / maxLon);
// return coordinate;
// }
//
// // Quadrant 3: +/-
// if ((x >= 0) && (y < 0)) {
// // X-Negative
// coordinate[0] = (currentScreen.width / 2) + (-1)
// * (int) ((x * (currentScreen.width / 2)) / minLat);
// // Y-Negative
// coordinate[1] = (currentScreen.height / 2)
// + (int) ((y * (currentScreen.height / 2)) / maxLon);
// return coordinate;
// }
//
// // Quadrant 1: -/+
// if ((x < 0) && (y >= 0)) {
// // X-Positive
// coordinate[0] = (currentScreen.width / 2)
// + (int) ((x * (currentScreen.width / 2)) / maxLat);
// // Y-Positive
// coordinate[1] = (currentScreen.height / 2)
// - (int) ((y * (currentScreen.height / 2)) / minLon);
// return coordinate;
// }
//
// return coordinate;
// }
//
// public double[] test(double x, double y) {
// double coordinate[] = new double[2];
//
// coordinate[0] = new Double(x);
// coordinate[1] = new Double(y);
//
// return coordinate;
// }
//
// public int[] mercator_position(double lat, double lng) {
// int coordinate[] = new int[2];
//
// coordinate[0] = (int) (currentScreen.width * (180 + lng) / 360)
// % currentScreen.width;
//
// double radlat = lat * Math.PI / 180;
// double newY = Math.log(Math.tan((radlat / 2) + (Math.PI / 4)));
//
// coordinate[1] = (int) ((currentScreen.height / 2) - (currentScreen.width *
// newY / (2 * Math.PI)));
//
// return coordinate;
// }
//
// public double[] equirectangular_position(double lat, double lng) {
// double coordinate[] = new double[2];
//
// coordinate[0] = ((lng + 180) * (currentScreen.width / 360));
// coordinate[1] = (((lat * -1) + 90) * (currentScreen.height / 180));
//
// return coordinate;
// }
//
// public int[] toScreen(double lat, double lon) {
// LOG.debug("Input (lat/lon): " + lat + "/" + lon);
// double mapLonDelta = maxLat - minLat;
// double mapLatBottomDegree = maxLon * Math.PI / 180;
// int coordinate[] = new int[2];
//
// coordinate[0] = (int) ((lon - minLat) * (currentScreen.width / mapLonDelta));
//
// lat = lat * Math.PI / 180;
// double worldMapWidth = ((currentScreen.width / mapLonDelta) * 360)
// / (2 * Math.PI);
// double mapOffsetY = (worldMapWidth / 2 * Math.log((1 + Math
// .sin(mapLatBottomDegree)) / (1 - Math.sin(mapLatBottomDegree))));
// coordinate[1] = (int) (currentScreen.height - ((worldMapWidth / 2 * Math
// .log((1 + Math.sin(lat)) / (1 - Math.sin(lat)))) - mapOffsetY));
//
// LOG.debug("Output (x/y): " + coordinate[0] + "/" + coordinate[1]);
// return coordinate;
// }
//
// public int[] convertGeoToPixel(double lon, double lat) {
// LOG.debug("Input (lon/lat): " + lon + "/" + lat);
// int coordinate[] = new int[2];
//
// double mapLonDelta = minLon - maxLon;
//
// int screenWidth = currentScreen.width;
// int screenheight = currentScreen.height;
//
// double mapLatBottomDegree = maxLat * Math.PI / 180;
//
// coordinate[0] = (int) ((lon - minLon) * (screenWidth / mapLonDelta));
//
// lat = lat * Math.PI / 180;
// double worldMapWidth = ((screenWidth / mapLonDelta) * 360)
// / (2 * Math.PI);
// double mapOffsetY = (worldMapWidth / 2 * Math.log((1 + Math
// .sin(mapLatBottomDegree)) / (1 - Math.sin(mapLatBottomDegree))));
// coordinate[1] = (int) (screenheight - ((worldMapWidth / 2 * Math
// .log((1 + Math.sin(lat)) / (1 - Math.sin(lat)))) - mapOffsetY));
//
// return coordinate;
// }
//
// public int[] toMercator(double lon, double lat){
// int coordinate[] = new int[2];
//
// coordinate[0] = (int) (lon - minLon);
//
// coordinate[1] = (int) ((0.5 *
// Math.log(((1+Math.sin(lat))/(1-Math.sin(lat))))));
//
// return coordinate;
// }
//
// public Coordinate getMin() {
// return min;
// }
