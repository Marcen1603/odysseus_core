package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.OctagonPictogramFigure;

public class OctagonPictogram extends AbstractShapePictogram {

	public OctagonPictogram() {
		super();		
		super.setKeepRatio(false);
	}

	public OctagonPictogram(OctagonPictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new OctagonPictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new OctagonPictogramFigure();
	}

}
