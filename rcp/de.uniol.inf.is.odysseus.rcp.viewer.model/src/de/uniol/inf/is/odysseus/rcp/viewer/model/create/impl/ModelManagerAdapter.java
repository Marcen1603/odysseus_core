package de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManagerListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;

public class ModelManagerAdapter<C> implements IModelManagerListener<C> {

	@Override
	public void modelAdded(IModelManager<C> sender, IGraphModel<C> provider) {
	}

	@Override
	public void modelRemoved(IModelManager<C> sender, IGraphModel<C> provider) {
	}

	@Override
	public void activeModelChanged(IModelManager<C> sender,
			IGraphModel<C> activeModel) {

	}

}
