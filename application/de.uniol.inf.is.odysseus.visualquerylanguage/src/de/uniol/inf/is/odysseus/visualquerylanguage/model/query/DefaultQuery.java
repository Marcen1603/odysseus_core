package de.uniol.inf.is.odysseus.visualquerylanguage.model.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.viewer.model.graph.DefaultGraphModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.DefaultModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
public class DefaultQuery implements IQuery<INodeContent>{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultQuery.class);
	
	private int id;
	private final IModelController<INodeContent> controller;
	private String name = "";
	private IGraphModel<INodeContent> graphModel;
	private boolean running = false;
	
	public DefaultQuery(String name) {
		this.graphModel = new DefaultGraphModel<INodeContent>();
		this.controller = new DefaultModelController<INodeContent>(graphModel);
		this.name = name;
	}

	@Override
	public IModelController<INodeContent> getController() {
		return controller;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setGraphModel(IGraphModel<INodeContent> graphModel) {
		if(graphModel != null) {
			this.graphModel = graphModel;
		}else {
			log.error("graphModel is null");
		}
	}
	
	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void addNode(AbstractLogicalOperator logOp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRootNode(AbstractLogicalOperator logOp) {
		// TODO Auto-generated method stub
		
	}
}
