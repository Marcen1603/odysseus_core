package de.uniol.inf.is.odysseus.rcp.viewer.view.editor;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusGraphView;

public interface IGraphViewEditor {

	public IOdysseusGraphModel getGraphModel();
	public IOdysseusGraphView getGraphView();
}
