package de.uniol.inf.is.odysseus.rcp.viewer.view.stream;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.stream.impl.DiagramConverterPair;
import de.uniol.inf.is.odysseus.rcp.viewer.view.stream.impl.DiagramInfo;

public interface IDiagramFactory {

	public <In, Out> DiagramConverterPair<In,Out> create( INodeModel<?> node, DiagramInfo info );

}
