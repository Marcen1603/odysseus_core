package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard.MapDashboardPart;
//import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;

public class ScreenManager {

	private static final Logger LOG = LoggerFactory.getLogger(ScreenManager.class);
	private static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

	private MapDashboardPart mapDashboardPart;
	private ScreenTransformation transformation;
	private Canvas canvas;

	private ITimeInterval interval;
	private ITimeInterval maxInterval;

	private Buffer connection;

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private MapMouseListener mouseListener;

	private Coordinate centerEPSG = new Coordinate(0, 0);
	private Coordinate centerUV = new Coordinate(0, 0);

	private CoordinateReferenceSystem crs;

	// mapSizeScreenCoord
	private int widthHalf, heightHalf = 175;
	private int height;
	private int srid = -1;
	private int milliseconds = 20;

	private double scale = 100000;
	private double zoomincrement = 1.5;

	private boolean renderComplete;

	private String infoText;

	private Renderer renderer = null;

	public ScreenManager(ScreenTransformation transformation, MapDashboardPart mapDashboardPart) {
		this.interval = new TimeInterval();
		this.interval.setStart(PointInTime.getZeroTime());
		this.maxInterval = new TimeInterval();
		this.transformation = transformation;
		this.transformation.setScreenManager(this);
		this.mapDashboardPart = mapDashboardPart;
		setSRID(mapDashboardPart.getMapEditorModel().getSRID());
		ProjCoordinate p = new ProjCoordinate(0, 0);
		if (this.crs != null)
			this.crs.getProjection().project(new ProjCoordinate(0, 0), p);
		setCenterUV((int) (p.x / scale), (int) (p.y / scale));
	}

	public void setSRID(int srid) {
		if (this.srid != srid) {
			this.srid = srid;
			if (srid != 0) {
				CRSFactory csFactory = new CRSFactory();
				this.crs = csFactory.createFromName("EPSG:" + this.srid);
				this.crs.getProjection().initialize();
			}
		}
	}

	public int getSRID() {
		return srid;
	}

	public Coordinate getCenter() {
		return (Coordinate) this.centerEPSG.clone();
	}

	void setCenterUV(double x, double y) {
		Coordinate oldCenterUV = centerUV;
		centerUV = new Coordinate(x, y);
		if (height != 0) {
			centerEPSG.x = -centerUV.x * scale;
			centerEPSG.y = (centerUV.y) * scale;
		}
		this.pcs.firePropertyChange("centerUV", oldCenterUV, centerUV);
	}

	public Coordinate getCenterUV() {
		return new Coordinate(this.centerUV.x, this.centerUV.y);
	}

	public double getScale() {
		return this.scale;
	}

	protected void setOffset(int width, int height) {
		this.height = height;
		this.widthHalf = (int) (0.5d * width);
		this.heightHalf = (int) (0.5d * height);
		this.pcs.firePropertyChange("mapSize", null, null);
	}

	public int[] getOffset() {
		return new int[] { widthHalf, heightHalf };
	}

	public Canvas createCanvas(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		canvas.setBackground(WHITE);
		canvas.addPaintListener(new GeometryPaintListener(mapDashboardPart));

		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				redraw();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP)
					panNorth(10);
				if (e.keyCode == SWT.ARROW_DOWN)
					panSouth(10);
				if (e.keyCode == SWT.ARROW_LEFT)
					panWest(10);
				if (e.keyCode == SWT.ARROW_RIGHT)
					panEast(10);
				redraw();
			}

		});

		mouseListener = new MapMouseListener(this, mapDashboardPart);
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMoveListener(mouseListener);
		canvas.addMouseWheelListener(mouseListener);
		canvas.addMouseTrackListener(mouseListener);
		canvas.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				setOffset(getCanvas().getSize().x, getCanvas().getSize().y);
				redraw();
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
		return canvas;
	}

	public final Canvas getCanvas() {
		return canvas;
	}

	public final boolean hasCanvasViewer() {
		return getCanvas() != null;
	}

	public void setCanvasViewer(Canvas viewer) {
		if (viewer != null) {
			this.canvas = viewer;
		} else {
			LOG.error("Canvas Viewer is null.");
		}
	}

	public final Display getDisplay() {
		return canvas.getDisplay();
	}

	public ScreenTransformation getTransformation() {
		return transformation;
	}
	
	public MapDashboardPart getMapDashboardPart(){
		return mapDashboardPart;
	}

	public Rectangle getMouseSelection() {
		return mouseListener.getSelection();
	}

	public String getInfoText() {
		return infoText;
	}

	public Envelope getViewportWorldCoord() {
		return getViewportWorldCoord(this.widthHalf, this.heightHalf);
	}

	public Envelope getViewportWorldCoord(int widthHalf, int heightHalf) {
		Envelope env = new Envelope(this.centerEPSG);
		env.expandBy(widthHalf * scale, heightHalf * scale);
		return env;
	}

