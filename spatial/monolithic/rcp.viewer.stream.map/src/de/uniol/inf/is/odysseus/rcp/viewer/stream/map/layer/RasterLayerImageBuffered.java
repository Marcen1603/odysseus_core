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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.AsyncImage;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.Stats;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.TileServer;

public class RasterLayerImageBuffered extends AbstractLayer<RasterLayerConfiguration> implements PropertyChangeListener {

	private static final long serialVersionUID = 7482299307627103992L;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(RasterLayerImageBuffered.class.getName());

	private TileServer tileServer;

	private Stats stats = new Stats();
	private ScreenManager manager = null;
	private ScreenTransformation transformation = null;
	private Canvas canvas = null;
	private Display display = null;

	public RasterLayerImageBuffered(RasterLayerConfiguration configuration) {
		super(configuration);
	}

	private Color waitForeground;

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		this.manager = screenManager;
		this.transformation = manager.getTransformation();
		this.canvas = manager.getCanvas();
		this.display = canvas.getDisplay();
		waitForeground = new Color(display, 200, 200, 200);
		this.name = configuration.getName();
//		tileServer = new TileServer(this, manager);
		this.active = true;
		manager.addPropertyChangeListener(this);
		synchronized (buffer) {
			this.buffer[0] = new Image(Display.getCurrent(), 100, 100);
		}
	}

	private Image[] buffer = new Image[1];
	private GC imgGC = null;
	private Point offset = new Point(0, 0);
	private Point size;
	private double buffersize = 0.5;

	private void newImage() {
		Color TRANSPARENT_COLOR = Display.getCurrent().getSystemColor( SWT.COLOR_BLACK);
		Color DRAWING_COLOR = Display.getCurrent().getSystemColor( SWT.COLOR_RED);

		PaletteData paletteData = new PaletteData( new RGB[]{
		   TRANSPARENT_COLOR.getRGB(),
		   DRAWING_COLOR.getRGB()
		   });
		
		getStats().reset();
		long t0 = System.currentTimeMillis();
		Point canvasSize = canvas.getSize();
		size = canvas.getSize();
		int[] offset = manager.getOffset();
		size.x *= buffersize;
		size.y *= buffersize;
		int width = canvasSize.x + size.x + size.x;
		int height = canvasSize.y + size.y + size.y;
		paletteData = new PaletteData(8, 8, 8);
		ImageData imageData = new ImageData( width, height, 4, paletteData);
		imageData.transparentPixel = paletteData.getPixel(TRANSPARENT_COLOR.getRGB());
//		imageData.type = SWT.IMAGE_GIF;
		this.buffer[0] = new Image(Display.getCurrent(), imageData);
//		this.buffer[0] = new Image(Display.getCurrent(), width, height);
		this.tileBuffer.clear();
		imgGC = new GC(buffer[0]);
//		imgGC.fillRectangle(buffer[0].getBounds());
//		if (manager.getCanvas() != null && !manager.getCanvas().isDisposed())
//		new GC(manager.getCanvas()).copyArea(this.buffer[0], 0, 0);
		Envelope world = manager.getViewportWorldCoord((offset[0] + size.x), (offset[1] + size.y));
		List<AsyncImage> images = getTileServer().getTiles(world, manager.getSRID(),
				new Point(width, height));
		for (AsyncImage image : images) {
			paintTile(new BufferTile(image));
		}
		long t1 = System.currentTimeMillis();
		stats.dt = t1 - t0;
	}

	GC gc = null;
	@Override
	public void draw(GC gc) {
		LinkedList<BufferTile> old = tileBuffer;
		synchronized (tileBuffer) {
			tileBuffer = new LinkedList<BufferTile>();
		}
		while (!old.isEmpty()) {
			BufferTile image = null;
				image = old.poll();
			paintTile(image);
		}
			if (buffer != null && accessable) {
				gc.drawImage(buffer[0], offset.x - size.x, offset.y - size.y);
				gc.setForeground(waitForeground);
				gc.drawRectangle(offset.x - size.x, offset.y - size.y, buffer[0].getBounds().width, buffer[0].getBounds().height);
			}
		int[] uv = transformation.transformCoord(manager.getCenter(), manager.getSRID());
		System.out.println(uv[0] + " " + uv[1]);
		gc.drawLine(uv[0], uv[1], uv[0] + 20, uv[1]);
		gc.drawLine(uv[0], uv[1] - 1000, uv[0], uv[1] + 1000);
		gc.drawOval(uv[0] - 10, uv[1] - 10, 20, 20);
		// int t = manager.getEnvelope().x / (1 <<
		// (transformation.getZoom()/2));
		// int x0 = (int) Math.floor(((double)
		// transformation.getMapPosition().x)
		// / t);
		// int y0 = (int) Math.floor(((double)
		// transformation.getMapPosition().y)
		// / t);
		// int x1 = (int) Math.ceil(((double) transformation.getMapPosition().x
		// + width)
		// / t);
		// int y1 = (int) Math.ceil(((double) transformation.getMapPosition().y
		// + height)
		// / t);
		//
		// int dy = y0 * t - transformation.getMapPosition().y;
		// for (int y = y0; y < y1; ++y) {
		// int dx = x0 * t - transformation.getMapPosition().x;
		// for (int x = x0; x < x1; ++x) {
		// paintTile(gc, dx, dy, x, y);
		// dx += t;
		// ++getStats().tileCount;
		// }
		// dy += t;
		// }
		//

	}

	LinkedList<BufferTile> tileBuffer = new LinkedList<BufferTile>();
	class BufferTile{
		AsyncImage image;
		int[] minxy;
		int[] maxxy;
		int width;
		int height;
		
		public BufferTile(AsyncImage image) {
			this.image = image;
		Envelope env = image.getEnvelope();
		// getTileServer().getSRID;
		Coordinate coord = new Coordinate(env.getMinX(), env.getMinY());
		Coordinate coord1 = new Coordinate(env.getMaxX(), env.getMaxY());
		this.minxy = transformation.transformCoord(coord, getTileServer().getSRID());
		this.maxxy = transformation.transformCoord(coord1, getTileServer().getSRID());
		this.width = Math.max(minxy[0], maxxy[0]) - Math.min(minxy[0], maxxy[0]);
		this.height = Math.max(minxy[1], maxxy[1]) - Math.min(minxy[1], maxxy[1]);
		}
	}

	boolean accessable = true;
	public void updateTile(AsyncImage image) {
		//paintTile(image);
		synchronized (tileBuffer) {
			tileBuffer.add(new BufferTile(image));
		}
		manager.redraw();
	}

	private void paintTile(BufferTile image) {
		try {
			// if (drawImage) {
			// //TileCache cache = getCache();
			// TileServer tileServer = getTileServer();
			// AsyncImage image = tileServer.getCache().get(tileServer, x, y,
			// transformation.getZoom()/2);
			// if (image == null) {
			// image = new AsyncImage(manager, tileServer, x, y,
			// transformation.getZoom()/2);
			// tileServer.getCache().put(tileServer, x, y,
			// transformation.getZoom()/2, image);
			// }
			if (image.image.getImage(Display.getCurrent()) != null) {
				accessable = false;
				
//					.image.getImage(Display.getCurrent()).getImageData();
//					buffer[0].getImageData().palette.
					imgGC.drawImage(image.image.getImage(Display.getCurrent()), 0, 0, tileServer.getTileWitdh(), tileServer.getTileHeight(), size.x + image.minxy[0], size.y
							+ image.maxxy[1], image.width, image.height);
				imgGC.setForeground(waitForeground);
				imgGC.drawRectangle(size.x + image.minxy[0], size.y + image.maxxy[1], image.width, image.height);
				accessable = true;
			}else
				synchronized (tileBuffer) {
					tileBuffer.add(image);
				}
				
		} catch (Exception e) {
			accessable = true;
			synchronized (tileBuffer) {
				tileBuffer.add(image);
			}
		}
	}

	public TileServer getTileServer() {
		return tileServer;
	}

	public void setTileServer(TileServer tileServer) {
		this.tileServer = tileServer;
		manager.redraw();
	}

	public void setTileServer(String tileServerURL) {
		this.name = "Raster Layer( " + tileServerURL + " )";
//		setTileServer(new TileServer(this, manager));
	}

	public Stats getStats() {
		return stats;
	}

	@Override
	public String[] getSupprtedDatatypes() {
		return null;
	}
	
	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
	}
	
	@Override
	public int getTupleCount() {
		return 0;
	}

	@Override
	public RasterLayerConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(LayerConfiguration configuration) {
		if (configuration instanceof RasterLayerConfiguration) {
			setConfiguration((RasterLayerConfiguration) configuration);
		}
	}

	public void setConfiguration(RasterLayerConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("centerUV")) {
			Coordinate oldValue = (Coordinate) evt.getOldValue();
			Coordinate newValue = (Coordinate) evt.getNewValue();
			double dx = oldValue.x - newValue.x;
			double dy = oldValue.y - newValue.y;
			offset.x -= dx;
			offset.y -= dy;
//			if (Math.abs(dx) + Math.abs(dy) > 3 ) buffersize = 0.5;
//			else buffersize = 0.2;
			System.out.println("Buffersize :" +buffersize + " offset " + dx + " " + dy);
			@SuppressWarnings("unused")
			int[] off = manager.getOffset();
			if (offset.x > size.x || offset.x < -size.x) {
				offset = new Point(0, 0);
				Image old = buffer[0];
				if (imgGC != null)
					imgGC.dispose();
				newImage();
				old.dispose();
			}
			else if (offset.y > size.y || offset.y < -size.y) {
				offset = new Point(0, 0);
				Image old = buffer[0];
				if (imgGC != null)
					imgGC.dispose();
				newImage();
				old.dispose();
			}
		} else {
			offset = new Point(0, 0);
			Image old = buffer[0];
			if (imgGC != null)
				imgGC.dispose();
			newImage();
			old.dispose();
		}
		manager.redraw();

	}

	@Override
	protected void finalize() throws Throwable {
		this.buffer[0].dispose();
		super.finalize();
	}

	@Override
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return this.tileServer.getEnvelope();
	}
}
