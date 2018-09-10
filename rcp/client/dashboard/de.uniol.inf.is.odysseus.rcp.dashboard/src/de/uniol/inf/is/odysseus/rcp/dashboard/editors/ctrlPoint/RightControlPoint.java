package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import org.eclipse.swt.events.MouseEvent;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public class RightControlPoint extends DraggingControlPointBehaviour {

	@Override
	public int getPositionX(DashboardPartPlacement place) {
		return place.getX() + place.getWidth();
	}

	@Override
	public int getPositionY(DashboardPartPlacement place) {
		return place.getY() + (place.getHeight() / 2);
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
		place.setWidth( place.getWidth() - distX);
	}
}
