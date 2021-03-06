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
package de.uniol.inf.is.odysseus.rcp.viewer.manage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.manage.IGraphViewFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementInfo;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.DefaultConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusNodeView;

public class OdysseusGraphViewFactory implements IGraphViewFactory<IPhysicalOperator> {

	private static final Logger logger = LoggerFactory.getLogger(OdysseusGraphViewFactory.class);

	// public OdysseusGraphViewFactory() {
	// ((Log4JLogger)logger).getLogger().setLevel( Level.DEBUG );
	// }

	@Override
	public IGraphView<IPhysicalOperator> createGraphView(IGraphModel<IPhysicalOperator> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		Objects.requireNonNull(graph, "Graph must not be null!");
		Objects.requireNonNull(symbolConfig, "SymbolConfig must not be null!");
		Objects.requireNonNull(symbolFactory, "SymbolFactory must not be null!");

		// Graph
		final IOdysseusGraphView graphView = new OdysseusGraphView((IOdysseusGraphModel) graph);

		// Knoten
		final Map<String, Collection<SymbolElementInfo>> map = symbolConfig.getMap();
		if (map == null) {
			throw new IllegalArgumentException("symbolConfig provides no map!");
		}

		final Collection<? extends INodeModel<IPhysicalOperator>> nodes = graph.getNodes();
		for (final INodeModel<IPhysicalOperator> node : nodes) {

			// Knoten zum Graphen packen
			graphView.insertViewedNode(createNodeDisplay(node, symbolFactory, symbolConfig));
		}

		// Verbindungen
		for (final IConnectionModel<IPhysicalOperator> conn : graph.getConnections()) {

			final DefaultConnectionView<IPhysicalOperator> connView = new OdysseusConnectionView(conn, findFirstNodeDisplay(graphView, conn.getStartNode()), findFirstNodeDisplay(graphView,
					conn.getEndNode()));

			createConnectionSymbol(connView, symbolFactory);
			graphView.insertViewedConnection(connView);
		}

		// Filter
		final List<INodeView<IPhysicalOperator>> nodesToFilter = Lists.newArrayList();
		for (final INodeView<IPhysicalOperator> node : graphView.getViewedNodes()) {
			if (PhysicalOperatorFilter.isFiltered(node.getModelNode().getContent())) {
				nodesToFilter.add(node);
			}
		}

		for (final INodeView<IPhysicalOperator> nodeToFilter : nodesToFilter) {
			graphView.removeViewedNode(nodeToFilter);
		}

		return graphView;
	}

	@Override
	public void updateGraphDisplay(IGraphModel<IPhysicalOperator> graph, ISymbolConfiguration symbolConfig, ISymbolElementFactory<IPhysicalOperator> symbolFactory,
			IGraphView<IPhysicalOperator> graphDisplay) {
		Objects.requireNonNull(graph, "Graph must not be null!");
		Objects.requireNonNull(symbolConfig, "symbolConfig must not be null!");
		Objects.requireNonNull(symbolFactory, "symbolFactory must not be null!");
		Objects.requireNonNull(graphDisplay, "graphDisplay must not be null!");
		
		logger.info("Updating GraphDisplay");

		// Schauen, ob Knoten weggefallen sind
		for (final IOdysseusNodeView nodeDisplay : graphDisplay.getViewedNodes().toArray(new IOdysseusNodeView[0])) {
			final INodeModel<IPhysicalOperator> nodeModel = nodeDisplay.getModelNode();
			if (!graph.getNodes().contains(nodeModel)) {
				graphDisplay.removeViewedNode(nodeDisplay);
			}
		}

		// Schauen, ob neue Knoten hinzugefügt worden sind
		final Map<String, Collection<SymbolElementInfo>> map = symbolConfig.getMap();
		for (final INodeModel<IPhysicalOperator> nodeModel : graph.getNodes()) {
			final Collection<INodeView<IPhysicalOperator>> nodeDisplays = findNodeDisplay(graphDisplay, nodeModel);
			if (nodeDisplays.isEmpty()) {
				graphDisplay.insertViewedNode(createNodeDisplay(nodeModel, symbolFactory, symbolConfig));
			}
		}

		// Schauen, ob alte Verbindungen wegfallen
		for (final IConnectionView<IPhysicalOperator> conn : graphDisplay.getViewedConnections()) {
			if (!graph.getConnections().contains(conn.getModelConnection())) {
				graphDisplay.removeViewedConnection(conn);
			}
		}

		// Schauen, ob neue Verbindungen hinzugekommen sind
		for (final IConnectionModel<IPhysicalOperator> connModel : graph.getConnections()) {
			if (findConnectionDisplay(graphDisplay, connModel).isEmpty()) {

				final INodeView<IPhysicalOperator> startNode = findFirstNodeDisplay(graphDisplay, connModel.getStartNode());
				final INodeView<IPhysicalOperator> endNode = findFirstNodeDisplay(graphDisplay, connModel.getEndNode());

				if (startNode == null) {
					logger.error("StartNode f�r die Verbindung " + connModel.toString() + " nicht gefunden");
					continue;
				}
				if (endNode == null) {
					logger.error("EndNode f�r die Verbindung " + connModel.toString() + " nicht gefunden");
					continue;
				}

				final DefaultConnectionView<IPhysicalOperator> connView = new OdysseusConnectionView(connModel, startNode, endNode);

				createConnectionSymbol(connView, symbolFactory);
				graphDisplay.insertViewedConnection(connView);
			}
		}

		// Die Konfiguration kann sich geändert haben...
		// daher alle Symbole neu erstellen
		// sicherheitshalber
		for (final INodeView<IPhysicalOperator> nodeView : graphDisplay.getViewedNodes()) {
			if (nodeView.getModelNode() != null) {
				final Collection<SymbolElementInfo> symbols = map.get(nodeView.getModelNode().getName());
				if (symbols != null) {
					createSymbols(nodeView, symbols, symbolFactory);
				} else {
					final Collection<SymbolElementInfo> defaultSymbolInfos = symbolConfig.getDefaultSymbolInfos();
					if (defaultSymbolInfos == null || defaultSymbolInfos.size() == 0) {
						logger.warn("No default node for '" + nodeView.getModelNode().getName() + "' specified. Use internal standard.");
					}

					createSymbols(nodeView, defaultSymbolInfos, symbolFactory);
				}
			}
		}

		logger.info("GraphDisplay updated");
	}

