package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import de.uniol.inf.is.odysseus.viewer.ctrl.IController;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;

public interface IModelController<C> extends IController<C>{

	public void addNode(INodeModel<C> node);
	
}
