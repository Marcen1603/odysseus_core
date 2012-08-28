package de.offis.salsa.obsrec.ui.paint;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import de.offis.salsa.obsrec.ui.BoxWorldPanel;

public class MouseDragZoomListener implements MouseListener, MouseMotionListener, MouseWheelListener {

	private int startX;
	private int startY;
	
	private BoxWorldPanel objectworldWidget;
	
	public MouseDragZoomListener(BoxWorldPanel objectworldWidget) {
		this.objectworldWidget = objectworldWidget;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = e.getXOnScreen();
		startY = e.getYOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() < 0){
			objectworldWidget.zoomIn(e.getX(), e.getY());
		} else if (e.getWheelRotation() > 0) {
			objectworldWidget.zoomOut(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int wegX = e.getXOnScreen() - startX;
        int wegY = e.getYOnScreen() - startY;

        startX = e.getXOnScreen();
        startY = e.getYOnScreen();
        
        double newX = objectworldWidget.getOffset()[0] + wegX;
        double newY = objectworldWidget.getOffset()[1] + wegY;
        
        objectworldWidget.setOffset(newX, newY);		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
