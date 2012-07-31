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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.wms.WMService;


/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class MapLayer extends AbstractLayer {

	private static final Logger LOG = LoggerFactory.getLogger(MapLayer.class);

	protected ScreenTransformation transformation = null;
	protected Style style = null;
	protected Image image = null;
	
	WMService wms = null;
	
	//private WMService wmservice = null;
	//private com.vividsolutions.wms.MapLayer map = new com.vividsolutions.wms.MapLayer("Map", "bla", srsList, subLayers, bbox)
	//private MapRequest mapRequest = null;
	
	// private String mapType = "bing";
//	private String mapType = "osm";
//	private String mapFormat = "image/png";

	public MapLayer(ScreenTransformation transformation, Style style) {
		this.name = "Map";
		this.transformation = transformation;
		this.style = style;
		//this.boundingBox = new BoundingBox("EPSG:3857", -180.0, 85.0, 180.0, -85.0);
		
		this.wms = new WMService();
		
		LOG.debug("Create new MapLayer: " + name);
	}
	
	
	@Override
	public void draw(GC gc) {
		
//		if (
//				//transformation.isUpdate() && 
//				image == null) {
//			URL imageUrl;
//			try {
//				imageUrl = new URL(wms.toWMSURL(0, 0, 0, 1000));
//				LOG.debug("Map URL: " + imageUrl.toString());
//				InputStream in = imageUrl.openStream();
//				image = new Image(gc.getDevice(), in);
//				in.close();
//				//image = updateImage(gc, 2048, this.boundingBox);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if (image != null){
//			gc.drawImage(image, (int)transformation.getMin().x, (int)transformation.getMin().y);
//		}
	}
//	@Override
//	public void draw(GC gc) {
//
//		if (transformation.hasUpdate()) {
//			
//			transformation.getMaxLat();
//			
////			double[] min = transformation.screenToEpsg4326(
////					transformation.getCurrentScreen().x,
////					transformation.getCurrentScreen().y);
////			double[] max = transformation.screenToEpsg4326(
////					transformation.getCurrentScreen().width,
////					transformation.getCurrentScreen().height);
////			
////			double minLon = transformation.getMinLon();
////			double maxLon = transformation.getMaxLon();
////			double minLat = transformation.getMinLat();
////			double maxLat = transformation.getMaxLat();
//
//			if (first) {
//				//transformation.getOriginScreen().width;
//				
//				//image = updateImage(gc, 1024, transformation.getOriginScreen().height, minLon, maxLat, maxLon, minLat);
//
//				//mapbbox = new BoundingBox("EPSG:3875", -180.0, 85.051128779799996, 180.0,  -85.051128779799996);
////				wmservice = new WMService("http://wms.latlon.org/?");
//				wmservice = new WMService("http://vmap0.tiles.osgeo.org/wms/vmap0?");
//				
////				wmservice = new WMService("http://129.206.228.72/cached/osm?");
//				LOG.debug(wmservice.getServerUrl());
//				
//// 				wmservice.getCapabilities().getService().getServerUrl();
////				wmservice.getServerUrl()
////				
//				try {
//					wmservice.initialize();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				mapRequest = wmservice.createMapRequest();
//		        mapRequest.setImageSize(transformation.getCurrentScreen().width, transformation.getCurrentScreen().height);
//		        
//		        it = wmservice.getCapabilities().getTopLayer().getSRSList().iterator();
//		        
//		       // mapRequest.setFormat( (String)formatCombo.getSelectedItem() );
//		       // mapRequest.setLayers( Arrays.asList( layerList.getSelectedValues() ) );
//		        mapRequest.setBoundingBox(transformation.getBbox());
//		        try {
//					LOG.debug(mapRequest.getURL().toString());
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				image = mapRequest.getImage(gc);
//				first = false;
//			}
//			
//			if(image != null){
//						int originX = image.getImageData().width;
//						int originY = image.getImageData().height;
//						double scale = transformation.getScale();
//						LOG.debug("Image: " + image.getImageData().width + ", " + image.getImageData().height);
//						LOG.debug("Image(scaled): " + image.getImageData().width * scale + ", " + image.getImageData().height * scale);	
//						scaledImage = new Image(gc.getDevice(),image.getImageData().scaledTo((int)(originX * scale), (int)(originY * scale)));
//			}
//
//			transformation.update(false);
//		}
//
//		int x = (int) transformation.getMin().x;
//		int y = (int) transformation.getMin().y;
//		
//		if (scaledImage != null)
//			gc.drawImage(scaledImage, x, y);
//	}


	/*
	 * http://www.openstreetmap.org/?minlon=-16.609584883169063&minlat=-28.32078788122744&maxlon=29.410602531980885&maxlat=19.249285513021775&box=yes
	 * 
	 * http://www.openstreetmap.org/?minlon=0.0&minlat=40.0&maxlon=10.0&maxlat=50&box=yes
	 * 
	 * http://www.openstreetmap.org/?lon=0&lat=0
	 */

//	private Image updateImage(GC gc, int width, BoundingBox boundingBox)  {
//		try {
//			String url = "http://wms.latlon.org/?" + "&format=" + mapFormat
//					+ "&layers=" + mapType + "&width="
//					+ width
//					//+ "&bbox=" + minLon + "," + maxLat + "," + maxLon + ","+ minLat;
//					+ "&bbox=" 
//					+ boundingBox.getMinX() 
//					+ ","
//					+ boundingBox.getMinY() 
//					+ ","
//					+ boundingBox.getMaxX() 
//					+ ","
//					+ boundingBox.getMaxY();
//			
//			LOG.debug("Image URL: " + url);
//			
//			
//			URL imageUrl = new URL(url);
//			InputStream in = imageUrl.openStream();
//			image = new Image(gc.getDevice(), in);
//			in.close();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return image;
//	}
	
}
