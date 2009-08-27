package de.uniol.inf.is.odysseus.viewer.view.manage;

import de.uniol.inf.is.odysseus.viewer.ctrl.IController;

public interface IView<C> {

	public IController<C> getController();
	public void createGraphDisplay( IGraphViewFactory<C> graphFactory );
	public void updateGraphDisplay( IGraphViewFactory<C> graphFactory );
	
}
