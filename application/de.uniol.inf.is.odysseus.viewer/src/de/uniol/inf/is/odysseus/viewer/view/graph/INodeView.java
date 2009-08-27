package de.uniol.inf.is.odysseus.viewer.view.graph;

import java.util.Collection;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.view.symbol.SymbolElementContainer;

public interface INodeView<C> {
	
	public INodeModel<C> getModelNode();
	
	public Vector getPosition();
	public void setPosition( Vector v );
	
	public int getWidth();
	public int getHeight();
	public void setWidth( int w );
	public void setHeight( int h );
	
	public SymbolElementContainer<C> getSymbolContainer();
	
	public boolean isVisible();
	public boolean isEnabled();
	public void setVisible( boolean visible );
	public void setEnabled( boolean enabled );
	
	public Collection<IConnectionView<C>> getAllConnections();
	public Collection<IConnectionView<C>> getConnectionsAsStart();	
	public Collection<IConnectionView<C>> getConnectionsAsEnd();
	
}