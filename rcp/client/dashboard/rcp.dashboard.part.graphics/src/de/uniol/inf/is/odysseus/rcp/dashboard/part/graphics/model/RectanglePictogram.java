package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.RectanglePictogramFigure;

public class RectanglePictogram extends AbstractShapePictogram {

	public RectanglePictogram() {
		super();		
		super.setKeepRatio(false);
	}

	public RectanglePictogram(RectanglePictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new RectanglePictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new RectanglePictogramFigure();
	}

}
