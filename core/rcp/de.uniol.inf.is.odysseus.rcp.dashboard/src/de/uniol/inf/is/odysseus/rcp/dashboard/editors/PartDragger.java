package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import com.google.common.base.Preconditions;


final class PartDragger {

	private DashboardPartPlacement place;
	private int currentDragX;
	private int currentDragY;
	
	public void beginDrag( DashboardPartPlacement place, int mouseStartX, int mouseStartY ) {
		Preconditions.checkNotNull(place, "Placement for dragging must not be null!");
		
		this.place = place;
		this.currentDragX = mouseStartX;
		this.currentDragY = mouseStartY;
	}
	
	public boolean isDragging() {
		return this.place != null;
	}
	
	public void drag( int mouseX, int mouseY ) {
		Preconditions.checkState( place != null, "Placement not set for dragging!");
		
		final int xDistance = mouseX - currentDragX;
		final int yDistance = mouseY - currentDragY;
		
		place.setX( place.getX() + xDistance);
		place.setY( place.getY() + yDistance);
	}
	
	public void endDrag() {
		place = null;
	}
}
