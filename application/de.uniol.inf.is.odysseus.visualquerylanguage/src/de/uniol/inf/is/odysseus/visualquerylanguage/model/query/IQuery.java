package de.uniol.inf.is.odysseus.visualquerylanguage.model.query;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;

public interface IQuery<C extends INodeContent> {
	
	public int getId();
	public void setId(int id);
	public String getName();
	public void setName(String name);
	
	public IModelController<C> getController();
	public void setGraphModel(IGraphModel<C> graphModel);
	
	public boolean isRunning();
	public void setRunning(boolean running);
	
	public void addRootNode(AbstractLogicalOperator logOp);
	public void addNode(AbstractLogicalOperator logOp);

}
