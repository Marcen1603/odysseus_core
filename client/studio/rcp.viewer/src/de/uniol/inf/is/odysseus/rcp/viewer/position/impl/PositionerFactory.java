package de.uniol.inf.is.odysseus.rcp.viewer.position.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.position.INodePositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;

public class PositionerFactory {

	public static final String HORIZONTAL_SUGIYAMA = "horizontal";
	public static final String CONFIG_KEY_LAYOUT = "node.layout";

	public static INodePositioner<IPhysicalOperator> newInstance(
			ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		INodePositioner<IPhysicalOperator> positioner = null;
		
		String positionerName = OdysseusRCPConfiguration.get(CONFIG_KEY_LAYOUT, "");

		if (positionerName.toLowerCase().equals((HORIZONTAL_SUGIYAMA))) {
			positioner = new HorizontalSugiyamaPositioner(symbolFactory);
		} else {
			positioner = new SugiyamaPositioner(symbolFactory);
		}

		return positioner;
	}

}
