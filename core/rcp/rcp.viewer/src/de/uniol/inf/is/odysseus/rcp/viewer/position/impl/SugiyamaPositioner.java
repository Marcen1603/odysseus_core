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
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.impl.OdysseusNodeView;

public final class SugiyamaPositioner implements INodePositioner<IPhysicalOperator> {

	private static final int INVISIBLE_NODE_SIZE_PIXELS = 10;

	private static final Logger logger = LoggerFactory.getLogger(SugiyamaPositioner.class);

	private static final int SPACE_PIXELS = 20; 
	private static final int SPACE_HEIGHT_PIXELS = 75; 
	private static final SymbolElementInfo DUMMY_SYMBOL_INFO = new SymbolElementInfo("invisible", null, 5, 5);
	
	private final ISymbolElementFactory<IPhysicalOperator> symbolFactory;

	public SugiyamaPositioner(ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		if (symbolFactory == null) {
			throw new IllegalArgumentException("symbolFactory is null");
		}

		this.symbolFactory = symbolFactory;
	}

	@Override
	public void positionize(IGraphView<IPhysicalOperator> graph, int width, int height) {
		Preconditions.checkNotNull(graph, "Graph to positionize must not be null!");

		/** PHASE 1 **/
		// Quellen finden.. diese haben einen Eingangsgrad von 0
		// Und Ebene der Knoten ermitteln und die Äquivalenzklassen/Ebene
		// ermitteln
		final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels = determineNodeLevels(graph);
		final int maxLevel = determineMaxLevel(nodeLevels);

		// DummyNodes erzeugen, wenn eine Kante über mehr als zwei benachbarte
		// Ebenen verl�uft
		insertInvisibleNodes(graph, nodeLevels, symbolFactory);

		/** PHASE 2 **/
		// Knoten der Ebenen arrangieren, sodass möglichst wenige Kreuzungen
		// vorkommen. Zahl der Knoten pro Ebene ermitteln und Knoten zuordnen
		List<List<INodeView<IPhysicalOperator>>> layers = determineLayers(nodeLevels, maxLevel);

		// layer-by-layer sweep
		final int maxCycles = 2;
		int currentCycle = 0;
		boolean changed = true;

		while (changed && currentCycle < maxCycles) {
			changed = false;

			// ************** von oben nach unten
			for (int i = 0; i < layers.size() - 1; i++) {
				final int fixedLayer = i; // Lesbarkeit...
				final int varLayer = i + 1;

				// Median-Werte der Knoten der aktuellen Ebene bestimmen
				final ArrayList<Integer> medians = new ArrayList<Integer>();
				for (int e = 0; e < layers.get(varLayer).size(); e++) {

					// Median der Nachfolgerknoten bestimmen
					final int med = getMedian(layers.get(varLayer).get(e), layers.get(fixedLayer), true);

					medians.add(med);
				}

				changed = sortByMedians(layers.get(varLayer), medians) || changed;
			}

			// ****************** und von unten nach oben
			for (int i = layers.size() - 1; i > 0; i--) {
				final int fixedLayer = i; // Lesbarkeit...
				final int varLayer = i - 1;

				// Median-Werte der Knoten der aktuellen Ebene bestimmen
				final ArrayList<Integer> medians = new ArrayList<Integer>();
				for (int e = 0; e < layers.get(varLayer).size(); e++) {

					// Median der Nachfolgerknoten bestimmen
					final int med = getMedian(layers.get(varLayer).get(e), layers.get(fixedLayer), false);

					medians.add(med);
				}

				changed = sortByMedians(layers.get(varLayer), medians) || changed;

			}
			currentCycle++;
			if (changed == false) {
				logger.trace("Nothing changed. layer-to-layer sweep finished!");
			}
		}

		/** PHASE 3 **/
		logger.debug("Phase 3: Calculate x- and y-Coordinates");

		// Arrays initialisieren
		// Diese werden die X-Koordinaten beherbergen
		final int[][] posXRight = new int[layers.size()][];
		for (int i = 0; i < layers.size(); i++) {
			posXRight[i] = new int[layers.get(i).size()];
		}
		final int[][] posXLeft = new int[layers.size()][];
		for (int i = 0; i < layers.size(); i++) {
			posXLeft[i] = new int[layers.get(i).size()];
		}

		// Abstand zwischen zwei Knoten
		int highestX = Integer.MIN_VALUE;
		for (int layer = 0; layer < layers.size(); layer++) {

			int lastX = 0;
			int lastWidth = 0;
			for (int index = 0; index < layers.get(layer).size(); index++) {
				final INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
				final int currWidth = currNode.getWidth();

				posXRight[layer][index] = 0;
				if (layer > 0) {
					final int med = getMedian(currNode, layers.get(layer - 1), true);
					final INodeView<IPhysicalOperator> parentNode = layers.get(layer - 1).get(med);
					final int parentWidth = parentNode.getWidth();

					if (parentWidth < currWidth) {
						posXRight[layer][index] = posXRight[layer - 1][med] - (currWidth - parentWidth) / 2;
					} else if (parentWidth > currWidth) {
						posXRight[layer][index] = posXRight[layer - 1][med] + (parentWidth - currWidth) / 2;
					} else {
						posXRight[layer][index] = posXRight[layer - 1][med];
					}

				}

				if (posXRight[layer][index] < lastX + lastWidth + SPACE_PIXELS) {
					posXRight[layer][index] = lastX + lastWidth + SPACE_PIXELS;
				}

				lastX = posXRight[layer][index];
				lastWidth = currNode.getWidth();
				if (highestX < lastX + lastWidth) {
					highestX = lastX + lastWidth;
				}
			}
		}

		for (int layer = 0; layer < layers.size(); layer++) {

			int lastX = highestX;
			for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
				final INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
				final int currWidth = currNode.getWidth();

				posXLeft[layer][index] = highestX - currNode.getWidth();
				if (layer > 0) {
					final int med = getMedian(currNode, layers.get(layer - 1), true);
					final INodeView<IPhysicalOperator> parentNode = layers.get(layer - 1).get(med);
					final int parentWidth = parentNode.getWidth();

					if (parentWidth < currWidth) {
						posXLeft[layer][index] = posXLeft[layer - 1][med] - (currWidth - parentWidth) / 2;
					} else if (parentWidth > currWidth) {
						posXLeft[layer][index] = posXLeft[layer - 1][med] + (parentWidth - currWidth) / 2;
					} else {
						posXLeft[layer][index] = posXLeft[layer - 1][med];
					}

				}

				if (posXLeft[layer][index] + currNode.getWidth() > lastX) {
					posXLeft[layer][index] = lastX - currNode.getWidth() - SPACE_PIXELS;
				}

				lastX = posXLeft[layer][index] - currNode.getWidth();
			}
		}

