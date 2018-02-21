package de.uniol.inf.is.odysseus.rcp.viewer.position.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.position.INodePositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;

public class PositionerFactory {

	public static final String HORIZONTAL_SUGIYAMA = "horizontal";

	public static INodePositioner<IPhysicalOperator> newInstance(String positionerName,
			ISymbolElementFactory<IPhysicalOperator> symbolFactory) {
		INodePositioner<IPhysicalOperator> positioner = null;

		if (positionerName == HORIZONTAL_SUGIYAMA) {
			positioner = new HorizontalSugiyamaPositioner(symbolFactory);
		} else {
			positioner = new SugiyamaPositioner(symbolFactory);
		}

		return positioner;
	}

}
