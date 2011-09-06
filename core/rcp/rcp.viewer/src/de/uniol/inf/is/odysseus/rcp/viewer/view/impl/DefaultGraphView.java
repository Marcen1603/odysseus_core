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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;

public class DefaultGraphView<C> implements IGraphView<C> {

	private final Collection<IConnectionView<C>> connections = new ArrayList<IConnectionView<C>>();
	private final Collection<INodeView<C>> nodes = new ArrayList<INodeView<C>>();
	
	private static final Logger logger = LoggerFactory.getLogger( DefaultGraphView.class );
	private final IGraphModel<C> data;
	
	public DefaultGraphView( IGraphModel<C> data ) {
		this.data = data;
	}
	
	@Override
	public void insertViewedConnection( IConnectionView<C> connection ) {
		if( !connections.contains( connection )) {
			connections.add( connection );
			((DefaultNodeView<C>)connection.getViewedEndNode()).addConnection( connection );
			((DefaultNodeView<C>)connection.getViewedStartNode()).addConnection( connection );
			logger.debug( "Inserted ConnectionDisplay " + connection );
		}
	}

	@Override
	public void removeViewedConnection( IConnectionView<C> connection ) {
		if( connections.contains( connection )) {
			connections.remove( connection );
			((DefaultNodeView<C>)connection.getViewedEndNode()).removeConnection( connection );
			((DefaultNodeView<C>)connection.getViewedStartNode()).removeConnection( connection );
			logger.debug( "Removed ConnectionDisplay " + connection  );
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeViewedNode( INodeView<C> node ) {
		if( nodes.contains( node  )) {
			nodes.remove( node );
			for( IConnectionView<C> conn : node.getAllConnections().toArray( new IConnectionView[0] ) ) {
				removeViewedConnection( conn );
			}
			logger.debug( "Removed NodeDisplay " + node  );
		}
	}

	@Override
	public void insertViewedNode( INodeView<C> node ) {
		if( !nodes.contains( node )) {
			nodes.add( node );
			logger.debug( "NodeDisplay inserted " + node );
		}
	}
	
	@Override
	public Collection<IConnectionView<C>> getViewedConnections() {
		return connections;
	}
	
	@Override
	public Collection<INodeView<C> > getViewedNodes() {
		return nodes;
	}

	@Override
	public IGraphModel< C > getModelGraph() {
		return data;
	}

}
