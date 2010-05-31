package de.uniol.inf.is.odysseus.rcp.viewer.view.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl.DefaultConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl.OdysseusNodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.manage.IGraphViewFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.SymbolElementInfo;

public class GraphViewEditorInputFactory implements IGraphViewFactory<IPhysicalOperator>{

	private static final Logger logger = LoggerFactory.getLogger( GraphViewEditorInputFactory.class );

	
//	public OdysseusGraphViewFactory() {
//		((Log4JLogger)logger).getLogger().setLevel( Level.DEBUG ); 
//	}
	
	@Override
	public IGraphView<IPhysicalOperator> createGraphView( IGraphModel<IPhysicalOperator> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<IPhysicalOperator> symbolFactory ) {
		
		if( graph == null ) 
			throw new IllegalArgumentException("graph is null!");
		
		if( symbolConfig == null ) 
			throw new IllegalArgumentException("symbolConfig is null!");
		
		if( symbolFactory == null ) 
			throw new IllegalArgumentException("symbolFactory is null!");
		
		// Graph
		IGraphView<IPhysicalOperator> graphView = new GraphViewEditorInput(graph);
		
		// Knoten
		final Map<String, Collection<SymbolElementInfo>> map = symbolConfig.getMap();
		if( map == null )
			throw new IllegalArgumentException("symbolConfig provides no map!");
		
		Collection<? extends INodeModel<IPhysicalOperator>> nodes = graph.getNodes();
		for( INodeModel<IPhysicalOperator> node : nodes ) {
			
			// Knoten zum Graphen packen
			graphView.insertViewedNode( createNodeDisplay(node, symbolFactory, symbolConfig) );
		}
		
		// Verbindungen
		for( IConnectionModel<IPhysicalOperator> conn : graph.getConnections()) {
			
			DefaultConnectionView<IPhysicalOperator> connView = new DefaultConnectionView<IPhysicalOperator>(conn,
					findFirstNodeDisplay(graphView, conn.getStartNode()), 
					findFirstNodeDisplay(graphView, conn.getEndNode()));
			
			createConnectionSymbol( connView, symbolFactory );
			graphView.insertViewedConnection( connView );
		}
		
		return graphView;
	}
	
	@SuppressWarnings("unchecked")
	private INodeView<IPhysicalOperator> findFirstNodeDisplay( IGraphView<IPhysicalOperator> graphDisplay, INodeModel<IPhysicalOperator> node ) {
		Collection<INodeView<IPhysicalOperator>> found = findNodeDisplay( graphDisplay, node );
		if( found.isEmpty() )
			return null;
		return found.toArray(new INodeView[1])[0];
	}
	
	private Collection<INodeView<IPhysicalOperator>> findNodeDisplay( IGraphView<IPhysicalOperator> graphDisplay, INodeModel<IPhysicalOperator> node ) {
		Collection<INodeView<IPhysicalOperator>> found = new ArrayList<INodeView<IPhysicalOperator>>();
		for( INodeView<IPhysicalOperator> nodeDisplay : graphDisplay.getViewedNodes()) {
			if( nodeDisplay.getModelNode() == node )
				found.add( nodeDisplay );
		}
		return found;
	}
	
	private Collection<IConnectionView<IPhysicalOperator>> findConnectionDisplay( IGraphView<IPhysicalOperator> graphDisplay, IConnectionModel<IPhysicalOperator> conn ) {
		Collection<IConnectionView<IPhysicalOperator>> found = new ArrayList<IConnectionView<IPhysicalOperator>>();
		for( IConnectionView<IPhysicalOperator> connDisplay : graphDisplay.getViewedConnections() ) {
			if( connDisplay.getModelConnection() == conn ) 
				found.add( connDisplay );
		}
		return found;	
	}

