package de.uniol.inf.is.odysseus.rcp.viewer.render.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.render.IRenderer;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ConnectionSymbolElementContainer;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementContainer;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SimpleSymbolRenderer<C> implements IRenderer<C> {
		
	@Override
	public void update( IGraphView<C> graph ) {
		if( graph == null )
			return;
		
		for( IConnectionView<C> conn : graph.getViewedConnections() ) {
			final ConnectionSymbolElementContainer<C> connSymbol = conn.getSymbolContainer();
			
			for( IConnectionSymbolElement<C> ele : connSymbol ) {
				ele.update();
			}
		}
		
		for( INodeView<C> node : graph.getViewedNodes() ) {
			if( !node.isVisible() )
				continue;
			
			if( node.getSymbolContainer().getSize() > 0 ) {
				updateSymbol(node.getSymbolContainer());
			} 
		}
	}
	
	@Override
	public void render( IGraphView<C> graph, float zoomFactor, RenderRange renderRange, Vector shift ) {
		
		if( graph == null )
			return;
						
		for( IConnectionView<C> conn : graph.getViewedConnections() ) {
			final ConnectionSymbolElementContainer<C> connSymbol = conn.getSymbolContainer();
		
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
						
			for( IConnectionSymbolElement<C> ele : connSymbol ) {
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
			
			if( node.getSymbolContainer().getSize() > 0 ) {
				renderNode(node, pos, width, height, zoomFactor);
			} 
			
		}
	}
	
	private void updateSymbol( SymbolElementContainer<C> symbol ) {
		for( ISymbolElement<C> element : symbol ) {
			if( element != null )
				element.update();
		}
	}
	
	private void renderNode( INodeView<C> node, Vector pos, int width, int height, float zoomFactor ) {
		for( ISymbolElement<C> symbol : node.getSymbolContainer() ) {
			if( symbol != null )
				renderSymbolElement( symbol, pos, width, height, zoomFactor );
		}
	}
	
	private void renderSymbolElement( ISymbolElement<C> element, Vector pos, int width, int height, float zoomFactor ) {
		element.draw( pos, width, height, zoomFactor );
	}

}
