package de.uniol.inf.is.odysseus.viewer.view.position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.view.graph.DefaultConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.OdysseusNodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.viewer.view.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.viewer.view.symbol.SymbolElementInfo;

public final class SugiyamaPositioner implements INodePositioner<IPhysicalOperator> {
	
	private static final Logger logger = LoggerFactory.getLogger(SugiyamaPositioner.class);
	
	private static final int SPACE = 20; // Horizontaler Abstand zwischen zwei Knoten
	private static final int SPACE_HEIGHT = 75; // Vertikaler Abstand
	private HashMap< INodeView<IPhysicalOperator>, Integer> nodeLevels;
	private ArrayList< ArrayList<INodeView<IPhysicalOperator>>> layers;
	private int maxLevel = Integer.MIN_VALUE;

	private final ISymbolElementFactory<IPhysicalOperator> symbolFactory;
	private final SymbolElementInfo dummySymbolInfo;
	
	public SugiyamaPositioner( ISymbolElementFactory<IPhysicalOperator> symbolFactory ) {
		if( symbolFactory == null )
			throw new IllegalArgumentException("symbolFactory is null");
		
		this.symbolFactory = symbolFactory;
		dummySymbolInfo = new SymbolElementInfo("invisible", null, 5, 5);
	}
	
	// die Parameter width und height werden hier nicht verwendet
	@Override
	public void positionize( IGraphView<IPhysicalOperator> graph, int width, int height ) {
		
//		((Log4JLogger)logger).getLogger().setLevel(Level.TRACE);
			
		if( graph == null ) {
			logger.warn( "graph is null. Nothing to positionize." );
			return;
		}
		
		logger.debug( "Positionize NodeDisplays with Sugiyama-Algorithm." );
		
		nodeLevels = new HashMap< INodeView<IPhysicalOperator>, Integer>();
		
		/** PHASE 1 **/
		// Quellen finden.. diese haben einen Eingangsgrad von 0
		// Und Ebene der Knoten ermitteln und die Äquivalenzklassen ermitteln
		logger.trace( "Phase 1: Find equivalent-classes (or levels) of NodeDisplays" );
		int found = 0;
		for( INodeView<IPhysicalOperator> node : graph.getViewedNodes() ) {
			if( node.getConnectionsAsEnd().size() == 0 ) {
				found++;
				traverse( node, 0 );
			}
		}
		if( found == 0 )
			return;
		
		// DummyNodes erzeugen, wenn eine Kante über mehr als zwei benachbarte
		// Ebenen verläuft
		logger.trace( "Creating invisible nodes, if neccessary" );
		
		// toArray(...) um ConcurrentModificationException zu vermeiden
		for( IConnectionView<IPhysicalOperator> conn : graph.getViewedConnections().toArray( new IConnectionView[0] ) ) {
			INodeView<IPhysicalOperator> start = conn.getViewedStartNode();
			INodeView<IPhysicalOperator> end = conn.getViewedEndNode();
			
			if( !nodeLevels.containsKey( start ) || !nodeLevels.containsKey( end ))
				continue;
			
			final int startLevel = nodeLevels.get( start );
			final int endLevel = nodeLevels.get( end );
			
			if( endLevel > startLevel + 1 ) {
				
				INodeView<IPhysicalOperator> startNode = start;
				for( int i = startLevel + 1; i < endLevel ; i++ ) {
					
					//unsichtbaren Knoten erzeugen
					INodeView<IPhysicalOperator> dummyNode = new OdysseusNodeView(null);
					dummyNode.setWidth( 5 );
					dummyNode.setHeight( 5 );
					dummyNode.getSymbolContainer().add( symbolFactory.createForNode( dummySymbolInfo ) );
					graph.insertViewedNode( dummyNode );
					nodeLevels.put( dummyNode, i );
					
					// Verbindung ohne Pfeil
					IConnectionView<IPhysicalOperator> dummyConnection = new DefaultConnectionView<IPhysicalOperator>(conn.getModelConnection(), startNode, dummyNode );
					final IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection( "Normal" ); 
					ele.setConnectionView( dummyConnection );
					dummyConnection.getSymbolContainer().add( ele );
					graph.insertViewedConnection( dummyConnection );
					startNode = dummyNode;
				}
				
				// Letzte Verbindung anlegen (mit Pfeil)
				IConnectionView<IPhysicalOperator> dummyConnection = new DefaultConnectionView<IPhysicalOperator>(conn.getModelConnection(), startNode, end );
				final IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection( "Arrow" ); 
				ele.setConnectionView( dummyConnection );
				dummyConnection.getSymbolContainer().add( ele );
				graph.insertViewedConnection( dummyConnection );

				graph.insertViewedConnection( new DefaultConnectionView<IPhysicalOperator>(conn.getModelConnection(), startNode, end) );
				graph.removeViewedConnection( conn );
			}
		}
		logger.debug("It exists " + ( maxLevel + 1 )+ " levels");
		
		/** PHASE 2 **/
		// Knoten der Ebenen arrangieren, sodass möglichst wenige Kreuzungen vorkommen
		if( logger.isTraceEnabled() )
			logger.trace( "Phase 2: Arrange nodes" );

		// Anzahlen der Knoten pro Ebene ermitteln und Knoten zuordnen
		layers = new ArrayList< ArrayList<INodeView<IPhysicalOperator>>>();
		for( int i = 0; i < maxLevel + 1; i++ )
			layers.add( new ArrayList<INodeView<IPhysicalOperator>>() );

		for( INodeView<IPhysicalOperator> node : nodeLevels.keySet()) {
			int l = nodeLevels.get( node );
			ArrayList<INodeView<IPhysicalOperator>> n = layers.get( l );
			n.add( node );
		}
		
		if( logger.isDebugEnabled()) {
			for( int i = 0; i < layers.size(); i++ )
				logger.debug("Level " + i + " has " + layers.get( i ).size() + " DisplayNodes");
		}
			
		
		// layer-by-layer sweep
		final int maxCycles = 2;
		int currentCycle = 0;
		boolean changed = true;
		
		if( logger.isTraceEnabled())
			logger.trace( "Layer-by-layer sweep: " + maxCycles + " times" );
		
		while( changed && currentCycle < maxCycles) {
			if( logger.isTraceEnabled() )
				logger.trace( "Layer-by-layer sweep cycle " + (currentCycle + 1) );
			
			changed = false;
			
			// ************** von oben nach unten
			for( int i = 0; i < layers.size() - 1; i++ ) {
				final int fixedLayer = i; // Lesbarkeit...
				final int varLayer = i + 1;
				
				// Median-Werte der Knoten der aktuellen Ebene bestimmen
				ArrayList<Integer> medians = new ArrayList<Integer>();
				for( int e = 0 ; e < layers.get( varLayer ).size(); e++ ) {
					
					// Median der Nachfolgerknoten bestimmen
					final int med = getMedian(layers.get( varLayer ).get( e ), layers.get( fixedLayer ), true );
					
					medians.add( med );
				}
				
				changed = sortByMedians( layers.get( varLayer ), medians ) || changed;
			}
			
			// ****************** und von unten nach oben
			for( int i = layers.size() - 1; i > 0; i-- ) {
				final int fixedLayer = i; // Lesbarkeit...
				final int varLayer = i - 1;
				
				// Median-Werte der Knoten der aktuellen Ebene bestimmen
				ArrayList<Integer> medians = new ArrayList<Integer>();
				for( int e = 0 ; e < layers.get( varLayer ).size(); e++ ) {
					
					// Median der Nachfolgerknoten bestimmen
					final int med = getMedian(layers.get( varLayer ).get( e ), layers.get( fixedLayer ), false);
										
					medians.add( med );
				}
				
				changed = sortByMedians( layers.get( varLayer ), medians ) || changed;
				
			}
			currentCycle++;
			if( changed == false )
				logger.trace( "Nothing changed. layer-to-layer sweep finished!" );
		}

		/** PHASE 3 **/
		logger.trace( "Phase 3: Calculate x- and y-Coordinates" );
		
		// Arrays initialisieren
		// Diese werden die X-Koordinaten beherbergen
		int[][] posXRight = new int[layers.size()][];
		for(int i = 0; i < layers.size(); i++ ) {
			posXRight[i] = new int[layers.get(i).size()];
		}
		int[][] posXLeft = new int[layers.size()][];
		for(int i = 0; i < layers.size(); i++ ) {
			posXLeft[i] = new int[layers.get(i).size()];
		}
		
		
		// Abstand zwischen zwei Knoten
		int highestX = Integer.MIN_VALUE;
		for( int layer = 0; layer < layers.size(); layer++) {
		
			int lastX = 0;
			int lastWidth = 0;
			for( int index = 0; index < layers.get( layer ).size(); index++ ) {
				final INodeView<IPhysicalOperator> currNode = layers.get( layer ).get( index );
				final int currWidth = currNode.getWidth();
				
				posXRight[layer][index] = 0;
				if( layer > 0 ) {
					final int med = getMedian( currNode, layers.get( layer - 1 ), true );
					final INodeView<IPhysicalOperator> parentNode = layers.get( layer-1 ).get( med );
					final int parentWidth = parentNode.getWidth();
					
					if( parentWidth < currWidth ) {
						posXRight[layer][index] = posXRight[layer-1][med] - ( currWidth - parentWidth ) / 2;
					} else if( parentWidth > currWidth ) {
						posXRight[layer][index] = posXRight[layer-1][med] + ( parentWidth - currWidth ) / 2;
					} else {					
						posXRight[layer][index] = posXRight[layer-1][med];
					}
					
				}
				
				if( posXRight[layer][index] < lastX + lastWidth + SPACE) {
					posXRight[layer][index] = lastX + lastWidth + SPACE;
				}
				
				lastX = posXRight[layer][index];
				lastWidth = currNode.getWidth();
				if( highestX < lastX + lastWidth )
					highestX = lastX + lastWidth;
			}
		}
		
		for( int layer = 0; layer < layers.size(); layer++) {
			
			int lastX = highestX;
			for( int index = layers.get( layer ).size() - 1; index >= 0; index-- ) {
				final INodeView<IPhysicalOperator> currNode = layers.get( layer ).get( index );
				final int currWidth = currNode.getWidth();
				
				posXLeft[layer][index] = highestX - currNode.getWidth();
				if( layer > 0 ){
					final int med = getMedian( currNode, layers.get( layer - 1 ), true );
					final INodeView<IPhysicalOperator> parentNode = layers.get( layer-1 ).get( med );
					final int parentWidth = parentNode.getWidth();
					
					if( parentWidth < currWidth ) {
						posXLeft[layer][index] = posXLeft[layer-1][med] - ( currWidth - parentWidth ) / 2;
					} else if( parentWidth > currWidth ) {
						posXLeft[layer][index] = posXLeft[layer-1][med] + ( parentWidth - currWidth ) / 2;
					} else {					
						posXLeft[layer][index] = posXLeft[layer-1][med];
					}

				}
				
				if( posXLeft[layer][index] + currNode.getWidth() > lastX ) {
					posXLeft[layer][index] = lastX - currNode.getWidth() - SPACE;
				}
				
				lastX = posXLeft[layer][index] - currNode.getWidth();
			}
		}
		
		logger.debug( "Final NodeDisplay positions" );
		for( int layer = 0; layer < layers.size(); layer++) {
			final int posY = layers.size() * SPACE_HEIGHT - SPACE_HEIGHT * (layer + 1);
			for( int index = layers.get( layer ).size() - 1; index >= 0; index-- ) {
				INodeView<IPhysicalOperator> currNode = layers.get( layer ).get( index );
				currNode.setPosition( new Vector(( posXRight[layer][index] + posXLeft[layer][index] ) / 2, posY ) );
			}
		}
	}
	
