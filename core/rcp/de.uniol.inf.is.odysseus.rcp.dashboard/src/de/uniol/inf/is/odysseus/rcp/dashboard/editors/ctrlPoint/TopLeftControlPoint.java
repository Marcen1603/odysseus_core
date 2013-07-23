package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import org.eclipse.swt.events.MouseEvent;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public class TopLeftControlPoint extends DraggingControlPointBehaviour {

	@Override
	public int getPositionX(DashboardPartPlacement place) {
		return place.getX() - ControlPoint.CONTROL_POINT_SIZE_PIXELS;
	}

	@Override
	public int getPositionY(DashboardPartPlacement place) {
		return place.getY() - ControlPoint.CONTROL_POINT_SIZE_PIXELS;
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
		
		place.setX( place.getX() - distX);
		place.setY( place.getY() - distY);
		place.setWidth( place.getWidth() + distX);
		place.setHeight( place.getHeight() + distY);
	}
}
