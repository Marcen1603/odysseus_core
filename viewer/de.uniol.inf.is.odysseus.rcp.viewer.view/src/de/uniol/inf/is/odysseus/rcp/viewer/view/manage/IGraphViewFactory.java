package de.uniol.inf.is.odysseus.rcp.viewer.view.manage;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElementFactory;

public interface IGraphViewFactory<C> {

	public IGraphView<C> createGraphView( IGraphModel<C> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<C> symbolFactory );
	public void updateGraphDisplay( IGraphModel<C> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<C> symbolFactory, IGraphView<C> graphDisplay );
	
}
