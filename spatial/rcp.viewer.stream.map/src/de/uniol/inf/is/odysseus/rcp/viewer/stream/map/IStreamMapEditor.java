package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.core.runtime.IAdaptable;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;

public interface IStreamMapEditor extends IAdaptable {

	public ScreenManager getScreenManager();

//	public void addVectorLayer(IStreamConnection<Object> connection, SDFAttribute attribute);
//	public void removeVectorLayer(VectorLayer vectorLayer);

	public int getMaxTuplesCount();
	abstract void update();
	public boolean isRectangleZoom();
	
	public MapEditorModel getMapEditorModel();

}
