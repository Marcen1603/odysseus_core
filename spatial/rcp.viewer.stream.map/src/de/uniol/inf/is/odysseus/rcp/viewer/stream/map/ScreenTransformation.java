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
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ScreenTransformation {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScreenTransformation.class);
	
	private Coordinate min = new Coordinate(0,0);
	private Integer scale = 1;
	private boolean update = true;
	
	private Rectangle currentScreen = new Rectangle(0, 0, 0, 0);
	private Rectangle originScreen = new Rectangle(0, 0, 0, 0);
	
	
	private double minLat = -180.0;
	private double maxLat =  180.0;
	
	private double minLon =  85.0;
	private double maxLon = -85.0;
	
	private Point center = new Point(0, 0);
	

	public void updateOrigin(Rectangle rectangle) {
		LOG.debug("Update Origin");
		originScreen = rectangle;
		currentScreen = rectangle;
		
		//Compute the new conversion factors
		//computeConversionX();
		//computeConversionY();
		
		update = true;
	}
	
	public void updateCurrent(Rectangle rectangle) {
		LOG.debug("Update Current");
		currentScreen = rectangle;

		//LOG.debug(String.format("Current Screen %d %d %d %d", currentScreen.x, currentScreen.y, currentScreen.width, currentScreen.height));
		
		//Compute the new conversion factors
		//computeConversionX();
		//computeConversionY();
		
		if (this.min != null) {
			min.x = ((rectangle.x + rectangle.width / 2 - this.center.x) * scale)
					+ min.x;
			min.y = ((rectangle.y + rectangle.height / 2 - this.center.y) * scale)
					+ min.y;
			try {
				scale = scale / (Math.min(currentScreen.width / rectangle.width, currentScreen.height / rectangle.height));
			if(scale == 0)
				scale = 1;
			} catch (ArithmeticException e) {
				LOG.debug(e.getMessage());
			}
		}
		update = true;
	}

	public int[] transformCoord(Coordinate coord,int srid) {
		
		// @FIXME 
		if(coord.z != Integer.MAX_VALUE){
			coord.z = Integer.MAX_VALUE;
			//double[] epsgCoord	= epsg4326To(3385, coord.x, coord.y);
			int[] scCoord = epsg4326tToScreen(coord.x,coord.y);
			coord.x = new Integer(scCoord[0]);
			coord.y = new Integer(scCoord[1]);
		}
		
		if (this.min == null) {
			this.min = (Coordinate) coord.clone();
			int[] uv = { new Double(center.x).intValue(),
					new Double(center.y).intValue() };
			return uv;
		} else {
			if (scale == null) {
				scale = (int)(Math.abs(coord.x - min.x) + Math.abs(coord.y - min.y) / this.currentScreen.width + this.currentScreen.height) * 2;
			}
			int[] uv = {
					new Double(center.x + (coord.x - min.x) / scale).intValue(),
					new Double(center.y + (coord.y - min.y) / scale).intValue() };
			return uv;
		}
	}

	public void zoomin() {
		scale /= 2;
		currentScreen.y -= scale;
		currentScreen.height -= scale;
		currentScreen.x -= scale;
		currentScreen.width -= scale;
		update = true;
		LOG.debug("Zoom In: " + scale);
	}

	public void zoomout() {
		scale += scale;
		currentScreen.y += scale;
		currentScreen.height += scale;
		currentScreen.x += scale;
		currentScreen.width += scale;
		update = true;
		LOG.debug("Zoom Out: " + scale);
	}

	public void panNorth() {
		currentScreen.y += scale;
		currentScreen.height += scale;
		min.y += scale;
		update = true;
		LOG.debug("Pan North: " + min.y);
	}

	public void panSouth() {
		currentScreen.y -= scale;
		currentScreen.height -= scale;
		min.y -= scale;
		update = true;
		LOG.debug("Pan South: " + min.y);
	}

	public void panWest() {
		currentScreen.x -= scale;
		currentScreen.width -= scale;
		min.x -= scale;
		update = true;
		LOG.debug("Pan West: " + min.x);
	}

	public void panEast() {
		currentScreen.x += scale;
		currentScreen.width += scale;
		min.x += scale;
		update = true;
		LOG.debug("Pan East: " + min.x);
	}
	
	public Integer getScale() {
		return scale;
	}

	public Rectangle getCurrentScreen() {
		return currentScreen;
	}
	
	public Rectangle getOriginScreen() {
		return originScreen;
	}
	

	public void update(boolean update) {
		this.update = update;
	}

	public boolean hasUpdate() {
		return this.update;
	}
	
	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMinLon() {
		return minLon;
	}

	public void setMinLon(double minLon) {
		this.minLon = minLon;
	}

	public double getMaxLon() {
		return maxLon;
	}

	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
	}

	public double[] epsg4326To(int epsg, double x, double y){
		//based on http://lists.osgeo.org/pipermail/proj4j/2010-May/000041.html
		
		double[] coordinates = new double[2];
		
		CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
		CRSFactory csFactory = new CRSFactory();
		
		/*
		 * Create {@link CoordinateReferenceSystem} & CoordinateTransformation.
	     * Normally this would be carried out once and reused for all transformations
	     */ 
		
		CoordinateReferenceSystem crs1 = csFactory.createFromName("EPSG:" + 4326);
		CoordinateReferenceSystem crs2 = csFactory.createFromName("EPSG:" + epsg);
	
//		LOG.debug("Transformation");
//		LOG.debug("(source) Datum: " + crs1.getDatum());
//		LOG.debug("(source) Name: " + crs1.getName());
//		LOG.debug("(source) Parameter: " + crs1.getParameterString());
//		LOG.debug("(source) Projection: " + crs1.getProjection());
//		
//		LOG.debug("(target) Datum: " + crs2.getDatum());
//		LOG.debug("(target) Name: " + crs2.getName());
//		LOG.debug("(target) Parameter: " + crs2.getParameterString());
//		LOG.debug("(target) Projection: " + crs2.getProjection());
		
	    CoordinateTransform trans = ctFactory.createTransform(crs1, crs2);
	
	    
	    
	    /*
	     * Create input and output points.
	     * These can be constructed once per thread and reused.
	     */ 
	    
	    ProjCoordinate source = new ProjCoordinate();
	    ProjCoordinate target = new ProjCoordinate();
	    	source.x = x;
	    	source.y = y;
	    	//LOG.debug("---------------------------------------------------");
	    	//LOG.debug("(source) Coordinate: " + source.x + ", " + source.y);	
	    	
		    /*
		     * Transform point
		     */
		    trans.transform(source, target);
		    coordinates[0] = target.x;
		    coordinates[1] = target.y;
		   // LOG.debug("(target) Coordinate: " + target.x + ", " + target.y);
	    	//LOG.debug("---------------------------------------------------");

		return coordinates;
	}
	
	
	public double[] screenToEpsg4326(int x,int y){
		double coordinate[] = new double[2];
		// Quadrant 1: -/+
		if((x < (currentScreen.width/2)) && (y < (currentScreen.height/2))){
			// X-Negative
			coordinate[0] = minLat - ((x * minLat)/(currentScreen.width/2));
			// Y-Positive
			coordinate[1] = minLon - ((y * minLon)/(currentScreen.height/2));
			return coordinate;
		}
		
		// Quadrant 3: +/-
		if((x >= (currentScreen.width/2)) && (y >= (currentScreen.height/2))){
			// X-Positive	
			coordinate[0] = (-1)*(maxLat- ((x * maxLat)/(currentScreen.width/2)));
			// Y-Negative
			coordinate[1] = (-1)*(maxLon - ((y * maxLon)/(currentScreen.height/2)));
			return coordinate;
		}
		
		// Quadrant 2: +/+
		if((x >= (currentScreen.width/2)) && (y < (currentScreen.height/2))){
			// X-Positive	
			coordinate[0] = (-1)*(maxLat - ((x * maxLat)/(currentScreen.width/2)));
			// Y-Positive
			coordinate[1] = (minLon - ((y * minLon)/(currentScreen.height/2)));
			return coordinate;
		}
		
		// Quadrant 4: -/-
		if((x < (currentScreen.width/2)) && (y >= (currentScreen.height/2))){
			// X-Negative
			coordinate[0] =	minLat - ((x * minLat) / (currentScreen.width/2));
			// Y-Negative
			coordinate[1] = (-1)*(maxLon - ((y * maxLon)/(currentScreen.height/2)));
			return coordinate;
		}
		return coordinate;
	}
	
	
	public int[] epsg4326tToScreen(double x,double y){
		int coordinate[] = new int[2];
		int width = (currentScreen.width/2);
		int height = (currentScreen.height/2);
		
//		if(!((x > -181) && (x < 181) && (y > -86) && (y < 86)))
//			throw new RuntimeException("No WGS84 Coordinates.");

		
		// Quadrant 2: +/+
		if((x >= 0) && (y >= 0)){
			// X-Positive
			coordinate[0] =	(int) (width + ((x * width)/maxLat));
			// Y-Negative
			coordinate[1] = (int) (height + ((y * height)/maxLon));
			return coordinate;
		}
		
		// Quadrant 4: -/-
		if((x < 0) && (y < 0)){
			// X-Negative
			coordinate[0] =	(currentScreen.width/2) - (int)((x * (currentScreen.width/2))/minLat);
			// Y-Positive
			coordinate[1] = (currentScreen.height/2) + (int)((y * (currentScreen.height/2))/maxLon);
			return coordinate;
		}
		
		// Quadrant 3: +/-
		if((x >= 0) && (y < 0)){
			// X-Negative
			coordinate[0] =	(currentScreen.width/2) + (-1)*(int)((x * (currentScreen.width/2))/minLat);
			// Y-Negative
			coordinate[1] = (currentScreen.height/2) + (int)((y * (currentScreen.height/2))/maxLon);
			return coordinate;
		}
		
		// Quadrant 1: -/+
		if((x < 0) && (y >= 0)){
			// X-Positive
			coordinate[0] = (currentScreen.width/2) + (int)((x * (currentScreen.width/2))/maxLat);
			// Y-Positive
			coordinate[1] = (currentScreen.height/2) - (int)((y * (currentScreen.height/2))/minLon);
			return coordinate;
		}
		
		return coordinate;
	}
	
}