	private int getMedian( INodeView<IPhysicalOperator> node, ArrayList<INodeView<IPhysicalOperator>> fixedLayerNodes, boolean fromTop ) {
		// Nachfolgerknoten holen
		ArrayList<INodeView<IPhysicalOperator>> neighbours = new ArrayList<INodeView<IPhysicalOperator>>();
		Collection<? extends IConnectionView<IPhysicalOperator>> conns = (fromTop == true ) ? node.getConnectionsAsEnd() : node.getConnectionsAsStart();
		for( IConnectionView<IPhysicalOperator> conn : conns ) {
			neighbours.add( (fromTop == true ) ? conn.getViewedStartNode() : conn.getViewedEndNode() );
		}
		
		// Median der Nachfolgerknoten bestimmen
		final int med;
		if( !neighbours.isEmpty()) {
		
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for( int p = 0; p < neighbours.size(); p++ ) {
				int pos = fixedLayerNodes.indexOf( neighbours.get( p ) );
				indexes.add( pos );
			}
			
			Collections.sort( indexes );
			med = indexes.get( indexes.size() / 2 );
		} else {
			med = 0;
		}		
		return med;
	}

	private void traverse( INodeView<IPhysicalOperator> node, int level ) {
		if( node == null ) {
			if( logger.isWarnEnabled() )
				logger.warn("node in level " + level + " is null!");
			return;
		}
		if( level < 0 ) {
			if( logger.isErrorEnabled() )
				logger.error( "level is negative: " + level );
			throw new IllegalArgumentException("level is negative: " + level);
		}
		
		if( logger.isTraceEnabled() ) 
			logger.trace( "Begin traverse level " + level );
		
		maxLevel = Math.max( maxLevel, level );
		final Collection<? extends IConnectionView<IPhysicalOperator>> connections = node.getConnectionsAsStart();
		
		for( IConnectionView<IPhysicalOperator> conn : connections ) {
			traverse( conn.getViewedEndNode(), level + 1 );
		}
		
		if( nodeLevels.containsKey( node ) ) {
			final int actLevel = nodeLevels.get( node );
			if( actLevel < level ) {
				if( logger.isDebugEnabled() )
					logger.debug("Higher NodeLevel inserted: " + node + " in level " + level + "(before was " + actLevel + ")");

				nodeLevels.put( node, level );
			}
		} else {
			if( logger.isDebugEnabled() )
				logger.debug("New NodeLevel inserted: " + node + " in level " + level );
			nodeLevels.put( node, level );		
		}
		
		if( logger.isTraceEnabled() ) 
			logger.trace( "End traverse level " + level );
	}
	
