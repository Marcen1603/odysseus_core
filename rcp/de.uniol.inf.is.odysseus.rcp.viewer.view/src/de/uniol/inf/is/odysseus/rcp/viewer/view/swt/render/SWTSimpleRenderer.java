package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.render;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl.RenderRange;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl.SimpleSymbolRenderer;

public class SWTSimpleRenderer<C> extends SimpleSymbolRenderer<C> {

	private GC gc;
	
	public void setGC( GC gc ) {
		this.gc = gc;
	}
	
	@Override
	public void render( IGraphView< C > graph, float zoomFactor, RenderRange renderRange, Vector shift ) {
		if( graph == null )
			return;
		if( gc == null || gc.isDisposed() )
			return;
		
		for( IConnectionView<C> conn : graph.getViewedConnections() ) {
			final INodeView<C> startNode = conn.getViewedStartNode();
			final INodeView<C> endNode = conn.getViewedEndNode();
						
			if( !startNode.isVisible() || !endNode.isVisible() )
				continue;
				
			final Vector start = startNode.getPosition()
							.add( shift )
							.add( startNode.getWidth() / 2, startNode.getHeight() / 2 )
							.mul( zoomFactor );

			final Vector end = endNode.getPosition()
							.add( shift )
							.add( startNode.getWidth() / 2, startNode.getHeight() / 2 )
							.mul( zoomFactor );
			
			gc.drawLine( (int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY() );
		}
		
		for( INodeView<C> node : graph.getViewedNodes() ) {
			
			final Vector pos = node.getPosition().add( shift ).mul( zoomFactor );
			final int height = (int)(node.getHeight() * zoomFactor);
			final int width = (int)(node.getWidth() * zoomFactor);

			gc.setBackground( gc.getDevice().getSystemColor( SWT.COLOR_BLACK ) );
			gc.fillRectangle( (int)pos.getX(), (int)pos.getY(), width, height );
		}
	}
	
}
