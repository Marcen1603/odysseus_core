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
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModelChangeListener;
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
