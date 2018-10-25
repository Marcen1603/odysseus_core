package de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.GC;

import com.google.common.base.Optional;
import java.util.Objects;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.IDashboardListener;

public final class ControlPoint implements MouseMoveListener, MouseListener, IDashboardListener {

	public static final int CONTROL_POINT_SIZE_PIXELS = 6;

	private final DashboardPartPlacement place;
	private final Dashboard dashboard;
	private final IControlPointBehaviour behaviour;

	private int x;
	private int y;

	public ControlPoint(Dashboard dashboard, DashboardPartPlacement place, IControlPointBehaviour behaviour) {
		Objects.requireNonNull(place, "DashboardPartPlacement must not be null!");
		Objects.requireNonNull(dashboard, "Dashboard must not be null!");
		Objects.requireNonNull(behaviour, "Control point behaviour must not be null!");

		this.dashboard = dashboard;
		this.place = place;
		behaviour.setControlPoint(this);
		this.x = behaviour.getPositionX(place);
		this.y = behaviour.getPositionY(place);
		this.behaviour = behaviour;

		dashboard.getControl().addMouseMoveListener(this);
		dashboard.getControl().addMouseListener(this);
		dashboard.addListener(this);
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public DashboardPartPlacement getDashboardPartPlacement() {
		return place;
	}

	public void render(GC gc) {
		gc.drawRectangle(x, y, CONTROL_POINT_SIZE_PIXELS, CONTROL_POINT_SIZE_PIXELS);
	}

	public void dispose() {
		dashboard.getControl().removeMouseMoveListener(this);
		dashboard.getControl().removeMouseListener(this);
		dashboard.removeListener(this);

		behaviour.dispose();
	}

	public boolean isInside(int x, int y) {
		return x >= this.x && x <= this.x + CONTROL_POINT_SIZE_PIXELS && y >= this.y && y <= this.y + CONTROL_POINT_SIZE_PIXELS;
	}
	
	// DashboardListener
	@Override
	public void dashboardChanged(Dashboard sender) {
		Optional<DashboardPartPlacement> optPlace = sender.getSelectedDashboardPartPlacement();
		if( optPlace.isPresent() && optPlace.get().equals(place)) {
			this.x = behaviour.getPositionX(optPlace.get());
			this.y = behaviour.getPositionY(optPlace.get());
		}
	}

	// DashboardListener
	@Override
	public void dashboardPartAdded(Dashboard sender, IDashboardPart addedPart) {
		// do nothing
	}

	// DashboardListener
	@Override
	public void dashboardPartRemoved(Dashboard sender, IDashboardPart removedPart) {
		// do nothing
	}

	// MouseMoveListener
	@Override
	public void mouseMove(MouseEvent e) {
		if (isInside(e.x, e.y)) {
			dashboard.getControl().setCursor(behaviour.getCursor());
		}

		behaviour.mouseMove(e);
	}

	// MouseListener
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		if (isInside(e.x, e.y)) {
			behaviour.mouseDoubleClick(e);
		}
	}

	// MouseListener
	@Override
	public void mouseDown(MouseEvent e) {
		if (isInside(e.x, e.y)) {
			behaviour.mouseDown(e);
		}
	}

	// MouseListener
	@Override
	public void mouseUp(MouseEvent e) {
		behaviour.mouseUp(e);
	}
}
