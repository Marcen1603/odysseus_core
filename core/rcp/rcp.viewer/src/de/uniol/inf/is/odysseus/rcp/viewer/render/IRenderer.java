package de.uniol.inf.is.odysseus.rcp.viewer.render;

import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.render.impl.RenderRange;

public interface IRenderer<C> {

	public void update( IGraphView<C> graph );
	public void render( IGraphView<C> graph, float zoomFactor, RenderRange renderRange, Vector shift );

}
