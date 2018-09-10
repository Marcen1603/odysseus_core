package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;


public abstract class DraggingControlPointBehaviour implements IControlPointBehaviour {
	
	private static final Cursor CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_CROSS);

	private boolean isDragging;

	private int dragX;
	private int dragY;

	private ControlPoint point;

	@Override
	public void setControlPoint(ControlPoint point) {
		this.point = point;
	}
	
	@Override
	public Cursor getCursor() {
		return CURSOR;
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (point.isInside(e.x, e.y)) {
			isDragging = true;
			dragX = e.x;
			dragY = e.y;
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		isDragging = false;
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		if( isDragging ) {
			final int distX = dragX - e.x;
			final int distY = dragY - e.y;
			
			dragPoint( point, distX, distY );
			
			point.getDashboard().update();		
			
			dragX = e.x;
			dragY = e.y;
		}
	}

	protected abstract void dragPoint( ControlPoint point, int distX, int distY );
}
