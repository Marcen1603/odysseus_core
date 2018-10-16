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
package de.uniol.inf.is.odysseus.rcp.viewer.position.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.position.INodePositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementInfo;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusNodeView;

public abstract class AbstractSugiyamaPositioner implements INodePositioner<IPhysicalOperator> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractSugiyamaPositioner.class);
	
	private static final int INVISIBLE_NODE_WIDTH_PIXELS = 160;
	private static final int INVISIBLE_NODE_HEIGHT_PIXELS = 27;
	private static final SymbolElementInfo DUMMY_SYMBOL_INFO = new SymbolElementInfo("invisible", null, 5, 5);

	private final ISymbolElementFactory<IPhysicalOperator> symbolFactory;

	public AbstractSugiyamaPositioner(ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		if (symbolFactory == null) {
			throw new IllegalArgumentException("symbolFactory is null");
		}

		this.symbolFactory = symbolFactory;
	}

	@Override
	public void positionize(IGraphView<IPhysicalOperator> graph, int width, int height) {
		// Preconditions.checkNotNull(graph, "Graph to positionize must not be null!");

		/** PHASE 1 **/
		// Find sources (nodes having 0 inputs).
		// Also determine level of nodes and their class of equivalance.
		final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels = determineNodeLevels(graph);
		final int maxLevel = determineMaxLevel(nodeLevels);

		// Create DummyNodes if an edge crosses more than two levels
		insertInvisibleNodes(graph, nodeLevels, symbolFactory);

		/** PHASE 2 **/
		// Minimize crossings by rearranging levels.
		// Determine number of nodes per level and assign to layer.
		List<List<INodeView<IPhysicalOperator>>> layers = determineLayers(nodeLevels, maxLevel);
		layerByLayerSweep(layers);

		/** PHASE 3 **/
		// Determine the position (pixel) of nodes on screen
		setNodePositions(layers);
		
	}

	/**
	 * Calculates and sets the position of all nodes (in pixel) 
	 * @param layers List containing list of nodes each representing a layer
	 */
	protected abstract void setNodePositions(List<List<INodeView<IPhysicalOperator>>> layers);

	private static void layerByLayerSweep(List<List<INodeView<IPhysicalOperator>>> layers) {
		final int maxCycles = 2;
		int currentCycle = 0;
		boolean changed = true;

		while (changed && currentCycle < maxCycles) {
			changed = false;

			// ************** from top to bottom
			for (int i = 0; i < layers.size() - 1; i++) {
				final int fixedLayer = i; // Readability...
				final int varLayer = i + 1;

				// Determine median values of nodes of the current layer
				final ArrayList<Integer> medians = new ArrayList<Integer>();
				for (int e = 0; e < layers.get(varLayer).size(); e++) {

					// Determine median of subsequent nodes
					final int med = getMedian(layers.get(varLayer).get(e), layers.get(fixedLayer), true);

					medians.add(med);
				}

				changed = sortByMedians(layers.get(varLayer), medians) || changed;
			}

			// ****************** from bottom to top
			for (int i = layers.size() - 1; i > 0; i--) {
				final int fixedLayer = i; // Lesbarkeit...
				final int varLayer = i - 1;

				// Determine median values of nodes of the current layer
				final ArrayList<Integer> medians = new ArrayList<Integer>();
				for (int e = 0; e < layers.get(varLayer).size(); e++) {

					// Determine median of subsequent nodes
					final int med = getMedian(layers.get(varLayer).get(e), layers.get(fixedLayer), false);

					medians.add(med);
				}

				changed = sortByMedians(layers.get(varLayer), medians) || changed;

			}
			currentCycle++;
		}
	}

	private static List<List<INodeView<IPhysicalOperator>>> determineLayers(
			final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels, final int maxLevel) {
		List<List<INodeView<IPhysicalOperator>>> layers = Lists.newArrayList();
		for (int i = 0; i < maxLevel + 1; i++) {
			layers.add(Lists.<INodeView<IPhysicalOperator>>newArrayList());
		}

		for (final INodeView<IPhysicalOperator> node : nodeLevels.keySet()) {
			final int level = nodeLevels.get(node);
			layers.get(level).add(node);
		}
		return layers;
	}

	private static void insertInvisibleNodes(IGraphView<IPhysicalOperator> graph,
			final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels,
			ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		List<IConnectionView<IPhysicalOperator>> connections = Lists.newArrayList(graph.getViewedConnections());
		for (final IConnectionView<IPhysicalOperator> conn : connections) {
			final INodeView<IPhysicalOperator> start = conn.getViewedStartNode();
			final INodeView<IPhysicalOperator> end = conn.getViewedEndNode();

			final int startLevel = nodeLevels.get(start);
			final int endLevel = nodeLevels.get(end);

			if (endLevel > startLevel + 1) {

				INodeView<IPhysicalOperator> startNode = start;
				for (int currentLevel = startLevel + 1; currentLevel < endLevel; currentLevel++) {

					// Create invisible dummy nodes
					final INodeView<IPhysicalOperator> dummyNode = new OdysseusNodeView();
					dummyNode.setWidth(INVISIBLE_NODE_WIDTH_PIXELS);
					dummyNode.setHeight(INVISIBLE_NODE_HEIGHT_PIXELS);
					dummyNode.getSymbolContainer().add(symbolFactory.createForNode(DUMMY_SYMBOL_INFO));
					graph.insertViewedNode(dummyNode);
					nodeLevels.put(dummyNode, currentLevel);

					// Connection without arc
					final IConnectionView<IPhysicalOperator> dummyConnection = new OdysseusConnectionView(
							conn.getModelConnection(), startNode, dummyNode);
					final IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection("Normal");
					ele.setConnectionView(dummyConnection);
					dummyConnection.getSymbolContainer().add(ele);
					graph.insertViewedConnection(dummyConnection);
					startNode = dummyNode;
				}

				// Create last connection (with arc)
				final IConnectionView<IPhysicalOperator> dummyConnection = new OdysseusConnectionView(
						conn.getModelConnection(), startNode, end);
				final IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection("Arrow");
				ele.setConnectionView(dummyConnection);
				dummyConnection.getSymbolContainer().add(ele);
				graph.insertViewedConnection(dummyConnection);

				graph.insertViewedConnection(new OdysseusConnectionView(conn.getModelConnection(), startNode, end));
				graph.removeViewedConnection(conn);
			}
		}
	}

	private static int determineMaxLevel(Map<INodeView<IPhysicalOperator>, Integer> nodeLevels) {
		int max = Integer.MIN_VALUE;
		for (final INodeView<IPhysicalOperator> nodeView : nodeLevels.keySet()) {
			max = Math.max(max, nodeLevels.get(nodeView));
		}
		return max;
	}

	private static Map<INodeView<IPhysicalOperator>, Integer> determineNodeLevels(IGraphView<IPhysicalOperator> graph) {
		final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels = Maps.newHashMap();

		// get sources and define them with level = 0
		List<INodeView<IPhysicalOperator>> nodesToVisit = Lists.newArrayList(graph.getViewedNodes());
		for (final INodeView<IPhysicalOperator> node : graph.getViewedNodes()) {
			if (node.getConnectionsAsEnd().size() == 0) {
				nodeLevels.put(node, 0);
				nodesToVisit.remove(node);
			}
		}

		while (!nodesToVisit.isEmpty()) {
			INodeView<IPhysicalOperator> node = nodesToVisit.remove(0);

			int level = determineLevel(node, nodeLevels, Lists.<INodeView<IPhysicalOperator>>newArrayList());
			nodeLevels.put(node, level);
		}

		return nodeLevels;
	}

	private static int determineLevel(INodeView<IPhysicalOperator> node,
			Map<INodeView<IPhysicalOperator>, Integer> nodeLevels, List<INodeView<IPhysicalOperator>> visitedNodes) {
		if (nodeLevels.containsKey(node)) {
			return nodeLevels.get(node);
		}

		if (visitedNodes.contains(node)) {
			return -1;
		}
		visitedNodes.add(node);

		int maxLevel = Integer.MIN_VALUE;
		for (IConnectionView<IPhysicalOperator> connection : node.getConnectionsAsEnd()) {
			maxLevel = Math.max(maxLevel, determineLevel(connection.getViewedStartNode(), nodeLevels, visitedNodes));
		}

		visitedNodes.remove(node);

		return maxLevel + 1;
	}

	protected static int getMedian(INodeView<IPhysicalOperator> node, List<INodeView<IPhysicalOperator>> fixedLayerNodes,
			boolean fromTop) {
		// get subsequnt nodes
		final ArrayList<INodeView<IPhysicalOperator>> neighbours = new ArrayList<INodeView<IPhysicalOperator>>();
		final Collection<? extends IConnectionView<IPhysicalOperator>> conns = (fromTop == true)
				? node.getConnectionsAsEnd() : node.getConnectionsAsStart();
		for (final IConnectionView<IPhysicalOperator> conn : conns) {
			neighbours.add((fromTop == true) ? conn.getViewedStartNode() : conn.getViewedEndNode());
		}

		// Determine median of subsequent nodes
		final int med;
		if (!neighbours.isEmpty()) {

			final ArrayList<Integer> indexes = new ArrayList<Integer>();
			for (int p = 0; p < neighbours.size(); p++) {
				final int pos = fixedLayerNodes.indexOf(neighbours.get(p));
				if (pos != -1) {
					indexes.add(pos);
				}
			}

			Collections.sort(indexes);
			if (indexes.isEmpty()) {
				med = 0;
			} else {
				med = indexes.get((indexes.size() - 1) / 2);
			}
		} else {
			med = 0;
		}
		return med;
	}

	private static boolean sortByMedians(List<INodeView<IPhysicalOperator>> nodes, ArrayList<Integer> medians) {
		if (nodes.size() != medians.size()) {
			if (logger.isWarnEnabled()) {
				logger.warn("nodes and medians arrays are not in the same size!");
			}
			return false;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("sort nodes by their medians");
		}

		boolean changed = false;
		// Sort nodes of current layer by median
		for (int p = 0; p < medians.size(); p++) {
			for (int q = 0; q < medians.size() - 1; q++) {
				if (medians.get(q) > medians.get(q + 1)) {
					// switch positions
					final int tmp = medians.get(q);
					medians.set(q, medians.get(q + 1));
					medians.set(q + 1, tmp);
					final INodeView<IPhysicalOperator> tmpDisplay = nodes.get(q);
					nodes.set(q, nodes.get(q + 1));
					nodes.set(q + 1, tmpDisplay);
					changed = true;
				} else if (medians.get(q) == medians.get(q + 1)) {
					if (nodes.get(q).getConnectionsAsEnd().size() % 2 == 1
							&& nodes.get(q + 1).getConnectionsAsEnd().size() % 2 == 0) {
						// switch positions
						final int tmp = medians.get(q);
						medians.set(q, medians.get(q + 1));
						medians.set(q + 1, tmp);
						final INodeView<IPhysicalOperator> tmpDisplay = nodes.get(q);
						nodes.set(q, nodes.get(q + 1));
						nodes.set(q + 1, tmpDisplay);
						changed = true;
					}
				}
			}
		}

		return changed;

	}
}
