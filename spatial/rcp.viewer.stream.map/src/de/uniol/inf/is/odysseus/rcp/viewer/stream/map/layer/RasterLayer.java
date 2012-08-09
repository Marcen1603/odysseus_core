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

import java.util.logging.Logger;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.AsyncImage;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.Stats;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.TileServer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.ProjectionUtil;

public class RasterLayer extends AbstractLayer {

	private static final Logger LOG = Logger
			.getLogger(RasterLayer.class.getName());

	private TileServer tileServer;

	private Stats stats = new Stats();

	private ScreenManager manager = null;
	private ScreenTransformation transformation = null;
	private Canvas canvas = null;
	private Display display = null;

	public RasterLayer(ScreenManager manager, int style) {
		this.manager = manager;
		this.transformation = manager.getTransformation();
		this.canvas = manager.getCanvas();
		this.display = canvas.getDisplay();
		
		tileServer = new TileServer("http://tile.openstreetmap.org/", 18, 0, manager);
		this.name = "Raster Layer( " + tileServer.getURL() + " )";

		LOG.info("Add new RasterLayer: " + getName());
	}

	@Override
	public void draw(GC gc) {
		getStats().reset();
		long t0 = System.currentTimeMillis();
		Point size = canvas.getSize();
		int width = size.x, height = size.y;
		int x0 = (int) Math.floor(((double) transformation.getMapPosition().x)
				/ ProjectionUtil.TILE_SIZE);
		int y0 = (int) Math.floor(((double) transformation.getMapPosition().y)
				/ ProjectionUtil.TILE_SIZE);
		int x1 = (int) Math.ceil(((double) transformation.getMapPosition().x + width)
				/ ProjectionUtil.TILE_SIZE);
		int y1 = (int) Math.ceil(((double) transformation.getMapPosition().y + height)
				/ ProjectionUtil.TILE_SIZE);

		int dy = y0 * ProjectionUtil.TILE_SIZE - transformation.getMapPosition().y;
		for (int y = y0; y < y1; ++y) {
			int dx = x0 * ProjectionUtil.TILE_SIZE - transformation.getMapPosition().x;
			for (int x = x0; x < x1; ++x) {
				paintTile(gc, dx, dy, x, y);
				dx += ProjectionUtil.TILE_SIZE;
				++getStats().tileCount;
			}
			dy += ProjectionUtil.TILE_SIZE;
		}

		long t1 = System.currentTimeMillis();
		stats.dt = t1 - t0;

	}

	private void paintTile(GC gc, int dx, int dy, int x, int y) {
		boolean DRAW_IMAGES = true;
//		boolean DEBUG = false;
//		boolean DRAW_OUT_OF_BOUNDS = !false;
//
//		boolean imageDrawn = false;
		
		int xTileCount = 1 << transformation.getZoom();
		int yTileCount = 1 << transformation.getZoom();
		boolean tileInBounds = x >= 0 && x < xTileCount && y >= 0
				&& y < yTileCount;
		boolean drawImage = DRAW_IMAGES && tileInBounds;
		if (drawImage) {
			//TileCache cache = getCache();
			TileServer tileServer = getTileServer();
			AsyncImage image = tileServer.getCache().get(tileServer, x, y, transformation.getZoom());
			if (image == null) {
				image = new AsyncImage(manager, tileServer, x, y, transformation.getZoom());
				tileServer.getCache().put(tileServer, x, y, transformation.getZoom(), image);
			}
			if (image.getImage(display) != null) {
				gc.drawImage(image.getImage(display), dx, dy);
				//imageDrawn = true;
			}
		}
//		if (DEBUG && (!imageDrawn && (tileInBounds || DRAW_OUT_OF_BOUNDS))) {
//			gc.setBackground(display
//					.getSystemColor(tileInBounds ? SWT.COLOR_GREEN
//							: SWT.COLOR_RED));
//			gc.fillRectangle(dx + 4, dy + 4, ProjectionUtil.TILE_SIZE - 8,
//					ProjectionUtil.TILE_SIZE - 8);
//			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
//			String s = "T " + x + ", " + y + (!tileInBounds ? " #" : "");
//			gc.drawString(s, dx + 4 + 8, dy + 4 + 12);
//		} else if (!DEBUG && !imageDrawn && tileInBounds) {
//			gc.setBackground(waitBackground);
//			gc.fillRectangle(dx, dy, ProjectionUtil.TILE_SIZE,
//					ProjectionUtil.TILE_SIZE);
//			gc.setForeground(waitForeground);
//			for (int yl = 0; yl < ProjectionUtil.TILE_SIZE; yl += 32) {
//				gc.drawLine(dx, dy + yl, dx + ProjectionUtil.TILE_SIZE, dy + yl);
//			}
//			for (int xl = 0; xl < ProjectionUtil.TILE_SIZE; xl += 32) {
//				gc.drawLine(dx + xl, dy, dx + xl, dy + ProjectionUtil.TILE_SIZE);
//			}
//		}
	}


