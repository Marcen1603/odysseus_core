/********************************************************************************** 
 * Copyright 2018 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.position.INodePositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;

public class PositionerFactory {

	public static final String HORIZONTAL_SUGIYAMA = "horizontal";
	public static final String VERTICAL_SUGIYAMA = "vertical";
	public static final String CONFIG_KEY_LAYOUT = "layout.direction";

	public static INodePositioner<IPhysicalOperator> newInstance(
			ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		INodePositioner<IPhysicalOperator> positioner = null;
		
		String positionerName = OdysseusRCPConfiguration.get(CONFIG_KEY_LAYOUT, HORIZONTAL_SUGIYAMA);

		if (positionerName.toLowerCase().equals((HORIZONTAL_SUGIYAMA))) {
			positioner = new HorizontalSugiyamaPositioner(symbolFactory);
		} else {
			positioner = new SugiyamaPositioner(symbolFactory);
		}

		return positioner;
	}

}
