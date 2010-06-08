package de.uniol.inf.is.odysseus.rcp.viewer.view.editor;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;

public interface IGraphViewEditor {

	public IGraphModel<IPhysicalOperator> getGraphModel();
	public IGraphView<IPhysicalOperator> getGraphView();
}
