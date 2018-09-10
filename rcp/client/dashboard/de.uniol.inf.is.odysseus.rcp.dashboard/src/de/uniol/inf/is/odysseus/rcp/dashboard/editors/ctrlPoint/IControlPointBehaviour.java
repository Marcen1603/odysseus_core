package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public interface IControlPointBehaviour extends MouseMoveListener, MouseListener {

	public void setControlPoint( ControlPoint point);
	
	public int getPositionX(DashboardPartPlacement place);
	public int getPositionY(DashboardPartPlacement place);
	public Cursor getCursor();
	
	public void dispose();
	
}
