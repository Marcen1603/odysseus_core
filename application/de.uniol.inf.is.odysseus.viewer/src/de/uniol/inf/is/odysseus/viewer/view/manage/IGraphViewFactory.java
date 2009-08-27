package de.uniol.inf.is.odysseus.viewer.view.manage;

import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolElementFactory;

public interface IGraphViewFactory<C> {

	public IGraphView<C> createGraphDisplay( IGraphModel<C> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<C> symbolFactory );
	public void updateGraphDisplay( IGraphModel<C> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<C> symbolFactory, IGraphView<C> graphDisplay );
	
}