	public TileServer getTileServer() {
		return tileServer;
	}

	public void setTileServer(TileServer tileServer) {
		this.tileServer = tileServer;
		canvas.redraw();
	}

	public void setTileServer(String tileServerURL) {
		this.name = "Raster Layer( " + tileServerURL + " )";
		setTileServer(new TileServer(tileServerURL, 17, 0, manager));
		canvas.redraw();
	}
	
	
	public Stats getStats() {
		return stats;
	}



}

// public static void main (String [] args) throws Exception {
// Display display = new Display ();
// Shell shell = new Shell(display);
// shell.setText("Map Widget - SWT Native Map Browsing, Map data from openstreetmap.org");
// shell.setSize(600, 710);
// shell.setLocation(10, 10);
// shell.setLayout (new FillLayout());
//
//
// new MapWidget(shell, SWT.NONE);
// shell.open ();
// while (!shell.isDisposed ()) {
// if (!display.readAndDispatch ()) display.sleep ();
// }
// display.dispose ();
//
//
// }

// public class MapLayer extends AbstractLayer {
//
// private static final Logger LOG = LoggerFactory.getLogger(MapLayer.class);
//
// protected ScreenTransformation transformation = null;
// protected Style style = null;
// protected Image image = null;
//
// WMService wms = null;

// private WMService wmservice = null;
// private com.vividsolutions.wms.MapLayer map = new
// com.vividsolutions.wms.MapLayer("Map", "bla", srsList, subLayers, bbox)
// private MapRequest mapRequest = null;

// private String mapType = "bing";
// private String mapType = "osm";
// private String mapFormat = "image/png";

