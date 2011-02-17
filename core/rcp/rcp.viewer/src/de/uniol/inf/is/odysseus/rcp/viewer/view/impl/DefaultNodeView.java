/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.viewer.view.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementContainer;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class DefaultNodeView<C> implements INodeView<C> {
	
	private final Collection<IConnectionView<C>> allConnections = new ArrayList<IConnectionView<C>>();
	private final Collection<IConnectionView<C>> connectionsAsEnd = new ArrayList<IConnectionView<C>>();
	private final Collection<IConnectionView<C>> connectionsAsStart = new ArrayList<IConnectionView<C>>();
	
	private Vector position = new Vector(0,0);
	private int width = 25;
	private int height = 25;
	private boolean isVisible = true;
	private boolean isEnabled = true;
	
	private SymbolElementContainer<C> symbol = new SymbolElementContainer<C>();
	
	private INodeModel<C> nodeModel;
	
	public DefaultNodeView( INodeModel<C> data ) {
		this.nodeModel = data;
	}
	
	void addConnection( IConnectionView<C> conn ) {
		if( allConnections.contains( conn ))
			return;
		
		allConnections.add(conn);
		if( conn.getViewedEndNode().equals( this ) ) {
			connectionsAsEnd.add( conn );
		}
		if( this.equals(conn.getViewedStartNode())) {
			connectionsAsStart.add( conn );
		}
	}
	
	void removeConnection( IConnectionView<C> conn ) {
		allConnections.remove( conn );
		connectionsAsEnd.remove( conn );
		connectionsAsStart.remove( conn );
	}	
	
	@Override
	public final void setPosition( Vector v ) {
		position = v;
	}
		
	@Override
	public void setHeight( int h ) {
		height = Math.max(h, 0);
	}

	@Override
	public void setWidth( int w ) {
		width =  Math.max(w, 0);
	}
	
	@Override
	public void setVisible( boolean isVisible ) {
		this.isVisible = isVisible;
	}
	
	@Override
	public void setEnabled( boolean isEnabled ) {
		this.isEnabled = isEnabled;
		if( symbol != null )
			for( ISymbolElement<C> ele : symbol )
				ele.setEnabled( isEnabled );
	}
	
	@Override
	public Vector getPosition() {
		return position;
	}
	
	@Override
	public Collection<IConnectionView<C>> getAllConnections() {
		return allConnections;
	}

	@Override
	public Collection<IConnectionView<C>> getConnectionsAsStart() {
		return connectionsAsStart;
	}
	
	@Override
	public Collection<IConnectionView<C>> getConnectionsAsEnd() {
		return connectionsAsEnd;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getSimpleName() + ":" );
		sb.append( " X=" );
		sb.append( position.getX() );
		sb.append( " Y=" );
		sb.append( position.getY() );
		return sb.toString();
	}

	@Override
	public SymbolElementContainer<C> getSymbolContainer() {
		return symbol;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
	
	@Override
	public INodeModel<C> getModelNode() {
		return nodeModel;
	}

}
