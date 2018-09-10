package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.util.Collection;


public interface IDashboardLayouter {

	public void layout( Collection<DashboardPartPlacement> dashboardPartPlacements, int width, int height);
}