//	public void zoomToExtend(ILayer layer) {
//		Envelope env = layer.getEnvelope();
//		Point screenSize = canvas.getSize();
//		double scaleX = (env.getMaxX() - env.getMinX()) / (screenSize.x);
//		double scaleY = (env.getMaxY() - env.getMinY()) / (screenSize.y);
//		double oldScale = scale;
//		scale = 1;
//		double scaleInt = 1;
//		for (scaleInt = (Math.max(scaleX, scaleY)); scaleInt > 10; scaleInt /= 10) {
//			scale *= 10;
//		}
//		scale *= Math.ceil(scaleInt);
//		if (scale > 0) {
//			// This should always be the case, if there is an environment
//			setCenterUV((int) Math.floor((env.centre().x / scale * (-1))), (int) Math.floor((env.centre().y / scale)));
//		} else {
//			scale = 1;
//		}
//		pcs.firePropertyChange("scale", oldScale, scale);
//		redraw();
//	}

	public ITimeInterval getInterval() {
		return interval;
	}

	public void setInterval(ITimeInterval interval) {
		ITimeInterval oldInterval = this.interval;
		this.interval = interval;
		this.pcs.firePropertyChange("interval", oldInterval, this.interval);
	}

	public PointInTime getIntervalStart() {
		return this.interval.getStart();
	}

	public void setIntervalStart(PointInTime intervalStart) {
		PointInTime oldIntervalStart = this.interval.getStart();
		this.interval.setStart(intervalStart);
		this.pcs.firePropertyChange("intervalStart", oldIntervalStart, this.interval.getStart());
	}

	public PointInTime getIntervalEnd() {
		return this.interval.getEnd();
	}

	public void setIntervalEnd(PointInTime intervalEnd) {
		PointInTime oldIntervalEnd = this.interval.getEnd();
		this.interval.setEnd(intervalEnd);
		this.pcs.firePropertyChange("intervalEnd", oldIntervalEnd, this.interval.getEnd());
	}

	public ITimeInterval getMaxInterval() {
		return maxInterval;
	}

	public void setMaxInterval(ITimeInterval maxInterval) {
		ITimeInterval oldMaxInterval = this.maxInterval;
		this.maxInterval = maxInterval;
		this.pcs.firePropertyChange("maxInterval", oldMaxInterval, this.maxInterval);
	}

	public PointInTime getMaxIntervalStart() {
		return this.maxInterval.getStart();
	}

	public void setMaxIntervalStart(PointInTime maxIntervalStart) {
		PointInTime oldMaxIntervalStart = this.maxInterval.getStart();
		ITimeInterval oldInterval = (ITimeInterval) this.maxInterval.clone();
		this.maxInterval.setStart(maxIntervalStart);
		if (this.interval.getStart().before(maxIntervalStart)) {
			// If the puffer deleted something, we have to update the interval
			this.interval.setStart(maxIntervalStart);
		}
		this.pcs.firePropertyChange("maxIntervalStart", oldMaxIntervalStart, this.maxInterval.getStart());
		this.pcs.firePropertyChange("maxInterval", oldInterval, this.maxInterval);
	}

	public PointInTime getMaxIntervalEnd() {
		return this.maxInterval.getEnd();
	}

	public void setMaxIntervalEnd(PointInTime maxIntervalEnd) {
		ITimeInterval oldInterval = (ITimeInterval) this.maxInterval.clone();
		if (maxIntervalEnd.before(this.maxInterval.getStart()))
			this.setMaxIntervalStart(maxIntervalEnd);
		if (this.interval.getStart().getMainPoint() == 0) {
			// No time was set
			this.interval.setStart(getMaxIntervalStart());
			this.interval.setEnd(getMaxIntervalEnd());
		}
		PointInTime oldMaxIntervalEnd = this.maxInterval.getEnd();
		this.maxInterval.setEnd(maxIntervalEnd.plus(1));
		this.pcs.firePropertyChange("maxIntervalEnd", oldMaxIntervalEnd, this.maxInterval.getEnd());
		this.pcs.firePropertyChange("maxInterval", oldInterval, this.maxInterval);
	}

	public void zoomOut(Point pivot) {

		double oldScale = this.scale;
		this.scale *= zoomincrement;
		Coordinate c = this.getCenterUV();
		Coordinate p = new Coordinate(pivot.x - widthHalf, pivot.y - heightHalf);
		double dx = Math.max(p.x, c.x) - Math.min(p.x, c.x);
		double dy = Math.max(p.y, c.y) - Math.min(p.y, c.y);
		Coordinate nc = new Coordinate();
		if (p.x < c.x)
			nc.x = (c.x - (dx * (zoomincrement - 1)) / zoomincrement);
		else
			nc.x = (c.x + (dx * (zoomincrement - 1)) / zoomincrement);
		if (p.y < c.y)
			nc.y = (c.y - (dy * (zoomincrement - 1)) / zoomincrement);
		else
			nc.y = (c.y + (dy * (zoomincrement - 1)) / zoomincrement);
		this.setCenterUV(nc.x, nc.y);
		pcs.firePropertyChange("scale", oldScale, scale);
		redraw();
	}

	public void zoomIn(Point pivot) {
		double oldScale = this.scale;
		this.scale /= zoomincrement;
		Coordinate c = this.getCenterUV();
		Coordinate c1 = (Coordinate) c.clone();
		c1.x *= zoomincrement;
		c1.y *= zoomincrement;
		Coordinate p = new Coordinate((pivot.x - widthHalf), (pivot.y - heightHalf));
		double dx = Math.max(p.x, c.x) - Math.min(p.x, c.x);
		double dy = Math.max(p.y, c.y) - Math.min(p.y, c.y);
		Coordinate nc = new Coordinate();
		if (p.x < c.x)
			nc.x = c.x + dx * (zoomincrement - 1);
		else
			nc.x = c.x - dx * (zoomincrement - 1);
		if (p.y < c.y) {
			nc.y = c.y + dy * (zoomincrement - 1);
		} else {
			nc.y = c.y - dy * (zoomincrement - 1);
		}
		this.setCenterUV(nc.x, nc.y);
		pcs.firePropertyChange("scale", oldScale, scale);
		redraw();
	}

	public void panNorth(int steps) {
		setCenterUV(getCenterUV().x, getCenterUV().y - steps);
	}

	public void panSouth(int steps) {
		setCenterUV(getCenterUV().x, getCenterUV().y + steps);
	}

	public void panWest(int steps) {
		setCenterUV(getCenterUV().x - steps, getCenterUV().y);
	}

	public void panEast(int steps) {
		setCenterUV(getCenterUV().x + steps, getCenterUV().y);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	public void redraw() {
		if (renderer == null) {
			renderer = new Renderer();
			getDisplay().timerExec(milliseconds, renderer);
		}
		renderer.setRedrawIntent(true);
	}

	public void setRenderComplete(boolean b) {
		renderComplete = b;

	}

	/**
	 * Adds a connection (as a layerUpdater) e.g. important for the time slider
	 * 
	 * @param connection
	 */
	public void addConnection(Buffer connection) {
		this.connection = connection;
	}
	
	public Buffer getConnection()	{
		return this.connection;
	}


	private class Renderer implements Runnable {

		private boolean redrawIntent = false;

		@Override
		public void run() {
			if (renderComplete == true & redrawIntent == true) {
				setRenderComplete(false);
				setRedrawIntent(false);
				canvas.redraw();
			}
			if (!canvas.isDisposed())
				canvas.getDisplay().timerExec(milliseconds, renderer);
		}

		public void setRedrawIntent(boolean b) {
			redrawIntent = b;
		}

	}

}
