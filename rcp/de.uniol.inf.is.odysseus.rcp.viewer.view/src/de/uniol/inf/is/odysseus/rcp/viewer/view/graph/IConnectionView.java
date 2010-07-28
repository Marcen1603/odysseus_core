package de.uniol.inf.is.odysseus.rcp.viewer.view.graph;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ConnectionSymbolElementContainer;

public interface IConnectionView<C> {
	
	public IConnectionModel<C> getModelConnection();	
	public INodeView<C> getViewedStartNode();	
	public INodeView<C> getViewedEndNode();
	
	public ConnectionSymbolElementContainer<C> getSymbolContainer();
	
}