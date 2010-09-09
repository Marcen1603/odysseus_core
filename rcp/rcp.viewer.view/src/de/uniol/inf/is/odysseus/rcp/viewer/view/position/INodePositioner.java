package de.uniol.inf.is.odysseus.rcp.viewer.view.position;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;



public interface INodePositioner<C> {

	public void positionize( IGraphView<C> graph, int width, int height );
	
}
