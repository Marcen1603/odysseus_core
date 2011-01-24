package de.uniol.inf.is.odysseus.rcp.viewer.symbol;

import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;


public interface ISymbolElement<C> {
	
	public void draw( Vector position, int width, int height, float zoomFactor );
	public void update();
	
	public void setEnabled( boolean isEnabled );
	public boolean isEnabled();
	
	public void setNodeView( INodeView<C> view );
	public INodeView<C> getNodeView();

}
