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
	
	//private String mapType = "bing";
	private String mapType = "osm";
	private String mapFormat = "image/png";
	
	public MapLayer(ScreenTransformation transformation, Style style) {
		this.name = "Map";
		this.transformation = transformation;
		this.style = style;
		LOG.debug("Create new ImageLayer: " + name);
	}

	@Override
	public void draw(GC gc) {
		if (transformation.hasUpdate()) {
			
			double[] min = transformation.SC2WGS(transformation.getCurrentScreen().x, transformation.getCurrentScreen().y);
			double[] max = transformation.SC2WGS(transformation.getCurrentScreen().width, transformation.getCurrentScreen().height);	

			transformation.setMinLat(min[0]);
			transformation.setMinLon(min[1]);
			transformation.setMaxLat(max[0]);
			transformation.setMaxLon(max[1]);

			
			LOG.debug("Calculated Map: " + " x="+ min[0] + "," + min[1] + " y=" + max[0] + "," + max[1]);
			
			//Only for testing. 
			if(		(min[1] >= -85.0 	&& min[1] <= 85.0)  &&
					(max[1] >= -85.0 	&& max[1] <= 85.0)  &&
					(min[0] >= -180.0 && min[0] <= 180.0) &&
					(max[0] >= -180.0 && max[0] <= 181.0)
			){
				max[0] = 180;
				image = updateImage(gc, transformation.getOriginScreen().width, transformation.getOriginScreen().height, min[0], min[1], max[0], max[1]);				
				//image = updateImage(gc, transformation.getOriginScreen().width,transformation.getOriginScreen().height , currentMin_X, currentMin_Y, currentMax_X, currentMax_Y);				

			}
//			else{
//				throw new RuntimeException("Illegal Coordinates");
//			}
			transformation.update(false);
		}
		gc.drawImage(image, 0, 0);
	}

	@Override
	public String getName() {
		return name;
	}

	
	/*
	 * http://www.openstreetmap.org/?minlon=-16.609584883169063&minlat=-28.32078788122744&maxlon=29.410602531980885&maxlat=19.249285513021775&box=yes
	 * 
	 * http://www.openstreetmap.org/?lon=0&lat=0
	 * 
	 */
	
	
	private Image updateImage(GC gc, int width,int height, double minLat,double maxLon , double maxLat, double minLon) {
		LOG.debug("Update Image: " + width + " " + "BBox[ " + minLat + "," + maxLon + "," + maxLat + "," + minLon +"]");
		Image image = null;
		try {
			String url = "http://wms.latlon.org/?"
					+ "&format=" 
					+ mapFormat
					+ "&layers="
					+ mapType
					+ "&width="
					+ width
					+ "&height="
					+ height
					+ "&bbox="
					+ minLat
					+ ","
					+ minLon
					+ ","
					+ maxLat
					+ ","
					+ maxLon
					+ "&mlat="
					+ 0.0
					+ "&mlon="
					+ 0.0
					;
					
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
