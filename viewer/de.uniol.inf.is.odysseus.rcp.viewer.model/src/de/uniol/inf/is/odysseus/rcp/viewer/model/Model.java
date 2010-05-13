package de.uniol.inf.is.odysseus.rcp.viewer.model;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.ctrl.IController;
import de.uniol.inf.is.odysseus.rcp.viewer.model.ctrl.impl.DefaultController;

public class Model {

	private static Model instance = null;
	private final IController<IPhysicalOperator> CONTROLLER = new DefaultController<IPhysicalOperator>();
	
	private Model() {
		
	}
	
	public static Model getInstance() {
		if( instance == null )
			instance = new Model();
		return instance;
	}
	
	public IController<IPhysicalOperator> getController() {
		return CONTROLLER;
	}

	public IModelManager<IPhysicalOperator> getModelManager() {
		return CONTROLLER.getModelManager();
	}
}
