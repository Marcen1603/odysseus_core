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
package de.uniol.inf.is.odysseus.rcp.viewer.swt.render;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.render.impl.RenderRange;
import de.uniol.inf.is.odysseus.rcp.viewer.render.impl.SimpleSymbolRenderer;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTSelectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementContainer;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTSymbolRenderer<C> extends SimpleSymbolRenderer<C>  {
	
	private static final Logger logger = LoggerFactory.getLogger( SWTSymbolRenderer.class );

	private GC gc;
	private SymbolElementContainer<C> selectionSymbol = new SymbolElementContainer<C>();
	private SymbolElementContainer<C> highlightSymbol = new SymbolElementContainer<C>();
	private SymbolElementContainer<C> breakSymbol = new SymbolElementContainer<C>();
	private SymbolElementContainer<C> breakEndSymbol = new SymbolElementContainer<C>();

	public SWTSymbolRenderer() {
		selectionSymbol.add( new SWTSelectionSymbolElement< C >( Display.getCurrent().getSystemColor( SWT.COLOR_RED )) );
		highlightSymbol.add( new SWTSelectionSymbolElement< C >( Display.getCurrent().getSystemColor( SWT.COLOR_BLUE )) );
		breakSymbol.add( new SWTSelectionSymbolElement< C >( Display.getCurrent().getSystemColor( SWT.COLOR_GREEN )) );
		breakEndSymbol.add( new SWTSelectionSymbolElement< C >( Display.getCurrent().getSystemColor( SWT.COLOR_GRAY )) );
		
		logger.info( "SWTSymbolRenderer created" );
	}
	
	public void setGC( GC gc ) {
		this.gc = gc;
	}
	
	public void render( IGraphView< C > graph, Collection<? extends INodeView<C>> highlightedNodes, Collection< ? extends INodeView< C >> selectedNodes, float zoomFactor, RenderRange renderRange, Vector shift ) {
		if( graph == null )
			return;
				
		// Auswahl rendern
		
		for( INodeView<C> node : graph.getViewedNodes() ) {
			if( selectedNodes.contains(node) ) {
				final Vector pos = node.getPosition().add( shift ).mul( zoomFactor );
				final int height = (int)(node.getHeight() * zoomFactor);
				final int width = (int)(node.getWidth() * zoomFactor);
				renderSymbol( selectionSymbol,  pos, width, height, shift, zoomFactor );
			} else if( highlightedNodes.contains(node)) {
				final Vector pos = node.getPosition().add( shift ).mul( zoomFactor );
				final int height = (int)(node.getHeight() * zoomFactor);
				final int width = (int)(node.getWidth() * zoomFactor);
				renderSymbol( highlightSymbol,  pos, width, height, shift, zoomFactor );
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

			final Vector sizeShift = determineSizeShift(startNode, endNode);
			final Vector end = endNode.getPosition()
							.add( shift )
							.add( sizeShift )
							.mul( zoomFactor );
			
			for( IConnectionSymbolElement<C> ele : conn.getSymbolContainer() ) {
				((SWTConnectionSymbolElement<C>)ele).setActualGC( gc );
				ele.draw( start, end, shift, zoomFactor );
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
			
			renderNode(node, pos, width, height, shift, zoomFactor);
		}
	}
	
	private void renderSymbol( SymbolElementContainer<C> symbol, Vector pos, int width, int height, Vector shift, float zoomFactor ) {
		for( ISymbolElement<C> element : symbol ) {
			if( element != null )
				renderSymbolElement( element, pos, width, height, shift, zoomFactor );
		}
	}

	private void renderNode( INodeView<C> node, Vector pos, int width, int height, Vector shift, float zoomFactor ) {
		for( ISymbolElement<C> symbol : node.getSymbolContainer() ) {
			if( symbol != null )
				renderSymbolElement( symbol, pos, width, height, shift, zoomFactor );
		}
	}
	
	private void renderSymbolElement( ISymbolElement<C> element, Vector pos, int width, int height, Vector shift, float zoomFactor ) {
		((SWTSymbolElement<C>)element).setActualGC( gc );
		element.draw( pos, width, height, shift, zoomFactor );
	}

	private static Vector determineSizeShift(final INodeView<?> startNode, final INodeView<?> endNode) {
		if( endNode.getConnectionsAsEnd().size() == 1 ) {
			return new Vector( endNode.getWidth() / 2, endNode.getHeight() / 2 );
		}
		
		int incomingCount = 10;
		int nearestIndex = 0;
		double minDist = Integer.MAX_VALUE;
		
		for( int i = 0; i < incomingCount; i++ ) {
			
			Vector possiblePos = new Vector( endNode.getWidth() / (incomingCount + 1) * ( i + 1) + endNode.getPosition().getX(), 
										endNode.getHeight() / 2 + endNode.getPosition().getY() );
			double diffX = possiblePos.getX() - ( startNode.getPosition().getX() + startNode.getWidth() / 2);
			double diffY = possiblePos.getY() - ( startNode.getPosition().getY() + startNode.getHeight() / 2);
			double dist = diffX * diffX + diffY * diffY;
			
			if( dist < minDist){
				minDist = dist;
				nearestIndex = i;
			}
		}
		
		return new Vector( endNode.getWidth() / (incomingCount + 1) * ( nearestIndex + 1), endNode.getHeight() / 2 );
	}
}
