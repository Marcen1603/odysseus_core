package de.uniol.inf.is.odysseus.rcp.viewer.view.render;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.select.ISelector;

public interface IRenderManager<C> {

	public void resetPositions();

	public float getZoomFactor();
	public void setZoomFactor(float zoomFactor);
	public void resetZoom();
	public void zoom( float offset );
	public void zoom( int centerX, int centerY, float offset );

	public void setDisplayedGraph(IGraphView<C> graph);
	public IGraphView<C> getDisplayedGraph();

	public void resetGraphOffset();
	public Vector getGraphOffset();
	public void setGraphOffset(Vector offset);

	public void refreshView();
	public ISelector<INodeView<C>> getSelector();
	
	public int getRenderWidth();
	public int getRenderHeight();

}