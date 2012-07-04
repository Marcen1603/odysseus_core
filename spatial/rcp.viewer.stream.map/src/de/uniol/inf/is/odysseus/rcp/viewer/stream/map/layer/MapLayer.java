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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class MapLayer implements Layer {

	private static final Logger LOG = LoggerFactory.getLogger(MapLayer.class);

	protected ScreenTransformation transformation = null;
	protected Style style = null;
	protected Image image = null;
	protected String name = null;
	
	private String mapType = "bing";
	//private String mapType = "osm";
	private String mapFormat = "image/png";

	private double currentMin_X;
	private double currentMin_Y;
	private double currentMax_X;
	private double currentMax_Y;
	
	public MapLayer(ScreenTransformation transformation, Style style) {
		this.name = "Map";
		this.transformation = transformation;
		this.style = style;
		currentMin_X 	= 	-180.0;
		currentMin_Y 	= 	-85.0;
		currentMax_X 	= 	180.0;
		currentMax_Y 	= 	85.0;
		LOG.debug("Create new ImageLayer: " + name);
	}

	@Override
	public void draw(GC gc) {
		if (transformation.hasUpdate()) {
			
			currentMin_X 	= 	transformation.getLat(transformation.getCurrentScreen().x);
			//transformation.computeRelativeX(transformation.getCurrentScreen().x, currentMax_X);  
			
			currentMax_X 	=   transformation.getLat(transformation.getCurrentScreen().width);	
			//transformation.computeRelativeX(transformation.getCurrentScreen().width - transformation.getCurrentScreen().x, currentMax_X);     			
			
			currentMin_Y 	=   transformation.getLon((int)transformation.getRelativeHeight(transformation.getCurrentScreen().x));
			//currentMin_Y 	=   transformation.getLon(transformation.getCurrentScreen().y);	
			//currentMin_Y 	= 	transformation.computeRelativeY(transformation.getCurrentScreen().y, currentMax_Y);
			
			//currentMax_Y 	=   transformation.getLon((int)transformation.getRelativeHeight(transformation.getCurrentScreen().height));
			currentMax_Y 	=   transformation.getLon(transformation.getCurrentScreen().height);	
			//currentMax_Y 	= 	transformation.computeRelativeY(transformation.getCurrentScreen().height - transformation.getCurrentScreen().y, currentMax_Y); 
			
			transformation.setMaxLat(currentMax_X);
			transformation.setMaxLon(currentMax_Y);
			
			LOG.debug("Map: " + " x="+ currentMin_X + "," + currentMin_Y + " y=" + currentMax_X + "," + currentMax_Y);
			
			//Only for testing. 
			if(		(currentMin_Y >= -85.0 	&& currentMin_Y <= 85.0)  &&
					(currentMax_Y >= -85.0 	&& currentMax_Y <= 85.0)  &&
					(currentMin_X >= -180.0 && currentMin_X <= 180.0) &&
					(currentMax_X >= -180.0 && currentMax_X <= 180.0)
			){
				image = updateImage(gc, transformation.getOriginScreen().width , currentMin_X, currentMin_Y, currentMax_X, currentMax_Y);				
			}
			else{
				throw new RuntimeException("Illegal Coordinates");
			}
			transformation.update(false);
		}
		gc.drawImage(image, transformation.getOriginScreen().x, transformation.getOriginScreen().y);
	}

	@Override
	public String getName() {
		return name;
	}

	private Image updateImage(GC gc, int width, double min_x,double min_y, double max_x, double max_y) {
		LOG.debug("Update Image: " + width + " " + "BBox[ " + min_x + "," + min_y + "," + max_x + "," + max_y +"]");
		Image image = null;
		try {
			String url = "http://wms.latlon.org/?"
					+ "&format=" 
					+ mapFormat
					+ "&layers="
					+ mapType
					+ "&width="
					+ width
					+ "&bbox="
					+ min_x
					+ ","
					+ min_y
					+ ","
					+ max_x	
					+ ","
					+ max_y;
			LOG.debug("Image URL: " + url);
			
			URL imageUrl = new URL(url);
			InputStream in = imageUrl.openStream();
			image = new Image(gc.getDevice(), in);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
