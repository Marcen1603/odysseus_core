package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.HexagonPictogramFigure;

public class HexagonPictogram extends AbstractShapePictogram {

	public HexagonPictogram() {
		super();		
		super.setKeepRatio(false);
	}

	public HexagonPictogram(HexagonPictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new HexagonPictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new HexagonPictogramFigure();
	}

}
