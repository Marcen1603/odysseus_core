package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.PentagonPictogramFigure;

public class PentagonPictogram extends AbstractShapePictogram {

	public PentagonPictogram() {
		super();		
		super.setKeepRatio(false);
	}

	public PentagonPictogram(PentagonPictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new PentagonPictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new PentagonPictogramFigure();
	}

}
