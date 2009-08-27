package de.uniol.inf.is.odysseus.viewer.model.graph;

import java.util.ArrayList;
import java.util.Collection;
/**
 * 
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public class DefaultNodeModel<C> extends DefaultModelContainer<C> implements INodeModel<C> {

	private Collection<INodeModelChangeListener<C>> listeners = new ArrayList<INodeModelChangeListener<C>>();
	private final Collection<IConnectionModel<C>> allConnections = new ArrayList<IConnectionModel<C>>();
	private final Collection<IConnectionModel<C>> connectionsAsEnd = new ArrayList<IConnectionModel<C>>();
	private final Collection<IConnectionModel<C>> connectionsAsStart = new ArrayList<IConnectionModel<C>>();

	private String name;

	public DefaultNodeModel( C content ) {
		super( content );
		if( getContent() != null )
			name = getContent().getClass().getSimpleName();
		else
			name = "";
	}

	@Override
	public void notifyNodeModelChangeListener() {
		synchronized(listeners) {
			for( INodeModelChangeListener<C> l : listeners ) {
				if( l != null )
					l.nodeModelChanged( this );
			}
		}
	}
	
	@Override
	public void addNodeModelChangeListener( INodeModelChangeListener<C> listener ) {
		if( listener == null )
			return;
		if( listeners.contains( listener ))
			return;
		
		synchronized( listeners ) {
			listeners.add( listener );
		}
		
	}

	@Override
	public void removeNodeModelChangeListener( INodeModelChangeListener<C> listener ) {
		synchronized(listeners) {
			listeners.remove( listener );
		}
	}

	@Override
	public final Collection<IConnectionModel<C>> getConnections() {
		return allConnections;
	}
	
	@Override
	public final Collection<IConnectionModel<C>> getConnectionsAsEndNode() {
		return connectionsAsEnd;
	}

	@Override
	public final Collection<IConnectionModel<C>> getConnectionsAsStartNode() {
		return connectionsAsStart;
	}


	@Override
	public void setName( String name ) {
		if( !this.name.equals( name )) {
			this.name = name;
			notifyNodeModelChangeListener();
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
}
