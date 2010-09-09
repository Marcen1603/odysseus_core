package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModelChangeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

/**
 * Standardimplementierung der IGraphModel-Schnittstelle. Sie stellt einen Graphen
 * als Ganzheit im Model dar (GraphModel). Sie besitzt die Kenntnisse über alle Knoten
 * und Verbindungen innerhalb des GraphModels. Über sie sollten Manipulationen 
 * des Graphen durchgeführt werden, um die Konsitenz zwischen Graph, Knoten und Verbindung
 * zu gewährleisten.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public class DefaultGraphModel<C> implements IGraphModel<C> {

	private final Collection<IGraphModelChangeListener<C>> listeners = new ArrayList<IGraphModelChangeListener<C>>();
	private final Collection<INodeModel<C>> nodes = new ArrayList<INodeModel<C>>();
	private final Collection<IConnectionModel<C>> connections = new ArrayList<IConnectionModel<C>>();
	private String name = "";
	
	@Override
	public Collection<IConnectionModel<C>> getConnections() {
		return connections;
	}


	@Override
	public Collection<INodeModel<C>> getNodes() {
		return nodes;
	}


	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public void addGraphModelChangeListener( IGraphModelChangeListener<C> listener ) {
		if( listener == null )
			return;
		
		synchronized(listeners) {
			if( !listeners.contains( listener ))
				listeners.add( listener );
		}
	}

	@Override
	public void notifyGraphModelChangeListener() {
		synchronized( listeners ) {
			for( IGraphModelChangeListener<C> l : listeners ) {
				if( l != null )
					l.graphModelChanged( this );
			}
		}
	}

	@Override
	public void removeGraphModelChangeListener( IGraphModelChangeListener<C> listener ) {
		synchronized(listeners) {
			listeners.remove( listener );
		}
	}


	@Override
	public void addConnection( IConnectionModel<C> connection ) {
		// TODO: Was ist, wenn die beteiligten Knoten im nicht im Graphen sind?
		if( !connections.contains( connection )) {
			connections.add( connection );
			addConnectionToNode(connection.getEndNode(), connection);
			addConnectionToNode(connection.getStartNode(), connection);
			notifyGraphModelChangeListener();
		}
	}
	
	@Override
	public void addNode( INodeModel< C > node ) {
		// TODO: Was ist, wenn node == null ist?
		if( !nodes.contains( node )) {
			nodes.add(node);
			notifyGraphModelChangeListener();
		}
	}
	
	@Override
	public void removeConnection( IConnectionModel<C> connection ) {
		if( connections.contains( connection )) {
			connections.remove( connection );
			removeConnectionFromNode(connection.getEndNode(), connection);
			removeConnectionFromNode(connection.getStartNode(), connection);
			notifyGraphModelChangeListener();
		}
	}
	
	@Override
	public void removeNode( INodeModel< C > node ) {
		nodes.remove( node );
		for( IConnectionModel<C> conn : node.getConnections().toArray(new IConnectionModel[0]) ) {
			removeConnection( conn );
			notifyGraphModelChangeListener();
		}
	}

	/**
	 * Fügt zu einem bestehenden Knoten eine neue Verbindung hinzu. 
	 * 
	 * @param node Knoten, welcher die neue Verbindung haben soll
	 * @param connection Neue Verbindung
	 */
	private void addConnectionToNode( INodeModel<C> node, IConnectionModel<C> connection ) {
		// Alle Verbindungen, welche diesen Knoten betreffen, holen
		Collection<IConnectionModel<C>> allConnections = node.getConnections();
		
		if( allConnections.contains( connection ))
			return;
		
		// Neue Verbindung einordnen. Je nachdem, ob der Knoten
		// ein Anfangs- oder ein Endknoten ist.
		allConnections.add( connection );
		if( connection.getEndNode() == node )
			node.getConnectionsAsEndNode().add( connection );
		else
			node.getConnectionsAsStartNode().add( connection );
	}
	
	/**
	 * Entfernt von einem bestehenden Knoten eine Verbindung
	 * @param node Knoten, von welcher die Verbindung entfernt werden soll
	 * @param connection Die zu entfernende Verbindung
	 */
	private void removeConnectionFromNode( INodeModel<C> node, IConnectionModel<C> connection ) {
		Collection<? extends IConnectionModel<C>> allConnections = node.getConnections();
		
		if( allConnections.contains( connection )) {
			allConnections.remove( connection );
			node.getConnectionsAsEndNode().remove( connection );
			node.getConnectionsAsStartNode().remove( connection );
		}
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public void setName(String nameOfGraph) {
		if( nameOfGraph == null ) return;
		if( !nameOfGraph.equals(name)) {
			name = nameOfGraph;
			notifyGraphModelChangeListener();
		}
	}

}