// public MapLayer(ScreenTransformation transformation, Style style) {
// this.name = "Map";
// this.transformation = transformation;
// this.style = style;
// //this.boundingBox = new BoundingBox("EPSG:3857", -180.0, 85.0, 180.0,
// -85.0);
//
// this.wms = new WMService();
//
// LOG.debug("Create new MapLayer: " + name);
// }
//
//
// @Override
// public void draw(GC gc) {
//
// if (
// //transformation.isUpdate() &&
// image == null) {
// URL imageUrl;
// try {
// imageUrl = new URL(wms.toWMSURL(0, 0, 0, 1000));
// LOG.debug("Map URL: " + imageUrl.toString());
// InputStream in = imageUrl.openStream();
// image = new Image(gc.getDevice(), in);
// in.close();
// //image = updateImage(gc, 2048, this.boundingBox);
// } catch (MalformedURLException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
//
// if (image != null){
// gc.drawImage(image, (int)transformation.getMin().x,
// (int)transformation.getMin().y);
// }
// }
// @Override
// public void draw(GC gc) {
//
// if (transformation.hasUpdate()) {
//
// transformation.getMaxLat();
//
// // double[] min = transformation.screenToEpsg4326(
// // transformation.getCurrentScreen().x,
// // transformation.getCurrentScreen().y);
// // double[] max = transformation.screenToEpsg4326(
// // transformation.getCurrentScreen().width,
// // transformation.getCurrentScreen().height);
// //
// // double minLon = transformation.getMinLon();
// // double maxLon = transformation.getMaxLon();
// // double minLat = transformation.getMinLat();
// // double maxLat = transformation.getMaxLat();
//
// if (first) {
// //transformation.getOriginScreen().width;
//
// //image = updateImage(gc, 1024, transformation.getOriginScreen().height,
// minLon, maxLat, maxLon, minLat);
//
// //mapbbox = new BoundingBox("EPSG:3875", -180.0, 85.051128779799996, 180.0,
// -85.051128779799996);
// // wmservice = new WMService("http://wms.latlon.org/?");
// wmservice = new WMService("http://vmap0.tiles.osgeo.org/wms/vmap0?");
//
// // wmservice = new WMService("http://129.206.228.72/cached/osm?");
// LOG.debug(wmservice.getServerUrl());
//
// // wmservice.getCapabilities().getService().getServerUrl();
// // wmservice.getServerUrl()
// //
// try {
// wmservice.initialize();
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// mapRequest = wmservice.createMapRequest();
// mapRequest.setImageSize(transformation.getCurrentScreen().width,
// transformation.getCurrentScreen().height);
//
// it = wmservice.getCapabilities().getTopLayer().getSRSList().iterator();
//
// // mapRequest.setFormat( (String)formatCombo.getSelectedItem() );
// // mapRequest.setLayers( Arrays.asList( layerList.getSelectedValues() ) );
// mapRequest.setBoundingBox(transformation.getBbox());
// try {
// LOG.debug(mapRequest.getURL().toString());
// } catch (MalformedURLException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// image = mapRequest.getImage(gc);
// first = false;
// }
//
// if(image != null){
// int originX = image.getImageData().width;
// int originY = image.getImageData().height;
// double scale = transformation.getScale();
// LOG.debug("Image: " + image.getImageData().width + ", " +
// image.getImageData().height);
// LOG.debug("Image(scaled): " + image.getImageData().width * scale + ", " +
// image.getImageData().height * scale);
// scaledImage = new
// Image(gc.getDevice(),image.getImageData().scaledTo((int)(originX * scale),
// (int)(originY * scale)));
// }
//
// transformation.update(false);
// }
//
// int x = (int) transformation.getMin().x;
// int y = (int) transformation.getMin().y;
//
// if (scaledImage != null)
// gc.drawImage(scaledImage, x, y);
// }

/*
 * http://www.openstreetmap.org/?minlon=-16.609584883169063&minlat=-
 * 28.32078788122744&maxlon=29.410602531980885&maxlat=19.249285513021775&box=yes
 * 
 * http://www.openstreetmap.org/?minlon=0.0&minlat=40.0&maxlon=10.0&maxlat=50&box
 * =yes
 * 
 * http://www.openstreetmap.org/?lon=0&lat=0
 */

// private Image updateImage(GC gc, int width, BoundingBox boundingBox) {
// try {
// String url = "http://wms.latlon.org/?" + "&format=" + mapFormat
// + "&layers=" + mapType + "&width="
// + width
// //+ "&bbox=" + minLon + "," + maxLat + "," + maxLon + ","+ minLat;
// + "&bbox="
// + boundingBox.getMinX()
// + ","
// + boundingBox.getMinY()
// + ","
// + boundingBox.getMaxX()
// + ","
// + boundingBox.getMaxY();
//
// LOG.debug("Image URL: " + url);
//
//
// URL imageUrl = new URL(url);
// InputStream in = imageUrl.openStream();
// image = new Image(gc.getDevice(), in);
// in.close();
// } catch (MalformedURLException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
//
// return image;
// }

// }
