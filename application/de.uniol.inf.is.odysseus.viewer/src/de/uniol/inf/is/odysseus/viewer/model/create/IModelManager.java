package de.uniol.inf.is.odysseus.viewer.model.create;

import java.util.List;

import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;

public interface IModelManager<C> {

	public void addModel( IGraphModel<C> model );
	public void removeModel( IGraphModel<C> mode );
	public void removeAllModels();
	public List<IGraphModel<C>> getModels();
	
	public void addListener( IModelManagerListener<C> listener );
	public void removeListener( IModelManagerListener<C> listener );
	
}
