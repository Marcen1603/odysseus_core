package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.BottomLeftControlPoint;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.BottomRightControlPoint;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.TopLeftControlPoint;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.TopRightControlPoint;

public class ControlPointManager implements ISelectionChangedListener, MouseListener, MouseMoveListener {

	private final Dashboard dashboard;
	private final Cursor arrowCursor;

	private ControlPoint topLeftPoint;
	private ControlPoint topRightPoint;
	private ControlPoint bottomLeftPoint;
	private ControlPoint bottomRightPoint;

	public ControlPointManager(Dashboard dashboard) {
		Preconditions.checkNotNull(dashboard, "Dashboard must not be null");
		this.dashboard = dashboard;

		arrowCursor = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);

		dashboard.addSelectionChangedListener(this);
		dashboard.getControl().addMouseMoveListener(this);
		dashboard.getControl().addMouseListener(this);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();

		disposeControlPoints();

		if (!selection.isEmpty()) {
			Optional<DashboardPartPlacement> optSelected = getSelectedDashboardPartPlacement(selection);
			if (optSelected.isPresent()) {
				DashboardPartPlacement dashboardPartPlacement = optSelected.get();

				createControlPoints(dashboard, dashboardPartPlacement);
			}
		}
	}

	public void render(GC gc) {
		renderControlPoint(gc, topLeftPoint);
		renderControlPoint(gc, topRightPoint);
		renderControlPoint(gc, bottomLeftPoint);
		renderControlPoint(gc, bottomRightPoint);
	}

	public void dispose() {
		disposeControlPoints();
		disposeCursor(arrowCursor);

		dashboard.getControl().removeMouseListener(this);
		dashboard.getControl().removeMouseMoveListener(this);
		dashboard.removeSelectionChangedListener(this);
	}

	// MouseListener
	@Override
	public void mouseDoubleClick(MouseEvent e) {

	}

	// MouseListener
	@Override
	public void mouseDown(MouseEvent e) {

	}

	// MouseListener
	@Override
	public void mouseUp(MouseEvent e) {

	}

	// MouseMoveListener
	@Override
	public void mouseMove(MouseEvent e) {
		if (!getControlPoint(e.x, e.y).isPresent()) {
			dashboard.getControl().setCursor(arrowCursor);
		}
	}

	public Optional<ControlPoint> getControlPoint(int x, int y) {
		if (topLeftPoint != null && topLeftPoint.isInside(x, y)) {
			return Optional.of(topLeftPoint);
		}
		if (topRightPoint != null && topRightPoint.isInside(x, y)) {
			return Optional.of(topRightPoint);
		}
		if (bottomLeftPoint != null && bottomLeftPoint.isInside(x, y)) {
			return Optional.of(bottomLeftPoint);
		}
		if (bottomRightPoint != null && bottomRightPoint.isInside(x, y)) {
			return Optional.of(bottomRightPoint);
		}
		
		return Optional.absent();
	}

	private void createControlPoints(Dashboard dashboard, DashboardPartPlacement dashboardPartPlacement) {
		topLeftPoint = new ControlPoint(dashboard, dashboardPartPlacement, new TopLeftControlPoint());
		bottomLeftPoint = new ControlPoint(dashboard, dashboardPartPlacement, new BottomLeftControlPoint());
		bottomRightPoint = new ControlPoint(dashboard, dashboardPartPlacement, new BottomRightControlPoint());
		topRightPoint = new ControlPoint(dashboard, dashboardPartPlacement, new TopRightControlPoint());
	}

	private void disposeControlPoints() {
		disposeControlPoint(topLeftPoint);
		disposeControlPoint(topRightPoint);
		disposeControlPoint(bottomLeftPoint);
		disposeControlPoint(bottomRightPoint);
	}
	
	private static void disposeControlPoint( ControlPoint point ) {
		if( point != null ) {
			point.dispose();
		}
	}

	private static void renderControlPoint(GC gc, ControlPoint point) {
		if (point != null) {
			point.render(gc);
		}
	}

	private static Optional<DashboardPartPlacement> getSelectedDashboardPartPlacement(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();
			if (selectedObject instanceof DashboardPartPlacement) {
				DashboardPartPlacement placement = (DashboardPartPlacement) selectedObject;
				return Optional.of(placement);
			}
		}
		return Optional.absent();
	}

	private static void disposeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isDisposed()) {
			cursor.dispose();
		}
	}
}
