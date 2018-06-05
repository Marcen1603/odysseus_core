package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.RectanglePictogramFigure;

public class SquarePictogram extends AbstractShapePictogram {

	public SquarePictogram() {
		super();		
		super.setKeepRatio(true);
	}

	public SquarePictogram(SquarePictogram rp) {
		super(rp);
		super.setKeepRatio(true);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new SquarePictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new RectanglePictogramFigure();
	}

}
