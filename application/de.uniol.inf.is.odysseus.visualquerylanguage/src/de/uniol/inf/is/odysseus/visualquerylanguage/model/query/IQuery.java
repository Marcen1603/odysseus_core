package de.uniol.inf.is.odysseus.visualquerylanguage.model.query;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.vqlinterfaces.ctrl.IController;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.IGraphModel;

public interface IQuery<C extends INodeContent> {
	
	public int getId();
	public String getName();
	public void setName(String name);
	
	public IController<C> getController();
	public void setGraphModel(IGraphModel<C> graphModel);
	
	public boolean isRunning();
	public void setRunning(boolean running);

}
