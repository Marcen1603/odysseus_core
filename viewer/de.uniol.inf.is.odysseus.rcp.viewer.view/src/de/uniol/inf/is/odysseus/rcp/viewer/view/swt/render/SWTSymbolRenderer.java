package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.render;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl.RenderRange;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl.SimpleSymbolRenderer;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.AbstractSWTConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.AbstractSWTSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSelectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.SymbolElementContainer;

public class SWTSymbolRenderer<C> extends SimpleSymbolRenderer<C> {
	
	private static final Logger logger = LoggerFactory.getLogger( SWTSymbolRenderer.class );

	private GC gc;
	private SymbolElementContainer<C> selectionSymbol = new SymbolElementContainer<C>();
	
	
	public SWTSymbolRenderer() {
		selectionSymbol.add( new SWTSelectionSymbolElement< C >( Display.getCurrent().getSystemColor( SWT.COLOR_RED )) );
		logger.info( "SWTSymbolRenderer created" );
	}
	
	public void setGC( GC gc ) {
		this.gc = gc;
	}
	
	public void render( IGraphView< C > graph, Collection< ? extends INodeView< C >> selectedNodes, float zoomFactor, RenderRange renderRange, Vector shift ) {
		if( graph == null )
			return;
				
		// Auswahl rendern
		
		for( INodeView<C> node : graph.getViewedNodes() ) {
			if( contains( selectedNodes, node )) {
				final Vector pos = node.getPosition().add( shift ).mul( zoomFactor );
				final int height = (int)(node.getHeight() * zoomFactor);
				final int width = (int)(node.getWidth() * zoomFactor);
				renderSymbol( selectionSymbol,  pos, width, height, zoomFactor );
			}
		}
		
		render( graph, zoomFactor, renderRange, shift );
	}
	
	// Muss überschrieben werden, da SWT mit GC arbeitet. Die Symbole
	// brauchen dies, da es sich bei jedem Neuzeichnen ändert.
	@Override
	public void render( IGraphView< C > graph, float zoomFactor, RenderRange renderRange, Vector shift ) {
		if( graph == null )
			return;
				
		for( IConnectionView<C> conn : graph.getViewedConnections() ) {
		
			final INodeView<C> startNode = conn.getViewedStartNode();
			final INodeView<C> endNode = conn.getViewedEndNode();
						
			if( startNode == null || !startNode.isVisible() || endNode == null || !endNode.isVisible() )
				continue;
				
			final Vector start = startNode.getPosition()
							.add( shift )
							.add( startNode.getWidth() / 2, startNode.getHeight() / 2 )
							.mul( zoomFactor );

			final Vector end = endNode.getPosition()
							.add( shift )
							.add( endNode.getWidth() / 2, endNode.getHeight() / 2 )
							.mul( zoomFactor );
			
			for( IConnectionSymbolElement<C> ele : conn.getSymbolContainer() ) {
				((AbstractSWTConnectionSymbolElement<C>)ele).setActualGC( gc );
				ele.draw( start, end, zoomFactor );
			}
		}
			
		
		for( INodeView<C> node : graph.getViewedNodes() ) {
			if( !node.isVisible() )
				continue;
			
			final Vector pos = node.getPosition().add( shift ).mul( zoomFactor );
			final int height = (int)(node.getHeight() * zoomFactor);
			final int width = (int)(node.getWidth() * zoomFactor);
			
			// Sichtbarkeitstest
			if( renderRange != null ) {
				if( !renderRange.intersects( pos, width, height )) {
					continue;
				}
			}
			
			renderNode(node, pos, width, height, zoomFactor);
			
			
		}
	}
	
	private boolean contains( Collection<? extends INodeView<C>> nodes, INodeView<C> node ) {
		if( nodes == null )
			return false;
		
		for( INodeView<C> n : nodes ) {
			if( n == node )
				return true;
		}
		return false;
	}
	
	private void renderSymbol( SymbolElementContainer<C> symbol, Vector pos, int width, int height, float zoomFactor ) {
		for( ISymbolElement<C> element : symbol ) {
			if( element != null )
				renderSymbolElement( element, pos, width, height, zoomFactor );
		}
	}

	private void renderNode( INodeView<C> node, Vector pos, int width, int height, float zoomFactor ) {
		for( ISymbolElement<C> symbol : node.getSymbolContainer() ) {
			if( symbol != null )
				renderSymbolElement( symbol, pos, width, height, zoomFactor );
		}
	}
	
	private void renderSymbolElement( ISymbolElement<C> element, Vector pos, int width, int height, float zoomFactor ) {
		((AbstractSWTSymbolElement<C>)element).setActualGC( gc );
		element.draw( pos, width, height, zoomFactor );
	}
}
