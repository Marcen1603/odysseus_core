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

public final class HorizontalSugiyamaPositioner extends AbstractSugiyamaPositioner {

	private static final String HORIZONTAL_NODE_DISTANCE_CFG_KEY = "layout.horizontal.distance.x";
	private static final String VERTICAL_NODE_DISTANCE_CFG_KEY = "layout.horizontal.distance.y";

	private static final int SPACE_WIDTH_PIXELS = Integer.parseInt(OdysseusRCPConfiguration.get(HORIZONTAL_NODE_DISTANCE_CFG_KEY, "50"));
	private static final int SPACE_HEIGHT_PIXELS = Integer.parseInt(OdysseusRCPConfiguration.get(VERTICAL_NODE_DISTANCE_CFG_KEY, "50"));
	
	private static final Logger logger = LoggerFactory.getLogger(HorizontalSugiyamaPositioner.class);
	
	public HorizontalSugiyamaPositioner(ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		super(symbolFactory);
	}

	@Override
	protected void setNodePositions(List<List<INodeView<IPhysicalOperator>>> layers) {

		// init arrays for position on Y axis 
		int maxLayerSize = 0;

		final int[][] posY = new int[layers.size()][];
		for (int i = 0; i < layers.size(); i++) {
			posY[i] = new int[layers.get(i).size()];
			maxLayerSize = Math.max(maxLayerSize, layers.get(i).size());
		}

		// calculate maximum node height and width per layer / orthogonal
		final int[] maxWidths = new int[layers.size()];
		final int[] maxHeights = new int[maxLayerSize];

		for (int i = 0; i < layers.size(); i++) {
			for (int k = 0; k < layers.get(i).size(); k++) {
				maxWidths[i] = Math.max(maxWidths[i], layers.get(i).get(k).getWidth());
				maxHeights[k] = Math.max(maxHeights[k], layers.get(i).get(k).getHeight());
			}
		}

		// calculate node position on Y axis depending on the node height and the maximum node height on the the same level
		for (int layer = 0; layer < layers.size(); layer++) {

			int offsetY = 0;
			for (int index = 0; index < layers.get(layer).size(); index++) {
				final INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);

				posY[layer][index] = 0;

				if (layer > 0) {
					final int med = getMedian(currNode, layers.get(layer - 1), true);
					posY[layer][index] = calcOffsetY(maxHeights, med) + (maxHeights[med] - currNode.getHeight()) / 2;
				}

				if (posY[layer][index] < offsetY + (maxHeights[index] - currNode.getHeight()) / 2) {
					posY[layer][index] = offsetY + (maxHeights[index] - currNode.getHeight()) / 2;
				}

				offsetY += maxHeights[index] + SPACE_HEIGHT_PIXELS;
			}
		}
		
		// Calculate x position depending on node width and maximum node width of the layer. Set x and y positions of nodes.
		logger.debug("Final NodeDisplay positions");
		int offsetX = 0;
		for (int layer = 0; layer < layers.size(); layer++) {
			for (int index = layers.get(layer).size() - 1; index >= 0; index--) {
				final int posX = offsetX + (maxWidths[layer] - layers.get(layer).get(index).getWidth()) / 2;
				INodeView<IPhysicalOperator> currNode = layers.get(layer).get(index);
				currNode.setPosition(new Vector(posX, posY[layer][index]));
			}
			offsetX += maxWidths[layer] + SPACE_WIDTH_PIXELS;
		}

	}

	private int calcOffsetY(int[] maxHeights, int index) {
		int offsetY = 0;
		for (int i = 0; i < index; i++) {
			offsetY += maxHeights[i] + SPACE_HEIGHT_PIXELS;
		}
		return offsetY;
	}
}
