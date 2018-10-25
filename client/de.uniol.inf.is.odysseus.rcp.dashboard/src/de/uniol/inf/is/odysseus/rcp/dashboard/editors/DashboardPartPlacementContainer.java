package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class DashboardPartPlacementContainer {
	
	private final List<DashboardPartPlacement> dashboardParts = Lists.newArrayList();
	private final Map<Control, DashboardPartPlacement> controlsMap = Maps.newHashMap();
	private final Map<DashboardPartPlacement, DashboardPartControl> containers = Maps.newHashMap();
	
	public boolean contains( DashboardPartPlacement placement ) {
		return dashboardParts.contains(placement);
	}

	public void add( DashboardPartPlacement placementToAdd ) {
		// Preconditions.checkArgument(!contains(placementToAdd), "Placement already added");
		
		dashboardParts.add(placementToAdd);
	}
	
	public void addContainer( DashboardPartPlacement placement, DashboardPartControl container ) {
		containers.put(placement, container);
		addControls(placement, container.getComposite() );
	}
	
	private void addControls(DashboardPartPlacement placement, Control base ) {
		controlsMap.put(base, placement);

		if (base instanceof Composite) {
			final Composite comp = (Composite) base;
			for (final Control compControl : comp.getChildren()) {
				addControls(placement, compControl );
			}
		}
	}
	
	public void remove( DashboardPartPlacement placement ) {
		Objects.requireNonNull(placement, "Dashboardpart to remove (as placement) must not be null!");

		removeControls(placement, containers.get(placement).getComposite());
		
		dashboardParts.remove(placement);
		containers.remove(placement);
	}
	
	private void removeControls(DashboardPartPlacement placement, Control base) {
		controlsMap.remove(base);

		if (base instanceof Composite) {
			final Composite comp = (Composite) base;
			for (final Control compControl : comp.getChildren()) {
				removeControls(placement, compControl );
			}
		}
	}

	public Optional<DashboardPartPlacement> getByControl( Widget control ) {
		return Optional.fromNullable(controlsMap.get(control));
	}
	
	public Optional<DashboardPartControl> getComposite( DashboardPartPlacement placement ) {
		return Optional.fromNullable(containers.get(placement));
	}
	
	public ImmutableList<DashboardPartPlacement> getDashboardPartPlacements() {
		return ImmutableList.copyOf(dashboardParts);
	}
	
	public DashboardPartPlacement getNextPartAfter( DashboardPartPlacement placement ) {
		Objects.requireNonNull(placement, "Placement must not be null!");
		
		final int pos = dashboardParts.indexOf(placement);
		final int newPos = (pos + 1) % dashboardParts.size();
		return dashboardParts.get(newPos);
	}
}
