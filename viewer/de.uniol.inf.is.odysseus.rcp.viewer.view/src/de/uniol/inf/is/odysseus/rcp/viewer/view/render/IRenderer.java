package de.uniol.inf.is.odysseus.rcp.viewer.view.render;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl.RenderRange;

public interface IRenderer<C> {

	public void update( IGraphView<C> graph );
	public void render( IGraphView<C> graph, float zoomFactor, RenderRange renderRange, Vector shift );

}
