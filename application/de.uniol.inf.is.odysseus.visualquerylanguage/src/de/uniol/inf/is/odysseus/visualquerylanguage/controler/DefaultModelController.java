package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import de.uniol.inf.is.odysseus.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.viewer.model.create.ModelManager;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;

public class DefaultModelController<C> implements IModelController<C>{
	
	private IModelManager<C> modelManager = new ModelManager<C>(); 
	
	public DefaultModelController (IGraphModel<C> graph) {
		if(graph == null) {
			throw new IllegalArgumentException("graph is null");
		}
		modelManager.addModel(graph);
	}
	
	@Override
	public void addNode(INodeModel<C> node) {
		modelManager.getModels().get(0).addNode(node);
	}


	@Override
	public IModelManager<C> getModelManager() {
		return modelManager;
	}
	
}
