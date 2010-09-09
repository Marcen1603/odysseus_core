package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;


public interface ISymbolElement<C> {
	
	public void draw( Vector position, int width, int height, float zoomFactor );
	public void update();
	
	public void setEnabled( boolean isEnabled );
	public boolean isEnabled();
	
	public void setNodeView( INodeView<C> view );
	public INodeView<C> getNodeView();

}
