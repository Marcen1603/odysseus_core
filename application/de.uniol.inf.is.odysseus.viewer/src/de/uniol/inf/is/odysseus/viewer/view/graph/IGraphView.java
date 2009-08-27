package de.uniol.inf.is.odysseus.viewer.view.graph;

import java.util.Collection;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;

public interface IGraphView<C>{
	
	public Collection<INodeView<C>> getViewedNodes();	
	public Collection<IConnectionView<C>> getViewedConnections();
	
	public void insertViewedNode(INodeView<C> nodeView );
	public void insertViewedConnection( IConnectionView<C> connView );
	
	public void removeViewedNode( INodeView<C> nodeView );
	public void removeViewedConnection( IConnectionView<C> connView );
	
	public IGraphModel<C> getModelGraph();
}