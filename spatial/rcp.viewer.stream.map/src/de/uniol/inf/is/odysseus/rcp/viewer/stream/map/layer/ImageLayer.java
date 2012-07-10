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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ImageLayer implements Layer {

	private static final Logger LOG = LoggerFactory.getLogger(ImageLayer.class);

	protected ScreenTransformation transformation = null;
	protected Style style = null;
	protected Image image = null;
	protected String name = null;

	private Point point = null;

	private boolean redraw = true;

	public ImageLayer(ScreenTransformation transformation, Style style, String name) {
		LOG.debug("Create new ImageLayer: " + name);
		this.transformation = transformation;
		this.style = style;
		this.name = name;
	}

	public void drawImage(Image image, Point point, GC gc) {
		this.point = point;
		int[] uv = transformation.transformCoord(point.getCoordinate(),point.getSRID());
		gc.drawImage(image, uv[0], uv[1]);
	}

	@Override
	public void draw(GC gc) {
		if (transformation.hasUpdate()) {
			int height = transformation.getCurrentScreen().height;
			int width = transformation.getCurrentScreen().width;
			//int scale = transformation.getScale();

			image = updateImage(gc, width ,-170.0, -70.0, 170.0, 70.0, 0);
			transformation.update(false);
		}
		gc.drawImage(image, 0, 0);
	}

	@Override
	public String getName() {
		return name;
	}


	private Image updateImage(GC gc, int width, double xx,double yy, double x, double y,
			double scale) {
		LOG.debug("Update Image: " + width + " " + " " + x + " " + y + " " + scale + " ");
		Image image = null;
// http://wms.latlon.org/?format=image/png&layers=bing&width=1000&bbox=-180.9999,-70.9999,176.9999,70.9999
		try {
			String url = "http://wms.latlon.org/?format=image/png&layers=bing&width="
					+ width
//					+ "&height="
//					+ height
					+ "&bbox="
					+ xx
					+ ","
					+ yy
					+ ","
					+ x	
					+ ","
					+ y;
			LOG.debug("Image URL: " + url);

			// URL imageUrl = new
			// URL("http://wms.latlon.org/?layers=bing&request=GetTile&width="+
			// width
			// +"&height="+height+"&x="+x+"&y="+y+"&z="+Math.round(scale)+"");
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