	private static void createConnectionSymbol(IConnectionView<IPhysicalOperator> connView, ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		final IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection("Arrow");
		ele.setConnectionView(connView);
		connView.getSymbolContainer().add(ele);
	}

	private static INodeView<IPhysicalOperator> createNodeDisplay(INodeModel<IPhysicalOperator> node, ISymbolElementFactory<IPhysicalOperator> symbolFactory, ISymbolConfiguration symbolConfig) {
		final OdysseusNodeView nodeDisplay = new OdysseusNodeView((IOdysseusNodeModel) node);

		// Typ ermitteln
		final String nodeType = node.getName();
		// Symbole erstellen
		final Map<String, Collection<SymbolElementInfo>> map = symbolConfig.getMap();
		final Collection<SymbolElementInfo> symbols = map.get(nodeType);
		if (symbols != null) {
			createSymbols(nodeDisplay, symbols, symbolFactory);
		} else {
			final Collection<SymbolElementInfo> defaultSymbolInfos = symbolConfig.getDefaultSymbolInfos();
			if (defaultSymbolInfos == null || defaultSymbolInfos.size() == 0) {
				logger.warn("No default node for '" + nodeType + "' specified. Use internal standard.");
			}

			createSymbols(nodeDisplay, defaultSymbolInfos, symbolFactory);

		}
		return nodeDisplay;
	}

	private static void createSymbols(INodeView<IPhysicalOperator> nodeView, Collection<SymbolElementInfo> symbolInfos, ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		if (nodeView == null) {
			return;
		}
		if (symbolFactory == null) {
			return;
		}

		nodeView.getSymbolContainer().clear();

		if (symbolInfos != null && symbolInfos.size() > 0) {
			for (final SymbolElementInfo info : symbolInfos) {

				final ISymbolElement<IPhysicalOperator> ele = symbolFactory.createForNode(info);
				ele.setNodeView(nodeView);

				nodeView.getSymbolContainer().add(ele);
				nodeView.setWidth(info.getWidth());
				nodeView.setHeight(info.getHeight());
			}
		} else {
			final ISymbolElement<IPhysicalOperator> ele = symbolFactory.createDefaultSymbolElement();
			ele.setNodeView(nodeView);
			nodeView.getSymbolContainer().add(ele);
		}
	}

	private static Collection<IConnectionView<IPhysicalOperator>> findConnectionDisplay(IGraphView<IPhysicalOperator> graphDisplay, IConnectionModel<IPhysicalOperator> conn) {
		final Collection<IConnectionView<IPhysicalOperator>> found = new ArrayList<IConnectionView<IPhysicalOperator>>();
		for (final IConnectionView<IPhysicalOperator> connDisplay : graphDisplay.getViewedConnections()) {
			if (connDisplay.getModelConnection() == conn) {
				found.add(connDisplay);
			}
		}
		return found;
	}

	@SuppressWarnings("unchecked")
	private static INodeView<IPhysicalOperator> findFirstNodeDisplay(IGraphView<IPhysicalOperator> graphDisplay, INodeModel<IPhysicalOperator> node) {
		final Collection<INodeView<IPhysicalOperator>> found = findNodeDisplay(graphDisplay, node);
		if (found.isEmpty()) {
			return null;
		}
		return found.toArray(new INodeView[1])[0];
	}

	private static Collection<INodeView<IPhysicalOperator>> findNodeDisplay(IGraphView<IPhysicalOperator> graphDisplay, INodeModel<IPhysicalOperator> node) {
		final Collection<INodeView<IPhysicalOperator>> found = new ArrayList<INodeView<IPhysicalOperator>>();
		for (final INodeView<IPhysicalOperator> nodeDisplay : graphDisplay.getViewedNodes()) {
			if (nodeDisplay.getModelNode() == node) {
				found.add(nodeDisplay);
			}
		}
		return found;
	}
}
