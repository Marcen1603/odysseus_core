package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import de.uniol.inf.is.odysseus.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;

public class DefaultModelController<C> implements IModelController<C>{
	
private IGraphModel<C> graph;
	
	public DefaultModelController (IGraphModel<C> graph) {
		if(graph == null) {
			throw new IllegalArgumentException("graph is null");
		}
		this.graph = graph;
	}
	
	@Override
	public void addNode(INodeModel<C> node) {
		graph.addNode(node);
	}

	@Override
	public IGraphModel<C> getModel() {
		return graph;
	}

	@Override
	public IModelProvider<C> getModelProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setModelProvider(IModelProvider<C> modelProvider) {
		// TODO Auto-generated method stub
		
	}
	
}
