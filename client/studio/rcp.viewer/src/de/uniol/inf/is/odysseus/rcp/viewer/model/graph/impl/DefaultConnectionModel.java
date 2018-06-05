/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModelChangeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

/**
 * Standardimplementierung der IConnectionModel-Schnittstelle. Sie stellt eine Verbindung
 * zwischen zwei Knoten innerhalb des GraphModels dar. Eine Verbindung besitzt genau einen
 * Anfangs- sowie Endknoten, welcher über den Konstruktor definiert, aber nachträglich
 * nicht verändert werden können. Eine Verbindung ist gleich einer anderen Verbindung, 
 * wenn sie die gleichen Anfangs- und Endknoten besitzen.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public class DefaultConnectionModel<C> implements IConnectionModel<C> {

	private final Collection<IConnectionModelChangeListener<C>> listeners = new ArrayList<IConnectionModelChangeListener<C>>();
	private final INodeModel<C> start;
	private final INodeModel<C> end;

	/**
	 * Konstruktor, welche aus den angegebenen Anfangsknoten und Endknoten eine
	 * DefaultConnectionModel-Instanz erzeugt. Ist eines der beiden Parameter null,
	 * so wird eine IllegalArgumentException geworfen.
	 * 
	 * @param startNode Anfangsknoten der neuen Verbindung
	 * @param endNode Endknoten der neuen Verbindung
	 */
	public DefaultConnectionModel(INodeModel<C> startNode, INodeModel<C> endNode ) {
		if( startNode == null )
			throw new IllegalArgumentException("startNode is null!");
		if( endNode == null )
			throw new IllegalArgumentException("endNode is null!");
		
		this.start = startNode;
		this.end = endNode;
	}
	
	@Override
	public INodeModel<C> getStartNode() {
		return start;
	}
	
	@Override
	public INodeModel<C> getEndNode() {
		return end;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "{" );
		sb.append( getStartNode() );
		sb.append( " --> " );
		sb.append( getEndNode() );
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void addConnectionModelChangeListener( IConnectionModelChangeListener<C> listener ) {
		if(listener == null )
			return;
		
		synchronized( listeners ) {
			if( !listeners.contains( listener ))
				listeners.add( listener );
		}
	}

	@Override
	public void notifyConnectionModelChangeListener() {
		synchronized( listeners ) {
			for( IConnectionModelChangeListener<C> l : listeners ) {
				if( l != null )
					l.connectionModelChanged( this );
			}
		}
	}

	@Override
	public void removeConnectionModelChangeListener( IConnectionModelChangeListener<C> listener ) {
		synchronized(listeners) {
			listeners.remove( listener );
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals( Object obj ) {
		if( !(obj instanceof DefaultConnectionModel)) {
			return false;
		}
		if( obj == this ) {
			return true;
		}
		DefaultConnectionModel<C> c = (DefaultConnectionModel<C>)obj;
		return c.getStartNode().equals(getStartNode()) && c.getEndNode().equals(getEndNode());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}


}
