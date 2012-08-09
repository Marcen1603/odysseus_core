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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.ProjectionUtil;

public class ScreenManager {

	private static final Logger LOG = LoggerFactory.getLogger(ScreenManager.class);
	private static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

	private StreamMapEditor editor;
	private ScreenTransformation transformation;
	private Canvas viewer;
	private Rectangle mouseSelection = null;
	private String infoText = "";

	public ScreenManager(ScreenTransformation transformation, StreamMapEditor editor) {
		this.transformation = transformation;
		this.editor = editor;
	}

	protected Canvas createCanvas(Composite parent) {
		Canvas canvasViewer = new Canvas(parent, SWT.NONE);
		canvasViewer.setBackground(WHITE);
		canvasViewer.addPaintListener(new GeometryPaintListener(editor));

		canvasViewer.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				//transformation.updateOrigin(viewer.getClientArea());
				LOG.debug("Resize: " + " min=" + viewer.getClientArea().x + ","
						+ viewer.getClientArea().y + " max="
						+ viewer.getClientArea().width + ","
						+ viewer.getClientArea().height);
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});

		canvasViewer.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// transformation.update(getRect());

				mouseSelection.width = e.x;
				mouseSelection.height = e.y;
				// transformation.updateCurrent(mouseSelection);

				// setRect(null);
				mouseSelection = null;

				//LOG.debug("OnMouseUp: " + e.x + "," + e.y);
				// LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" +
				// transformation.getLon(e.y)) ;

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
			public void mouseDown(MouseEvent e) {

				mouseSelection = new Rectangle(e.x, e.y, 0, 0);
				//LOG.debug("OnMouseDown: " + e.x + "," + e.y);
				// LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" +
				// transformation.getLon(e.y)) ;
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				//LOG.error("Mouse Double Click is not implemented");
			}
		});

		canvasViewer.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				infoText = "";
				infoText += "Screen Coordinate: " + e.x + "," + e.y + "\n";
			    infoText += "Zoom: " + transformation.getZoom() + "\n";	
			    infoText += "Size: " + viewer.getSize() + "\n";
			    infoText += "Position: " + transformation.getMapPosition() + "\n";	
			    infoText += "Center: " + transformation.getBasicLayer().getCenterPosition() + "\n";	
			    
			    infoText += "Courser: " + transformation.getBasicLayer().getCursorPosition() + "\n";	
			    
			    infoText += "Lat: " + ProjectionUtil.position2lat(transformation.getBasicLayer().getCursorPosition().y, transformation.getZoom()) + "\n";	
			    infoText += "Lon: " + ProjectionUtil.position2lon(transformation.getBasicLayer().getCursorPosition().x, transformation.getZoom()) + "\n";	


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

		canvasViewer.addKeyListener(new KeyListener() {

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
		return canvasViewer;
	}

	public final Canvas getCanvas() {
		return viewer;
	}

	public final boolean hasCanvasViewer() {
		return getCanvas() != null;
	}

	public void setCanvasViewer(Canvas viewer) {
		if (viewer != null) {
			this.viewer = viewer;
		} else {
			LOG.error("Canvas Viewer is null.");
		}
	}

	public final Display getDisplay() {
		return viewer.getDisplay();
	}
	
	public ScreenTransformation getTransformation() {
		return transformation;
	}

	public Rectangle getMouseSelection() {
		return mouseSelection;
	}
	
	public String getInfoText() {
		return infoText;
	}

}
