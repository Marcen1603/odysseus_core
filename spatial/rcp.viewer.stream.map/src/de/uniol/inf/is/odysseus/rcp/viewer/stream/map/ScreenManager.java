/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile.PointD;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.ProjectionUtil;

public class ScreenManager {

	private static final Logger LOG = LoggerFactory.getLogger(ScreenManager.class);
	private static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

	private StreamMapEditor editor;
	private ScreenTransformation transformation;
	private Canvas canvas;
	private Point mapSize = new Point(0, 0);
	
	private String infoText;
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private MapMouseListener mouseListener;
	
	public ScreenManager(ScreenTransformation transformation, StreamMapEditor editor) {
		this.transformation = transformation;
		this.transformation.setScreenManager(this);
		
		this.editor = editor;
		setZoom(1);
		Point mapPosition = new Point(0, 0);
		setZoom(1);
		setMapPosition(mapPosition);
	}

	protected Canvas createCanvas(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(WHITE);
		canvas.addPaintListener(new GeometryPaintListener(editor));
		
		
//		canvasViewer.addControlListener(new ControlListener() {
//
//			@Override
//			public void controlResized(ControlEvent e) {
//				//transformation.updateOrigin(viewer.getClientArea());
//				LOG.debug("Resize: " + " min=" + viewer.getClientArea().x + ","
//						+ viewer.getClientArea().y + " max="
//						+ viewer.getClientArea().width + ","
//						+ viewer.getClientArea().height);
//			}
//
//			@Override
//			public void controlMoved(ControlEvent e) {
//			}
//		});
//
//		canvasViewer.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseUp(MouseEvent e) {
//				// transformation.update(getRect());
//
//				mouseSelection.width = e.x;
//				mouseSelection.height = e.y;
//				// transformation.updateCurrent(mouseSelection);
// 
//				// setRect(null);
//				mouseSelection = null;
// 
//				//LOG.debug("OnMouseUp: " + e.x + "," + e.y);
//				// LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" +
//				// transformation.getLon(e.y)) ;
//
//				if (hasCanvasViewer() && !getCanvas().isDisposed()) {
//					PlatformUI.getWorkbench().getDisplay()
//							.asyncExec(new Runnable() {
//								@Override
//								public void run() {
//									if (!getCanvas().isDisposed())
//										getCanvas().redraw();
//								}
//							});
//				}
//			}
//
//			@Override
//			public void mouseDown(MouseEvent e) {
//
//				mouseSelection = new Rectangle(e.x, e.y, 0, 0);
//				//LOG.debug("OnMouseDown: " + e.x + "," + e.y);
//				// LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" +
//				// transformation.getLon(e.y)) ;
//			}
//
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//				//LOG.error("Mouse Double Click is not implemented");
//			}
//		});
//
//		canvasViewer.addMouseMoveListener(new MouseMoveListener() {
//
//			@Override
//			public void mouseMove(MouseEvent e) {
//				infoText = "";
//				infoText += "Screen Coordinate: " + e.x + "," + e.y + "\n";
//			    infoText += "Zoom: " + transformation.getZoom() + "\n";	
//			    infoText += "Size: " + viewer.getSize() + "\n";
//			    infoText += "Position: " + transformation.getMapPosition() + "\n";	
//			    infoText += "Center: " + transformation.getBasicLayer().getCenterPosition() + "\n";	
//			    
//			    infoText += "Courser: " + transformation.getBasicLayer().getCursorPosition() + "\n";	
//			    
//			    infoText += "Lat: " + ProjectionUtil.position2lat(transformation.getBasicLayer().getCursorPosition().y, transformation.getZoom()) + "\n";	
//			    infoText += "Lon: " + ProjectionUtil.position2lon(transformation.getBasicLayer().getCursorPosition().x, transformation.getZoom()) + "\n";	
//
//
////				if (hasCanvasViewer() && !getCanvas().isDisposed()) {
////					PlatformUI.getWorkbench().getDisplay()
////							.asyncExec(new Runnable() {
////								@Override
////								public void run() {
////									if (!getCanvas().isDisposed())
////										getCanvas().redraw();
////								}
////							});
////				}
//
//			}
//		});
//
		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == 'z')
					transformation.zoomin(1);

				if (e.character == 'x')
					transformation.zoomout(1);

				if (hasCanvasViewer() && !getCanvas().isDisposed()) {
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!getCanvas().isDisposed())
										getCanvas().redraw();
								}
							});
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP)
					transformation.panNorth(10);
				if (e.keyCode == SWT.ARROW_DOWN)
					transformation.panSouth(10);
				if (e.keyCode == SWT.ARROW_LEFT)
					transformation.panWest(10);
				if (e.keyCode == SWT.ARROW_RIGHT)
					transformation.panEast(10);
				if (hasCanvasViewer() && !getCanvas().isDisposed()) {
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!getCanvas().isDisposed())
										getCanvas().redraw();
								}
							});
				}
			}

		});

		mouseListener = new MapMouseListener(this, editor);
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMoveListener(mouseListener);
		canvas.addMouseWheelListener(mouseListener);
		canvas.addMouseTrackListener(mouseListener);
		
		return canvas;
	}

	public Point getCursorPosition() {
		return new Point(transformation.getMapPosition().x +  mouseListener.mouseCoords.x,
				transformation.getMapPosition().y + mouseListener.mouseCoords.y);
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

	public Rectangle getMouseSelection() {
		return mouseListener.getSelection();
	}
	
	public String getInfoText() {
		return infoText;
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
	
}
