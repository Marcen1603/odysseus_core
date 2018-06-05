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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.VectorLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.ImageStyle;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.tile.AsyncImage;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.tile.Stats;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.tile.TileServer;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;

public class RasterLayer extends AbstractLayer<RasterLayerConfiguration> implements PropertyChangeListener {

	private static final long serialVersionUID = 7482299307627103992L;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(RasterLayer.class.getName());

	private TileServer tileServer;

	private Stats stats = new Stats();
	protected transient ScreenManager manager = null;
	protected transient ScreenTransformation transformation = null;
	private transient Canvas canvas = null;
	private transient ImageStyle style = null;

	private Point offset = new Point(0, 0);
	private Point size;
	private double buffersize = 0.1;
	private Envelope buffer = null;

	public RasterLayer(RasterLayerConfiguration configuration) {
		super(configuration);
		this.style = new ImageStyle();
	}

	@Override
	public Style getStyle() {
		return this.style;
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		this.manager = screenManager;
		this.transformation = manager.getTransformation();
		this.canvas = manager.getCanvas();
		this.name = configuration.getName();
		tileServer = new TileServer(this, manager);
		this.active = true;
		newBuffer();
		manager.addPropertyChangeListener(this);
	}

	private void newBuffer() {
		getStats().reset();
		long t0 = System.currentTimeMillis();
		Point canvasSize = canvas.getSize();
		size = canvas.getSize();
		int[] offset = manager.getOffset();
		size.x *= buffersize;
		size.y *= buffersize;
		int width = canvasSize.x + size.x + size.x;
		int height = canvasSize.y + size.y + size.y;
		buffer = manager.getViewportWorldCoord((offset[0] + size.x), (offset[1] + size.y));
		synchronized (tileBuffer) {
			this.tileBuffer.clear();
		}
		if (getTileServer().getURL() != null) {
			// Maybe no tile server is defined
			List<AsyncImage> images = getTileServer().getTiles(buffer, manager.getSRID(), new Point(width, height));
			for (AsyncImage image : images) {
				updateTile(image);
			}
		}
		long t1 = System.currentTimeMillis();
		stats.dt = t1 - t0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(GC gc) {
		LinkedList<BufferTile> old = null;
		synchronized (tileBuffer) {
			old = (LinkedList<BufferTile>) tileBuffer.clone();
		}
		while (!old.isEmpty()) {
			BufferTile image = null;
			image = old.poll();
			paintTile(image, gc);
		}
	}

	LinkedList<BufferTile> tileBuffer = new LinkedList<BufferTile>();

	public void updateTile(AsyncImage image) {
		// paintTile(image);
		synchronized (tileBuffer) {
			if (!tileBuffer.contains(image))
				tileBuffer.add(new BufferTile(image));
			++getStats().tileCount;
		}
		manager.redraw();
	}

	private void paintTile(BufferTile tile, GC gc) {
		Image image = tile.image.getImage(Display.getDefault());
		if (image != null && !image.isDisposed()) {
			tile.update();
			Rectangle bounds = image.getBounds();
			gc.drawImage(image, 0, 0, bounds.width, bounds.height, tile.minxy[0], tile.maxxy[1], tile.width,
					tile.height);
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
		setTileServer(new TileServer(this, manager));
	}

	public Stats getStats() {
		return stats;
	}

	@Override
	public String[] getSupportedDatatypes() {
		return null;
	}

	@Override
	public void setBuffer(Buffer buffer) {
	}

	@Override
	public int getTupleCount() {
		return 0;
	}

	@Override
	public RasterLayerConfiguration getConfiguration() {
		return configuration;
	}

	//@SuppressWarnings("cast")
	@Override
	public void setConfiguration(LayerConfiguration configuration) {
		if (configuration instanceof VectorLayerConfiguration) {
			setConfiguration((VectorLayerConfiguration) configuration);
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

			if (offset.x > size.x || offset.x < -size.x) {
				offset = new Point(0, 0);
				newBuffer();
			} else if (offset.y > size.y || offset.y < -size.y) {
				offset = new Point(0, 0);
				newBuffer();
			}
		}
		if (evt.getPropertyName().equals("scale")) {
			offset = new Point(0, 0);
			newBuffer();
		}
		manager.redraw();

	}

	class BufferTile {
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

		void update() {
			Envelope env = image.getEnvelope();
			Coordinate coord = new Coordinate(env.getMinX(), env.getMinY());
			Coordinate coord1 = new Coordinate(env.getMaxX(), env.getMaxY());
			this.minxy = transformation.transformCoord(coord, getTileServer().getSRID());
			this.maxxy = transformation.transformCoord(coord1, getTileServer().getSRID());
			this.width = Math.max(minxy[0], maxxy[0]) - Math.min(minxy[0], maxxy[0]);
			this.height = Math.max(minxy[1], maxxy[1]) - Math.min(minxy[1], maxxy[1]);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof AsyncImage)
				this.image.equals(obj);
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}

	@Override
	public Envelope getEnvelope() {
		Envelope env = this.tileServer.getEnvelope();
		int destSrid = this.manager.getSRID();
		if (this.getSRID() != destSrid) {
			CoordinateTransform ct = transformation.getCoordinateTransform(this.getSRID(), destSrid);
			ProjCoordinate destMin = new ProjCoordinate();
			ProjCoordinate destMax = new ProjCoordinate();
			ct.transform(new ProjCoordinate(env.getMinX(), env.getMinY()), destMin);
			ct.transform(new ProjCoordinate(env.getMaxX(), env.getMaxY()), destMax);
			return new Envelope(destMin.x, destMax.x, destMin.y, destMax.y);
		}
		return this.tileServer.getEnvelope();
	}

	/**
	 * Tries to get the geometry from the tuple at the given index. If it is a GeometryCollection, the centroid is used
	 * 
	 * @param tuple
	 *            The tuple that holds the geometry
	 * @param index
	 *            The index at which the geometry is in the tuple
	 * @return The geometry.
	 */
	public Geometry getGeometry(Tuple<?> tuple, int index) {

		Geometry geometry = null;
		Object attribute = tuple.getAttribute(index);

		if (attribute instanceof GeometryCollection) {
			GeometryCollection geoColl = (GeometryCollection) attribute;
			geometry = geoColl.getCentroid();
		} else if (attribute instanceof GeometryWrapper) {
			geometry = ((GeometryWrapper) attribute).getGeometry();
		} else if (attribute instanceof com.vividsolutions.jts.geom.Point) {
			geometry = (com.vividsolutions.jts.geom.Point) attribute;
		} else {
			throw new RuntimeException(
					"Tuple attribute type " + attribute.getClass() + " not supported for the map dashboard part.");
		}
		return geometry;
	}

	@Override
	public int getSRID() {
		return this.tileServer.getSRID();
	}

}
