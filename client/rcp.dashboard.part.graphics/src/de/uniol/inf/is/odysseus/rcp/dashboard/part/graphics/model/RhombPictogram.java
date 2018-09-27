package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.RhombPictogramFigure;

public class RhombPictogram extends AbstractShapePictogram {

	public RhombPictogram() {
		super();		
		super.setKeepRatio(false);
	}

	public RhombPictogram(RhombPictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new RhombPictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new RhombPictogramFigure();
	}

}
