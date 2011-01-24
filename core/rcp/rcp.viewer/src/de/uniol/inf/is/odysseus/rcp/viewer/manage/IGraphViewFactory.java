package de.uniol.inf.is.odysseus.rcp.viewer.manage;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;

public interface IGraphViewFactory<C> {

	public IGraphView<C> createGraphView( IGraphModel<C> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<C> symbolFactory );
	public void updateGraphDisplay( IGraphModel<C> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<C> symbolFactory, IGraphView<C> graphDisplay );
	
}
