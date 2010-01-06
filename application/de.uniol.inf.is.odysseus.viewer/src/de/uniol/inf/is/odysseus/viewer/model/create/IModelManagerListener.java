package de.uniol.inf.is.odysseus.viewer.model.create;

import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;


public interface IModelManagerListener<C> {

	public void modelAdded( ModelManager<C> sender, IGraphModel<C> model );
	public void modelRemoved( ModelManager<C> sender, IGraphModel<C> model );
}
