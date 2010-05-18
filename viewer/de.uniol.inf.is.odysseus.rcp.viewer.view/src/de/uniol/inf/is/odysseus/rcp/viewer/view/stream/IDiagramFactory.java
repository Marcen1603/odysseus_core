package de.uniol.inf.is.odysseus.rcp.viewer.view.stream;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

public interface IDiagramFactory {

	public <In, Out> DiagramConverterPair<In,Out> create( INodeModel<?> node, DiagramInfo info );

}
