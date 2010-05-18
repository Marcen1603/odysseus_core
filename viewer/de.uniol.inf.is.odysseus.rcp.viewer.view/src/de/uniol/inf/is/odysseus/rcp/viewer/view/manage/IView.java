package de.uniol.inf.is.odysseus.rcp.viewer.view.manage;

import de.uniol.inf.is.odysseus.rcp.viewer.model.ctrl.IController;

public interface IView<C> {

	public IController<C> getController();
	
}
