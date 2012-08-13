package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.MapMouseListener;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.PointD;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.ProjectionUtil;

public class BasicLayer extends AbstractLayer{

	private ScreenManager screenmanager;
	private ScreenTransformation transformation;
	private Display display;
	private Canvas canvas;
	
	private Color waitBackground, waitForeground;
	private Point mapSize = new Point(0, 0);
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private MapMouseListener mouseListener;
	
	public BasicLayer(ScreenManager screenmanager) {
		this.screenmanager = screenmanager;
		this.transformation = screenmanager.getTransformation();
		transformation.setBasicLayer(this);
		
		this.display = screenmanager.getDisplay();
		this.canvas = screenmanager.getCanvas();
		
		waitBackground = new Color(display, 160, 160, 160);
		waitForeground = new Color(display, 200, 200, 200);	
		
		Point mapPosition = new Point(0, 0);
		
		setZoom(1);
		canvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				BasicLayer.this.widgetDisposed(e);
			}
		});
		setMapPosition(mapPosition);
		
		this.name = "Basic Layer";
		
		mouseListener = new MapMouseListener(this);
		
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMoveListener(mouseListener);
		canvas.addMouseWheelListener(mouseListener);
		canvas.addMouseTrackListener(mouseListener);
	}
	
	
	@Override
	public void draw(GC gc) {
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
			}
			dy += ProjectionUtil.TILE_SIZE;
		}
		gc.setForeground(new Color(gc.getDevice(),255,0,0));
		gc.drawText(screenmanager.getInfoText(), 0 , 0,true); 

	}
	
	private void paintTile(GC gc, int dx, int dy, int x, int y) {
// Do not draw the background.
//		boolean DEBUG = false;
//		boolean DRAW_OUT_OF_BOUNDS = !false;
//
//		boolean imageDrawn = false;
//		int xTileCount = 1 << transformation.getZoom();
//		int yTileCount = 1 << transformation.getZoom();
//		boolean tileInBounds = x >= 0 && x < xTileCount && y >= 0
//				&& y < yTileCount;
//		
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
	
	public Point getMapPosition() {
		return new Point(transformation.getMapPosition().x, transformation.getMapPosition().y);
	}

	public void setMapPosition(Point mapPosition) {
		setMapPosition(mapPosition.x, mapPosition.y);
	}

	public void setMapPosition(int x, int y) {
		if (transformation.getMapPosition().x == x && transformation.getMapPosition().y == y)
			return;
		Point oldMapPosition = getMapPosition();
		transformation.getMapPosition().x = x;
		transformation.getMapPosition().y = y;
		pcs.firePropertyChange("mapPosition", oldMapPosition, getMapPosition());
	}

	public void translateMapPosition(int tx, int ty) {
		setMapPosition(transformation.getMapPosition().x + tx, transformation.getMapPosition().y + ty);
	}


	public void setZoom(int zoom) {
		if (zoom == transformation.getZoom())
			return;
		transformation.getZoomStamp().incrementAndGet();
		int oldZoom = transformation.getZoom();
		transformation.setZoom(zoom);
		mapSize.x = getXMax();
		mapSize.y = getYMax();
		pcs.firePropertyChange("zoom", oldZoom, zoom);
	}

	public void zoomIn(Point pivot) {
		Point mapPosition = getMapPosition();
		int dx = pivot.x;
		int dy = pivot.y;
		setZoom(transformation.getZoom() + 1);
		setMapPosition(mapPosition.x * 2 + dx, mapPosition.y * 2 + dy);
		canvas.redraw();
	}

	public void zoomOut(Point pivot) {
		if (transformation.getZoom() <= 1)
			return;
		Point mapPosition = getMapPosition();
		int dx = pivot.x;
		int dy = pivot.y;
		setZoom(transformation.getZoom() - 1);
		setMapPosition((mapPosition.x - dx) / 2, (mapPosition.y - dy) / 2);
		canvas.redraw();
	}

	public int getXTileCount() {
		return (1 << transformation.getZoom());
	}

	public int getYTileCount() {
		return (1 << transformation.getZoom());
	}

	public int getXMax() {
		return ProjectionUtil.TILE_SIZE * getXTileCount();
	}

	public int getYMax() {
		return ProjectionUtil.TILE_SIZE * getYTileCount();
	}

	public Point getCursorPosition() {
		return new Point(transformation.getMapPosition().x + mouseListener.mouseCoords.x,
				transformation.getMapPosition().y + mouseListener.mouseCoords.y);
	}

	public Point getTile(Point position) {
		return new Point((int) Math.floor(((double) position.x)
				/ ProjectionUtil.TILE_SIZE),
				(int) Math.floor(((double) position.y)
						/ ProjectionUtil.TILE_SIZE));
	}

	public Point getCenterPosition() {
		org.eclipse.swt.graphics.Point size = canvas.getSize();
		return new Point(transformation.getMapPosition().x + size.x / 2, transformation.getMapPosition().y + size.y / 2);
	}

	public void setCenterPosition(Point p) {
		org.eclipse.swt.graphics.Point size = canvas.getSize();
		setMapPosition(p.x - size.x / 2, p.y - size.y / 2);
	}

	public PointD getLongitudeLatitude(Point position) {
		return new PointD(ProjectionUtil.position2lon(position.x, transformation.getZoom()),
				ProjectionUtil.position2lat(position.y, transformation.getZoom()));
	}

	public Point computePosition(PointD coords) {
		int x = ProjectionUtil.lon2position(coords.x, transformation.getZoom());
		int y = ProjectionUtil.lat2position(coords.y, transformation.getZoom());
		return new Point(x, y);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	public Canvas getCanvas(){
		return canvas;
	}
	
	public ScreenManager getScreenManager(){
		return screenmanager;
	}
	
	protected void widgetDisposed(DisposeEvent e) {
		waitBackground.dispose();
		waitForeground.dispose();
	}
	
}
