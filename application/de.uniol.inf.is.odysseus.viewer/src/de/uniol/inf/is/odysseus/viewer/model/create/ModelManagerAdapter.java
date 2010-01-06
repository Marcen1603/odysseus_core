package de.uniol.inf.is.odysseus.viewer.model.create;

import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;


public class ModelManagerAdapter<C> implements IModelManagerListener<C> {

	@Override
	public void modelAdded(ModelManager<C> sender, IGraphModel<C> provider) {}

	@Override
	public void modelRemoved(ModelManager<C> sender, IGraphModel<C> provider) {}


}
