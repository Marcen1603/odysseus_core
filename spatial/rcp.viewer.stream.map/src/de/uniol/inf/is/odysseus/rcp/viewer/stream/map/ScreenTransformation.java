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
public class ScreenTransformation {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScreenTransformation.class);
	
	private Coordinate min = new Coordinate(0,0);
	private Integer scale = 1;
	private boolean update = false;
	
	private Rectangle currentScreen = new Rectangle(0, 0, 0, 0);
	private Rectangle originScreen = new Rectangle(0, 0, 0, 0);
	
	private double conversionX = 0;
	private double conversionY = 0;
	
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
		computeConversionX();
		computeConversionY();
		
		update = true;
	}
	
	public void updateCurrent(Rectangle rectangle) {
		LOG.debug("Update Current");
		currentScreen = rectangle;

		//LOG.debug(String.format("Current Screen %d %d %d %d", currentScreen.x, currentScreen.y, currentScreen.width, currentScreen.height));
		
		//Compute the new conversion factors
		computeConversionX();
		computeConversionY();
		
		if (this.min != null) {
			min.x = ((rectangle.x + rectangle.width / 2 - this.center.x) * scale)
					+ min.x;
			min.y = ((rectangle.y + rectangle.height / 2 - this.center.y) * scale)
					+ min.y;
			try {
				//scale = scale / (Math.min(currentScreen.width / rectangle.width, currentScreen.height / rectangle.height));
			if(scale == 0)
				scale = 1;
			} catch (ArithmeticException e) {
				LOG.debug(e.getMessage());
			}
		}
		update = true;
	}

	public int[] transformCoord(Coordinate coord) {
		
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
	
	public double getRelativeHeight(int width){
		double d = (double)(originScreen.height/originScreen.width);
		return (double)(width * d); 
	}
	

	public double getLat(int screenCoordinate){
		return (conversionX * screenCoordinate) - maxLat;
	}
	
	public double getLon(int screenCoordinate){
			return (conversionY * screenCoordinate) - minLon;	
	}
	
	

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
		computeConversionX();
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
		computeConversionX();
	}

	public double getMinLon() {
		return minLon;
	}

	public void setMinLon(double minLon) {
		this.minLon = minLon;
		computeConversionY();
	}

	public double getMaxLon() {
		return maxLon;
	}

	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
		computeConversionY();
	}

	private void computeConversionX(){
		double distance = maxLat - minLat;
		LOG.debug("Current X distance:" + distance);
		conversionX = distance/currentScreen.width;
	}
	
	private void computeConversionY(){
		double distance = minLon - maxLon;
		LOG.debug("Current Y distance:" + distance);
		conversionY = distance/currentScreen.height;
	}
	
}