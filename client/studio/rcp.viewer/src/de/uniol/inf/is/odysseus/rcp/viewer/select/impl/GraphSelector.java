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
package de.uniol.inf.is.odysseus.rcp.viewer.select.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;

public class GraphSelector<C> extends DefaultSelector<INodeView<C>> {

	private static final Logger logger = LoggerFactory.getLogger( GraphSelector.class );

	public boolean selectPath( INodeView<C> start, INodeView<C> end ) {
		Collection<INodeView<C>> path = new ArrayList<INodeView<C>>();
		boolean found = selectPath( path, start, end );
		if( !found ) {
			path.clear();
			found = selectPath( path, end, start );
		}
		if( !path.isEmpty() )
			select(path);
		return found;
	}

	@SuppressWarnings("unchecked")
	private <T extends INodeView<C>> boolean selectPath( Collection<T> path, T start, T end ) {
		
		if( start == end ) {
			path.add( start );
			return true;
		} 
		Collection<? extends IConnectionView<C>> conns = start.getConnectionsAsStart();
		if( conns.isEmpty() )
			return false;
			
		for( IConnectionView<C> conn : conns ) {
			if( selectPath( path, (T)conn.getViewedEndNode(), end ) ) {
				path.add( start );
				return true;
			}
		}
		
		return false;
	}

	public void selectNextNodes() {
		List<INodeView<C>> prevNodes = Lists.newArrayList();
		if( getSelectionCount() > 0 ) {
			for( INodeView<C> node : getSelected()) {
				for( IConnectionView<C> connection : node.getConnectionsAsStart()) {
					prevNodes.add(findNextNode(connection.getViewedEndNode()));
				}
			}
			if( prevNodes.isEmpty()) {
				return;
			}
		}
		unselectAll();
		select(prevNodes);
	}

	private INodeView<C> findNextNode(INodeView<C> viewedEndNode) {
		if( viewedEndNode.getModelNode() == null ) {
			return findNextNode(viewedEndNode.getConnectionsAsStart().iterator().next().getViewedEndNode());
		}
		return viewedEndNode;
	}
	
	private INodeView<C> findPrevNode(INodeView<C> viewedStartNode) {
		if( viewedStartNode.getModelNode() == null ) {
			return findPrevNode(viewedStartNode.getConnectionsAsEnd().iterator().next().getViewedStartNode());
		}
		return viewedStartNode;
	}

	public void selectPreviousNodes() {
		List<INodeView<C>> prevNodes = Lists.newArrayList();
		if( getSelectionCount() > 0 ) {
			for( INodeView<C> node : getSelected()) {
				for( IConnectionView<C> connection : node.getConnectionsAsEnd()) {
					prevNodes.add(findPrevNode(connection.getViewedStartNode()));
				}
			}
			if( prevNodes.isEmpty()) {
				return;
			}
		}
		unselectAll();
		select(prevNodes);
	}
	
	@Override
	public final void select( Collection<? extends INodeView<C>> nodeDisplays ) {
		Collection<INodeView<C>> nodesSelected = new ArrayList<INodeView<C>>();
		for( INodeView<C> disp : nodeDisplays ) {
			
			if( !disp.isVisible() )
				continue;
			
			if( logger.isTraceEnabled() )
				logger.trace( "Node selected: " + disp );
			super.select( disp );
			nodesSelected.add( disp );
		}
		notifySelectListener( this, nodesSelected );	
	}

	@Override
	public final void select( INodeView<C> nodeDisplay ) {		
		Collection<INodeView<C>> col = new ArrayList<INodeView<C>>();
		col.add( nodeDisplay );
		select(col);
	}

	@Override
	public final void unselect( Collection<? extends INodeView<C>> nodeDisplays ) {
		if( nodeDisplays == null || nodeDisplays.isEmpty() )
			return;

		for( INodeView<C> node : nodeDisplays )
			super.unselect( node );
		notifyUnselectListener( this, nodeDisplays );
	}
	
	@Override
	public final void unselect( INodeView<C> node ) {
		Collection<INodeView<C>> col = new ArrayList<INodeView<C>>();
		col.add( node );
		unselect(col);
	}

	@Override
	public final void unselectAll() {		
		Collection<INodeView<C>> selected = getSelected();
		if( selected.isEmpty() )
			return;
		
		super.unselectAll();
		notifyUnselectListener( this, selected );
	}

}
