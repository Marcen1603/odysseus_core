package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;

public interface IGraphViewEditor {

	public IOdysseusGraphModel getGraphModel();
	public IOdysseusGraphView getGraphView();
}
