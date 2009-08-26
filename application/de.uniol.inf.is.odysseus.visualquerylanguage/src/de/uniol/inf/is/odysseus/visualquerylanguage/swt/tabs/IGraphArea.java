package de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs;

import de.uniol.inf.is.odysseus.vqlinterfaces.ctrl.IController;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.IGraphView;

public interface IGraphArea<T> {
	
	public IController<T> getController();
	public IGraphView<T> getGraphView();

}
