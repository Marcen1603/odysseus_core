package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.BasicLayer;

public class MapMouseListener implements MouseListener, MouseWheelListener, MouseMoveListener, MouseTrackListener {
	
    public Point mouseCoords = new Point(0, 0);
    private Point downCoords;
    private Point downPosition;
    private BasicLayer basicLayer;
    
    
    public MapMouseListener(BasicLayer basicLayer) {
    	this.basicLayer = basicLayer;
	}

	public void mouseEnter(MouseEvent e) {
		basicLayer.getCanvas().forceFocus();
    }
    
    public void mouseExit(MouseEvent e) {
    }

    public void mouseHover(MouseEvent e) {
    }
    
    public void mouseDoubleClick(MouseEvent e) {
        if (e.button == 1) 
        	basicLayer.zoomIn(new Point(mouseCoords.x, mouseCoords.y));
        else if (e.button == 3)
        	basicLayer.zoomOut(new Point(mouseCoords.x, mouseCoords.y));
    }
    public void mouseDown(MouseEvent e) {
        if (e.button == 1 && (e.stateMask & SWT.CTRL) != 0) {
        	basicLayer.setCenterPosition(basicLayer.getCursorPosition());
        	basicLayer.getCanvas().redraw();
        }
        if (e.button == 1) {
            downCoords = new Point(e.x, e.y);
            downPosition = basicLayer.getMapPosition();
        }
    }
    public void mouseUp(MouseEvent e) {
        if (e.count == 1) {
            handleDrag(e);
        }
        downCoords = null;
        downPosition = null;
    }
    
    public void mouseMove(MouseEvent e) {
        handlePosition(e);
        handleDrag(e);
    }
    public void mouseScrolled(MouseEvent e) {
        if (e.count > 0)
        	basicLayer.zoomIn(new Point(mouseCoords.x, mouseCoords.y));
        else if (e.count < 0)
        	basicLayer.zoomOut(new Point(mouseCoords.x, mouseCoords.y));
    }
    
    private void handlePosition(MouseEvent e) {
        mouseCoords = new Point(e.x, e.y);
    }

    private void handleDrag(MouseEvent e) {
        if (downCoords != null) {
            int tx = downCoords.x - e.x;
            int ty = downCoords.y - e.y;
            basicLayer.setMapPosition(downPosition.x + tx, downPosition.y + ty);
            basicLayer.getCanvas().redraw();
        }
    }    
}
