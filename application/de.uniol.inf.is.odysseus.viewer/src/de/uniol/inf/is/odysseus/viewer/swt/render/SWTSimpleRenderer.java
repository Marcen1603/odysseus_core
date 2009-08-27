package de.uniol.inf.is.odysseus.viewer.swt.render;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.viewer.view.render.RenderRange;
import de.uniol.inf.is.odysseus.viewer.view.render.SimpleSymbolRenderer;

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
			
			gc.drawLine( start.getX(), start.getY(), end.getX(), end.getY() );
		}
		
		for( INodeView<C> node : graph.getViewedNodes() ) {
			
			final Vector pos = node.getPosition().add( shift ).mul( zoomFactor );
			final int height = (int)(node.getHeight() * zoomFactor);
			final int width = (int)(node.getWidth() * zoomFactor);

			gc.setBackground( gc.getDevice().getSystemColor( SWT.COLOR_BLACK ) );
			gc.fillRectangle( pos.getX(), pos.getY(), width, height );
		}
	}
	
}
