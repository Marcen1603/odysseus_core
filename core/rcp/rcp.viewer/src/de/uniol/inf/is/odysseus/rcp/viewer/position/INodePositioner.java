package de.uniol.inf.is.odysseus.rcp.viewer.position;

import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;



public interface INodePositioner<C> {

	public void positionize( IGraphView<C> graph, int width, int height );
	
}
