package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public class MoveControlPoint extends DraggingControlPointBehaviour {

	private static final Cursor CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEALL);

	public enum Position {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	}
	
	private final Position position;
	
	public MoveControlPoint( Position position ) {
		this.position = position;
	}
	
	@Override
	public int getPositionX(DashboardPartPlacement place) {
		switch( position ) {
		case TOP:
		case BOTTOM:
			return place.getX() + ( place.getWidth() / 2) - ControlPoint.CONTROL_POINT_SIZE_PIXELS;
		case LEFT:
			return place.getX() - ControlPoint.CONTROL_POINT_SIZE_PIXELS;
		case RIGHT:
			return place.getX() + place.getWidth();
		default:
			throw new IllegalArgumentException("Position " + position + " not known");
		}
	}
	
	@Override
	public Cursor getCursor() {
		return CURSOR;
	}

	@Override
	public int getPositionY(DashboardPartPlacement place) {
		switch( position ){
		case LEFT:
		case RIGHT:
			return place.getY() + (place.getHeight() / 2) - ControlPoint.CONTROL_POINT_SIZE_PIXELS;
		case TOP:
			return place.getY() - ControlPoint.CONTROL_POINT_SIZE_PIXELS;
		case BOTTOM:
			return place.getY() + place.getHeight();
		default:
			throw new IllegalArgumentException("Position " + position + " not known");
		}
	}

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// do nothing
	}

	@Override
	protected void dragPoint(ControlPoint point, int distX, int distY) {
		DashboardPartPlacement place = point.getDashboardPartPlacement();
		
		place.setX( place.getX() - distX );
		place.setY( place.getY() - distY );
	}

}
