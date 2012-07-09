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

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tool.MouseLabel;

public class ScreenManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScreenManager.class);
	private static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	
	private StreamMapEditor editor;
	private ScreenTransformation transformation;
	private Canvas viewer;	
	private Rectangle mouseSelection = null;
	private MouseLabel mouseLabel   = new MouseLabel();
	
	
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
				transformation.updateOrigin(viewer.getClientArea());
				LOG.debug("Resize: " + " min=" + viewer.getClientArea().x + "," + viewer.getClientArea().y + " max=" + viewer.getClientArea().width + "," + viewer.getClientArea().height );
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});

		canvasViewer.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				//transformation.update(getRect());
				
				mouseSelection.width = e.x;
				mouseSelection.height = e.y;
				//transformation.updateCurrent(mouseSelection);
				
				//setRect(null);
				mouseSelection = null;

				LOG.debug("OnMouseUp: " + e.x + "," + e.y);
				//LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" + transformation.getLon(e.y)) ;
				
				if (hasCanvasViewer() && !getCanvasViewer().isDisposed()) {
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!getCanvasViewer().isDisposed())
										getCanvasViewer().redraw();
								}
							});
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
				mouseSelection = new Rectangle(e.x, e.y, 0, 0);
				LOG.debug("OnMouseDown: " + e.x + "," + e.y);	
				//LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" + transformation.getLon(e.y)) ;
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				LOG.error("Mouse Double Click is not implemented");
			}
		});

		canvasViewer.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) { 
				mouseLabel.label =  "Screen Coordinate: " + e.x + "," + e.y + "\n";
				
				
				double[] map4326 	= transformation.screenToEpsg4326(e.x, e.y);
				
				double[] map3857 	= transformation.epsg4326To(3395,map4326[0], map4326[1]);
				
				int[] scCoord 		= transformation.epsg4326tToScreen(map4326[0], map4326[1]);
				
				
				mouseLabel.label += "Computed Coordinate: " + scCoord[0] + ", " + scCoord[1] + "\n";	
				mouseLabel.label += "4326 Coordinate: " + map4326[0] + ", " + map4326[1] + "\n";	
				mouseLabel.label += "3857 Coordinate: " + map3857[0] + ", " + map3857[1] + "\n";	
				
				mouseLabel.x = e.x;
				mouseLabel.y = e.y;
				
				// TODO Auto-generated method stub
				if (mouseSelection != null) {
					mouseSelection.width = e.x - mouseSelection.x;
					mouseSelection.height = e.y - mouseSelection.y;
				}
				
				if (hasCanvasViewer() && !getCanvasViewer().isDisposed()) {
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!getCanvasViewer().isDisposed())
										getCanvasViewer().redraw();
								}
							});
				}

				//LOG.debug("Mouse: x=" + e.x + " y=" + e.y);
				//LOG.debug("Map: x=" + transformation.getLat(e.x) + " y=" + transformation.getLon(e.y)) ;
				
			}
		});
		
		
		
		canvasViewer.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == '+')
					transformation.zoomin();
				if (e.character == '-')
					transformation.zoomout();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP)
					transformation.panNorth();
				if (e.keyCode == SWT.ARROW_DOWN)
					transformation.panSouth();
				if (e.keyCode == SWT.ARROW_LEFT)
					transformation.panWest();
				if (e.keyCode == SWT.ARROW_RIGHT)
					transformation.panEast();
			}
		});
		return canvasViewer;
	}

	protected final Canvas getCanvasViewer() {
		return viewer;
	}

	protected final boolean hasCanvasViewer() {
		return getCanvasViewer() != null;
	}

	public void setCanvasViewer(Canvas viewer) {
		if (viewer != null) {
			this.viewer = viewer;
		} else {
			LOG.error("Canvas Viewer is null.");
		}
	}

	public ScreenTransformation getTransformation() {
		return transformation;
	}
	
	public Rectangle getMouseSelection(){
		return mouseSelection;
	}
	
	public MouseLabel getMouseLabel(){
		return mouseLabel;
	}
	
}
