package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;


public interface IConnectionSymbolElement<C> {

	public void draw(Vector startPosition, Vector endPosition, float zoomFactor );
	public void update();
	
	public void setEnabled( boolean isEnabled );
	public boolean isEnabled();
	
	public void setConnectionView( IConnectionView<C> view );
	public IConnectionView<C> getConnectionView();
	
}