	private boolean sortByMedians( ArrayList<INodeView<IPhysicalOperator>> nodes, ArrayList<Integer> medians ) {
		if( nodes.size() != medians.size() ) {
			if( logger.isWarnEnabled() )
				logger.warn( "nodes and medians arrays are not in the same size!" );
			return false;
		}
		
		if( logger.isTraceEnabled()) {
			logger.trace( "sort nodes by their medians" );
		}
			
		boolean changed = false;
		// Knoten der aktuellen Ebene nach dem Median sortieren...
		for( int p = 0; p < medians.size(); p++ ) {
			for( int q = 0; q < medians.size() - 1; q++) {
				if( medians.get( q ) > medians.get(q + 1)) {
					// Positionen tauschen
					int tmp = medians.get( q );
					medians.set( q, medians.get( q + 1 ) );
					medians.set( q + 1, tmp );
					INodeView<IPhysicalOperator> tmpDisplay = nodes.get( q );
					nodes.set( q, nodes.get( q+1 ) );
					nodes.set( q+1, tmpDisplay );
					changed = true;
				} else if( medians.get( q ) == medians.get( q + 1 )) {
					if( nodes.get( q ).getConnectionsAsEnd().size() % 2 == 1 && 
					    nodes.get( q + 1 ).getConnectionsAsEnd().size() % 2 == 0 ) {
						// Positionen tauschen
						int tmp = medians.get( q );
						medians.set( q, medians.get( q + 1 ) );
						medians.set( q + 1, tmp );
						INodeView<IPhysicalOperator> tmpDisplay = nodes.get( q );
						nodes.set( q, nodes.get( q+1 ) );
						nodes.set( q+1, tmpDisplay );
						changed = true;
					}
				}
			}
		}
		
		return changed;
		
	}

}
