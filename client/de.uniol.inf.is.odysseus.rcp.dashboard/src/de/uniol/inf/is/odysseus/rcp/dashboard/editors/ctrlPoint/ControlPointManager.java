package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public class ControlPointManager implements MouseMoveListener, ISelectionListener {

	private final Dashboard dashboard;
	private final Cursor arrowCursor;
	private final List<ControlPoint> points = Lists.newArrayList();

	public ControlPointManager(Dashboard dashboard) {
		Objects.requireNonNull(dashboard, "Dashboard must not be null");
		this.dashboard = dashboard;

		arrowCursor = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
		dashboard.getControl().addMouseMoveListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		disposeControlPoints();

		if (!selection.isEmpty() && !dashboard.getControl().isDisposed()) {
			Optional<DashboardPartPlacement> optSelected = getSelectedDashboardPartPlacement(selection);
			if (optSelected.isPresent()) {
				DashboardPartPlacement dashboardPartPlacement = optSelected.get();

				createControlPoints(dashboard, dashboardPartPlacement);
			}
		}
	}

	private void disposeControlPoints() {
		for( ControlPoint point : points ) {
			disposeControlPoint(point);
		}
		points.clear();
	}
	
	private static void disposeControlPoint( ControlPoint point ) {
		if( point != null ) {
			point.dispose();
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
	
	private void createControlPoints(Dashboard dashboard, DashboardPartPlacement dashboardPartPlacement) {
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new TopLeftControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new BottomLeftControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new BottomRightControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new TopRightControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new TopControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new BottomControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new LeftControlPoint()));
		points.add(new ControlPoint(dashboard, dashboardPartPlacement, new RightControlPoint()));
	}

	public void render(GC gc) {
		for( ControlPoint point : points ) {
			renderControlPoint(gc, point);
		}
	}
	
	private static void renderControlPoint(GC gc, ControlPoint point) {
		if (point != null) {
			point.render(gc);
		}
	}

	public void dispose() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);

		disposeControlPoints();
		disposeCursor(arrowCursor);

		dashboard.getControl().removeMouseMoveListener(this);
	}

	private static void disposeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isDisposed()) {
			cursor.dispose();
		}
	}

	// MouseMoveListener
	@Override
	public void mouseMove(MouseEvent e) {
		if (!getControlPoint(e.x, e.y).isPresent()) {
			dashboard.getControl().setCursor(arrowCursor);
		}
	}

	public Optional<ControlPoint> getControlPoint(int x, int y) {
		for( ControlPoint point : points ) {
			if( point.isInside(x, y)) {
				return Optional.of(point);
			}
		}
				
		return Optional.absent();
	}
}