	@Override
	public void updateGraphDisplay( IGraphModel<IPhysicalOperator> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<IPhysicalOperator> symbolFactory, IGraphView<IPhysicalOperator> graphDisplay ) {
		logger.info( "Updating GraphDisplay" );
		
		if( graph == null ) 
			throw new IllegalArgumentException("graph is null!");
		
		if( symbolConfig == null ) 
			throw new IllegalArgumentException("symbolConfig is null!");
		
		if( symbolFactory == null ) 
			throw new IllegalArgumentException("symbolFactory is null!");
		
		if( graphDisplay == null )
			throw new IllegalArgumentException("graphDisplay is null!");
		
		// Schauen, ob Knoten weggefallen sind
		for( IOdysseusNodeView nodeDisplay : graphDisplay.getViewedNodes().toArray( new IOdysseusNodeView[0]) ) {
			INodeModel<IPhysicalOperator> nodeModel = nodeDisplay.getModelNode();
			if( !graph.getNodes().contains( nodeModel ) ) {
				graphDisplay.removeViewedNode( nodeDisplay );
			}
		}
		
		// Schauen, ob neue Knoten hinzugefügt worden sind
		final Map<String, Collection<SymbolElementInfo>> map = symbolConfig.getMap();
		for( INodeModel<IPhysicalOperator> nodeModel : graph.getNodes() ) {
			Collection<INodeView<IPhysicalOperator>> nodeDisplays = findNodeDisplay( graphDisplay, nodeModel );
			if( nodeDisplays.isEmpty() ) {
				graphDisplay.insertViewedNode(createNodeDisplay(nodeModel, symbolFactory, symbolConfig ));
			}
		}
		
		// Schauen, ob alte Verbindungen wegfallen
		for( IConnectionView<IPhysicalOperator> conn : graphDisplay.getViewedConnections() ) {
			if( !graph.getConnections().contains( conn.getModelConnection() )) {
				graphDisplay.removeViewedConnection( conn );
			}
		}
		
		// Schauen, ob neue Verbindungen hinzugekommen sind
		for( IConnectionModel<IPhysicalOperator> connModel : graph.getConnections() ) {
			if( findConnectionDisplay( graphDisplay, connModel).isEmpty() ) {
				
				INodeView<IPhysicalOperator> startNode = findFirstNodeDisplay(graphDisplay, connModel.getStartNode());
				INodeView<IPhysicalOperator> endNode = findFirstNodeDisplay(graphDisplay, connModel.getEndNode());
				
				if( startNode == null ) {
					logger.error("StartNode f�r die Verbindung " + connModel.toString() + " nicht gefunden");
					continue;
				}
				if( endNode == null ) {
					logger.error("EndNode f�r die Verbindung " + connModel.toString() + " nicht gefunden");
					continue;
				}
				
				DefaultConnectionView<IPhysicalOperator> connView = new DefaultConnectionView<IPhysicalOperator>(connModel,
						startNode, endNode);
				
				createConnectionSymbol( connView, symbolFactory );
				graphDisplay.insertViewedConnection( connView );
			}
		}
		
		// Die Konfiguration kann sich geändert haben...
		// daher alle Symbole neu erstellen
		// sicherheitshalber
		for( INodeView<IPhysicalOperator> nodeView : graphDisplay.getViewedNodes() ) {
			if( nodeView.getModelNode() != null )  {
				Collection<SymbolElementInfo> symbols = map.get( nodeView.getModelNode().getName() );
				if( symbols != null ) 
					createSymbols( nodeView, symbols, symbolFactory );
				else {
					Collection<SymbolElementInfo> defaultSymbolInfos = symbolConfig.getDefaultSymbolInfos();
					if( defaultSymbolInfos == null || defaultSymbolInfos.size() == 0) 
						logger.warn( "No default node for '" + nodeView.getModelNode().getName() + "' specified. Use internal standard.");
					
					createSymbols( nodeView, defaultSymbolInfos, symbolFactory );
				}
			}
		}
		
		
		logger.info( "GraphDisplay updated" );
	}

	private INodeView<IPhysicalOperator> createNodeDisplay( INodeModel<IPhysicalOperator> node, ISymbolElementFactory<IPhysicalOperator> symbolFactory, ISymbolConfiguration symbolConfig ) {
		OdysseusNodeView nodeDisplay = new OdysseusNodeView((IOdysseusNodeModel)node);
		
		// Typ ermitteln
		final String nodeType = node.getName();
		// Symbole erstellen
		Map<String, Collection<SymbolElementInfo>> map = symbolConfig.getMap();
		Collection<SymbolElementInfo> symbols = map.get( nodeType );
		if( symbols != null ) 
			createSymbols( nodeDisplay, symbols, symbolFactory );
		else {
			Collection<SymbolElementInfo> defaultSymbolInfos = symbolConfig.getDefaultSymbolInfos();
			if( defaultSymbolInfos == null || defaultSymbolInfos.size() == 0) 
				logger.warn( "No default node for '" + nodeType + "' specified. Use internal standard.");
			
			createSymbols( nodeDisplay, defaultSymbolInfos, symbolFactory );
			
		} 
		return nodeDisplay;
	}
	
	private void createConnectionSymbol(IConnectionView<IPhysicalOperator> connView, ISymbolElementFactory<IPhysicalOperator> symbolFactory ) {
		IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection("Arrow");
		ele.setConnectionView( connView );
		connView.getSymbolContainer().add( ele );
	}
	
	private void createSymbols(INodeView<IPhysicalOperator> nodeView, Collection<SymbolElementInfo> symbolInfos, ISymbolElementFactory<IPhysicalOperator> symbolFactory ) {
		if( nodeView == null )
			return;
		if( symbolFactory == null )
			return;
		
		nodeView.getSymbolContainer().clear();
			
		if( symbolInfos != null && symbolInfos.size() > 0) {
			for( SymbolElementInfo info : symbolInfos) {
				
				ISymbolElement<IPhysicalOperator> ele = symbolFactory.createForNode( info );
				ele.setNodeView( nodeView );
				
				nodeView.getSymbolContainer().add( ele );
				nodeView.setWidth( info.getWidth() );
				nodeView.setHeight( info.getHeight() );
			}
		} else {
			ISymbolElement<IPhysicalOperator> ele = symbolFactory.createDefaultSymbolElement();
			ele.setNodeView( nodeView );
			nodeView.getSymbolContainer().add( ele );
		}
	}
}
