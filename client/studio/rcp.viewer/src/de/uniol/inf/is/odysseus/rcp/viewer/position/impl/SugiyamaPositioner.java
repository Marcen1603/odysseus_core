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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public final class SugiyamaPositioner extends AbstractSugiyamaPositioner {

	private static final String HORIZONTAL_NODE_DISTANCE_CFG_KEY = "layout.vertical.distance.x";
	private static final String VERTICAL_NODE_DISTANCE_CFG_KEY = "layout.vertical.distance.y";

	private static final int SPACE_WIDTH_PIXELS = Integer.parseInt(OdysseusRCPConfiguration.get(HORIZONTAL_NODE_DISTANCE_CFG_KEY, "100"));
	private static final int SPACE_HEIGHT_PIXELS = Integer.parseInt(OdysseusRCPConfiguration.get(VERTICAL_NODE_DISTANCE_CFG_KEY, "75"));
	
	private static final Logger logger = LoggerFactory.getLogger(SugiyamaPositioner.class);

	public SugiyamaPositioner(ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		super(symbolFactory);
	}

	protected void setNodePositions(List<List<INodeView<IPhysicalOperator>>> layers) {
		// Initialize arrays
		// They will store the X coordinates
		final int[][] posXRight = new int[layers.size()][];
		for (int i = 0; i < layers.size(); i++) {
			posXRight[i] = new int[layers.get(i).size()];
		}
		final int[][] posXLeft = new int[layers.size()][];
		for (int i = 0; i < layers.size(); i++) {
			posXLeft[i] = new int[layers.get(i).size()];
		}

		// Disctance between two nodes
		int highestX = Integer.MIN_VALUE;
		for (int layer = 0; layer < layers.size(); layer++) {

			int lastX = 0;
			int lastWidth = 0;
			for (int index = 0; index < layers.get(layer).size(); index++) {
				final INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
				// final int currWidth = currNode.getWidth();

				posXRight[layer][index] = 0;
				if (layer > 0) {
					final int med = getMedian(currNode, layers.get(layer - 1), true);
					// final INodeView<IPhysicalOperator> parentNode =
					// layers.get(layer - 1).get(med);
					// final int parentWidth = parentNode.getWidth();

					// if (parentWidth < currWidth) {
					// posXRight[layer][index] = posXRight[layer - 1][med] -
					// (currWidth - parentWidth) / 2;
					// } else if (parentWidth > currWidth) {
					// posXRight[layer][index] = posXRight[layer - 1][med] +
					// (parentWidth - currWidth) / 2;
					// } else {
					posXRight[layer][index] = posXRight[layer - 1][med];
					// }

				}

				if (posXRight[layer][index] < lastX + lastWidth + SPACE_WIDTH_PIXELS) {
					posXRight[layer][index] = lastX + lastWidth + SPACE_WIDTH_PIXELS;
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
				// final int currWidth = currNode.getWidth();

				posXLeft[layer][index] = highestX - currNode.getWidth();
				if (layer > 0) {
					final int med = getMedian(currNode, layers.get(layer - 1), true);
					// final INodeView<IPhysicalOperator> parentNode =
					// layers.get(layer - 1).get(med);
					// final int parentWidth = parentNode.getWidth();

					// if (parentWidth < currWidth) {
					// posXLeft[layer][index] = posXLeft[layer - 1][med] -
					// (currWidth - parentWidth) / 2;
					// } else if (parentWidth > currWidth) {
					// posXLeft[layer][index] = posXLeft[layer - 1][med] +
					// (parentWidth - currWidth) / 2;
					// } else {
					posXLeft[layer][index] = posXLeft[layer - 1][med];
					// }

				}

				if (posXLeft[layer][index] + currNode.getWidth() > lastX) {
					posXLeft[layer][index] = lastX - currNode.getWidth() - SPACE_WIDTH_PIXELS;
				}

				lastX = posXLeft[layer][index] - currNode.getWidth();
			}
		}

		logger.debug("Final NodeDisplay positions");
		// for (int layer = 0; layer < layers.size(); layer++) {
		// final int posY = layers.size() * SPACE_HEIGHT_PIXELS -
		// SPACE_HEIGHT_PIXELS * (layer + 1);
		//
		// if (layer == 0 || layer > 0 && layers.get(layer).size() >
		// layers.get(layer - 1).size()) {
		//
		// for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
		// final INodeView<IPhysicalOperator> currNode =
		// layers.get(layer).get(index);
		// currNode.setPosition(new Vector((posXRight[layer][index] +
		// posXLeft[layer][index]) / 2, posY));
		// System.err.println("#1: Position of " +
		// currNode.getModelNode().getContent().getClass().getSimpleName() + "
		// set to " + currNode.getPosition());
		// }
		// } else {
		//
		// for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
		// final INodeView<IPhysicalOperator> currNode =
		// layers.get(layer).get(index);
		// final Collection<IConnectionView<IPhysicalOperator>> connectionsAsEnd
		// = currNode.getConnectionsAsEnd();
		// double sumX = 0;
		// for (final IConnectionView<IPhysicalOperator> con : connectionsAsEnd)
		// {
		// sumX += con.getViewedStartNode().getPosition().getX();
		// }
		// currNode.setPosition(new Vector(sumX / connectionsAsEnd.size(),
		// posY));
		// System.err.println("2#: Position of " +
		// currNode.getModelNode().getContent().getClass().getSimpleName() + "
		// set to " + currNode.getPosition());
		// }
		// }
		// }
		for (int layer = 0; layer < layers.size(); layer++) {
			final int posY = layers.size() * SPACE_HEIGHT_PIXELS - SPACE_HEIGHT_PIXELS * (layer + 1);
			for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
				INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
				currNode.setPosition(new Vector(posXRight[layer][index], posY));
			}
		}
	}

}
