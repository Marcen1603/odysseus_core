package de.uniol.inf.is.odysseus.viewer.swt.select;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;

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
	
	@Override
	public final void select( Collection<? extends INodeView<C>> nodeDisplays ) {
	
		Collection<INodeView<C>> nodesSelected = new ArrayList<INodeView<C>>();
		for( INodeView<C> disp : nodeDisplays ) {
			
			if( !disp.isVisible() )
				continue;
			
			if( logger.isDebugEnabled() )
				logger.debug( "Node selected: " + disp );
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
