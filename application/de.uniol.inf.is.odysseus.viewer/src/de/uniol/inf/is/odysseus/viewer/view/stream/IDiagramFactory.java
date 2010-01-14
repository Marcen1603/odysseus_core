package de.uniol.inf.is.odysseus.viewer.view.stream;

import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;

public interface IDiagramFactory {

	public <In, Out> DiagramConverterPair<In,Out> create( INodeModel<?> node, DiagramInfo info );

}
