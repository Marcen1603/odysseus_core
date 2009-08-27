package de.uniol.inf.is.odysseus.viewer.view.position;

import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;



public interface INodePositioner<C> {

	public void positionize( IGraphView<C> graph, int width, int height );
	
}
