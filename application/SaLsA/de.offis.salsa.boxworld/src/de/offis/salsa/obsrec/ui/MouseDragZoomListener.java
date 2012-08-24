package de.offis.salsa.obsrec.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseDragZoomListener implements MouseListener, MouseMotionListener, MouseWheelListener {

	private ObjectworldWidget objectworldWidget;
	
	public MouseDragZoomListener(ObjectworldWidget objectworldWidget) {
		this.objectworldWidget = objectworldWidget;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println("mmw");
		if(e.getWheelRotation() < 0){
			CanvasElement.zoom += 0.01;
		} else if (e.getWheelRotation() > 0) {
			CanvasElement.zoom -= 0.01;
		}
	}

	int x = 0;
	int y = 0;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		e.getX();
		e.getY();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
