package de.uniol.inf.is.odysseus.rcp.viewer.model.create;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;


public interface IModelManagerListener<C> {

	public void modelAdded( IModelManager<C> sender, IGraphModel<C> model );
	public void modelRemoved( IModelManager<C> sender, IGraphModel<C> model );
	
	public void activeModelChanged( IModelManager<C> sender, IGraphModel<C> activeModel );
}
