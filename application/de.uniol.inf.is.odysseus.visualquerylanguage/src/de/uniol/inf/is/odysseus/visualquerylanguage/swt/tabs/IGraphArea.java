package de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs;

import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IModelController;

public interface IGraphArea<T> {
	
	public IModelController<T> getController();
	public IGraphView<T> getGraphView();

}
