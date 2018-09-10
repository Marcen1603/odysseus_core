package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.CirclePictogramFigure;

public class EllipsePictogram extends AbstractShapePictogram {

	public EllipsePictogram() {
		super();
		super.setKeepRatio(false);
	}

	public EllipsePictogram(EllipsePictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new EllipsePictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new CirclePictogramFigure();
	}

}
