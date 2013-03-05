package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;

import com.vividsolutions.jts.geom.Coordinate;


public class MapMouseListener implements MouseListener, MouseWheelListener,
		MouseMoveListener, MouseTrackListener {

	public Point mouseCoords = new Point(0, 0);
	private Point downCoords;
	private Coordinate downPosition;
	private ScreenManager screenManager;
	private StreamMapEditorPart editor;

	private Rectangle mouseSelection = null;

	public MapMouseListener(ScreenManager screenManager, StreamMapEditorPart editor) {
		this.screenManager = screenManager;
		this.editor = editor;
	}

	public void mouseEnter(MouseEvent e) {
		// Evil
		// basicLayer.getCanvas().forceFocus();
	}

	public void mouseExit(MouseEvent e) {
	}

	public void mouseHover(MouseEvent e) {
	}

	public void mouseDoubleClick(MouseEvent e) {
		if (!editor.isRectangleZoom()) {
			if (e.button == 1){
//				screenManager.zoomIn(new Point(mouseCoords.x, mouseCoords.y));
			}else if (e.button == 3){
//				screenManager.zoomOut(new Point(mouseCoords.x, mouseCoords.y));
			}
		}
	}

	public void mouseDown(MouseEvent e) {
		if (!editor.isRectangleZoom()) {
			if (e.button == 1 && (e.stateMask & SWT.CTRL) != 0) {
//				screenManager.setCenterPosition(screenManager
//						.getCursorPosition());
				screenManager.redraw();
			}
			if (e.button == 1) {
				downCoords = new Point(e.x, e.y);
				downPosition = screenManager.getCenterUV();
			}
		} else {
			mouseSelection = new Rectangle(e.x, e.y, 0, 0);

			screenManager.redraw();
		}
	}

	public void mouseUp(MouseEvent e) {
		if (!editor.isRectangleZoom()) {
			if (e.count == 1) {
				handleDrag(e);
			}
			downCoords = null;
			downPosition = null;
		} else {
			mouseSelection.width = mouseSelection.x - e.x;
			mouseSelection.height = mouseSelection.y - e.y;
			mouseSelection = null;
			
			screenManager.redraw();
			
		}
	}

	public void mouseMove(MouseEvent e) {
		if (!editor.isRectangleZoom()) {
			handlePosition(e);
			handleDrag(e);
		}
		else{
			if (mouseSelection != null) {
				mouseSelection.width = e.x - mouseSelection.x;
				mouseSelection.height = e.y - mouseSelection.y;
				
				screenManager.redraw();
			}
		}
	}

	public void mouseScrolled(MouseEvent e) {
//		if (!editor.isRectangleZoom()) {
			if (e.count > 0){
				screenManager.zoomIn(new Point(mouseCoords.x, mouseCoords.y));
			}else if (e.count < 0){
				screenManager.zoomOut(new Point(mouseCoords.x, mouseCoords.y));
			}
//		}
	}

	private void handlePosition(MouseEvent e) {
		mouseCoords = new Point(e.x, e.y);
//		System.out.println(mouseCoords + " " + screenManager.getMapPosition());
	}

	private void handleDrag(MouseEvent e) {
		if (!editor.isRectangleZoom()) {
			if (downCoords != null) {
				int tx = downCoords.x - e.x;
				int ty = downCoords.y - e.y;
				screenManager.setCenterUV(downPosition.x - tx, downPosition.y - ty);
				
				screenManager.getCanvas().redraw();
			}
		}
	}

	public Rectangle getSelection() {
		return mouseSelection;
	}
}
