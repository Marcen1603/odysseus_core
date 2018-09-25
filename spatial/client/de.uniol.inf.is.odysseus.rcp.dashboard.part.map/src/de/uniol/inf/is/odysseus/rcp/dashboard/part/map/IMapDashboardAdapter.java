package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

import org.eclipse.core.runtime.IAdaptable;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;
//import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer.TimeSliderComposite;

public interface IMapDashboardAdapter extends IAdaptable {

	public ScreenManager getScreenManager();

	abstract void update();
	public boolean isRectangleZoom();
	
	public MapEditorModel getMapEditorModel();

//	public TimeSliderComposite getTimeSliderComposite();

}
