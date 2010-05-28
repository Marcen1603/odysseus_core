package de.uniol.inf.is.odysseus.rcp.viewer.model.create;

import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;

public interface IModelManager<C> {

	public void addModel( IGraphModel<C> model );
	public void removeModel( IGraphModel<C> mode );
	public void removeAllModels();
	public List<IGraphModel<C>> getModels();
	
	public void setActiveModel( IGraphModel<C> model );
	public IGraphModel<C> getActiveModel();
		
	public void addListener( IModelManagerListener<C> listener );
	public void removeListener( IModelManagerListener<C> listener );
	
}
