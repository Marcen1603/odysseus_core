package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.util.Collection;


public class GridDashboardLayouter implements IDashboardLayouter {

	private static final int SELECTION_BORDER_MARGIN_PIXELS = 3;
	
	@Override
	public void layout(Collection<DashboardPartPlacement> dashboardPartPlacements, int width, int height) {

		final int partCount = dashboardPartPlacements.size();
		final int columnCount = ((int) Math.sqrt(partCount)) + 1;
		final int rowCount = (partCount / columnCount) + 1;
		
		final int cellWidth = width / columnCount;
		final int cellHeight = height / rowCount;
		
		int currentX = 0;
		int currentY = 0;
		for( DashboardPartPlacement dashboardPartPlace : dashboardPartPlacements ) {
			dashboardPartPlace.setWidth(cellWidth - (SELECTION_BORDER_MARGIN_PIXELS * 2));
			dashboardPartPlace.setHeight(cellHeight - (SELECTION_BORDER_MARGIN_PIXELS * 2));
			
			dashboardPartPlace.setX((currentX * cellWidth) + SELECTION_BORDER_MARGIN_PIXELS);
			dashboardPartPlace.setY((currentY * cellHeight) + SELECTION_BORDER_MARGIN_PIXELS);
			
			currentX++;
			if( currentX == columnCount ) {
				currentX = 0;
				currentY++;
			}
		}
	}

}