		logger.debug("Final NodeDisplay positions");
		for (int layer = 0; layer < layers.size(); layer++) {
			final int posY = layers.size() * SPACE_HEIGHT_PIXELS - SPACE_HEIGHT_PIXELS * (layer + 1);

			if (layer == 0 || layer > 0 && layers.get(layer).size() > layers.get(layer - 1).size()) {

				for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
					final INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
					currNode.setPosition(new Vector((posXRight[layer][index] + posXLeft[layer][index]) / 2, posY));
				}
			} else {

				for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
					final INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
					final Collection<IConnectionView<IPhysicalOperator>> connectionsAsEnd = currNode.getConnectionsAsEnd();
					double sumX = 0;
					for (final IConnectionView<IPhysicalOperator> con : connectionsAsEnd) {
						sumX += con.getViewedStartNode().getPosition().getX();
					}
					currNode.setPosition(new Vector(sumX / connectionsAsEnd.size(), posY));
				}
			}
		}
	}

	private static List<List<INodeView<IPhysicalOperator>>> determineLayers(final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels, final int maxLevel) {
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

	private static void insertInvisibleNodes(IGraphView<IPhysicalOperator> graph, final Map<INodeView<IPhysicalOperator>, Integer> nodeLevels, ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		List<IConnectionView<IPhysicalOperator>> connections = Lists.newArrayList(graph.getViewedConnections());
		for (final IConnectionView<IPhysicalOperator> conn : connections) {
			final INodeView<IPhysicalOperator> start = conn.getViewedStartNode();
			final INodeView<IPhysicalOperator> end = conn.getViewedEndNode();

			final int startLevel = nodeLevels.get(start);
			final int endLevel = nodeLevels.get(end);

			if (endLevel > startLevel + 1) {

				INodeView<IPhysicalOperator> startNode = start;
				for (int currentLevel = startLevel + 1; currentLevel < endLevel; currentLevel++) {

					// unsichtbaren Knoten erzeugen
					final INodeView<IPhysicalOperator> dummyNode = new OdysseusNodeView();
					dummyNode.setWidth(INVISIBLE_NODE_SIZE_PIXELS);
					dummyNode.setHeight(INVISIBLE_NODE_SIZE_PIXELS);
					dummyNode.getSymbolContainer().add(symbolFactory.createForNode(DUMMY_SYMBOL_INFO));
					graph.insertViewedNode(dummyNode);
					nodeLevels.put(dummyNode, currentLevel);

					// Verbindung ohne Pfeil
					final IConnectionView<IPhysicalOperator> dummyConnection = new OdysseusConnectionView(conn.getModelConnection(), startNode, dummyNode);
					final IConnectionSymbolElement<IPhysicalOperator> ele = symbolFactory.createForConnection("Normal");
					ele.setConnectionView(dummyConnection);
					dummyConnection.getSymbolContainer().add(ele);
					graph.insertViewedConnection(dummyConnection);
					startNode = dummyNode;
				}

				// Letzte Verbindung anlegen (mit Pfeil)
				final IConnectionView<IPhysicalOperator> dummyConnection = new OdysseusConnectionView(conn.getModelConnection(), startNode, end);
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
		
		while(!nodesToVisit.isEmpty()) {
			INodeView<IPhysicalOperator> node = nodesToVisit.remove(0);
			
			int level = determineLevel(node, nodeLevels, Lists.<INodeView<IPhysicalOperator>>newArrayList());
			nodeLevels.put(node, level);
		}

		return nodeLevels;
	}

	private static int determineLevel(INodeView<IPhysicalOperator> node, Map<INodeView<IPhysicalOperator>, Integer> nodeLevels, List<INodeView<IPhysicalOperator>> visitedNodes) {
		if( nodeLevels.containsKey(node)) {
			return nodeLevels.get(node);
		}
		
		if( visitedNodes.contains(node)) {
			return -1;
		}
		visitedNodes.add(node);
		
		int maxLevel = Integer.MIN_VALUE;
		for(IConnectionView<IPhysicalOperator> connection: node.getConnectionsAsEnd()) {
			maxLevel = Math.max(maxLevel, determineLevel(connection.getViewedStartNode(), nodeLevels, visitedNodes));
		}
		
		visitedNodes.remove(node);
		
		return maxLevel + 1;
	}

	private static int getMedian(INodeView<IPhysicalOperator> node, List<INodeView<IPhysicalOperator>> fixedLayerNodes, boolean fromTop) {
		// Nachfolgerknoten holen
		final ArrayList<INodeView<IPhysicalOperator>> neighbours = new ArrayList<INodeView<IPhysicalOperator>>();
		final Collection<? extends IConnectionView<IPhysicalOperator>> conns = (fromTop == true) ? node.getConnectionsAsEnd() : node.getConnectionsAsStart();
		for (final IConnectionView<IPhysicalOperator> conn : conns) {
			neighbours.add((fromTop == true) ? conn.getViewedStartNode() : conn.getViewedEndNode());
		}

		// Median der Nachfolgerknoten bestimmen
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
				med = indexes.get(indexes.size() / 2);
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
		// Knoten der aktuellen Ebene nach dem Median sortieren...
		for (int p = 0; p < medians.size(); p++) {
			for (int q = 0; q < medians.size() - 1; q++) {
				if (medians.get(q) > medians.get(q + 1)) {
					// Positionen tauschen
					final int tmp = medians.get(q);
					medians.set(q, medians.get(q + 1));
					medians.set(q + 1, tmp);
					final INodeView<IPhysicalOperator> tmpDisplay = nodes.get(q);
					nodes.set(q, nodes.get(q + 1));
					nodes.set(q + 1, tmpDisplay);
					changed = true;
				} else if (medians.get(q) == medians.get(q + 1)) {
					if (nodes.get(q).getConnectionsAsEnd().size() % 2 == 1 && nodes.get(q + 1).getConnectionsAsEnd().size() % 2 == 0) {
						// Positionen tauschen
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
