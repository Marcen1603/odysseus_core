package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.CirclePictogramFigure;

public class CirclePictogram extends AbstractShapePictogram {

	public CirclePictogram() {
		super();
		super.setKeepRatio(true);
	}

	public CirclePictogram(CirclePictogram rp) {
		super(rp);
		super.setKeepRatio(true);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new CirclePictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new CirclePictogramFigure();
	}

}
