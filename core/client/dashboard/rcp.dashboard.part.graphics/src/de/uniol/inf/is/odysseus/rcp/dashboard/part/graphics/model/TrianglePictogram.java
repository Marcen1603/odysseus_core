package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import org.eclipse.draw2d.IFigure;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.TrianglePictogramFigure;

public class TrianglePictogram extends AbstractShapePictogram {

	public TrianglePictogram() {
		super();		
		super.setKeepRatio(false);
	}

	public TrianglePictogram(TrianglePictogram rp) {
		super(rp);
		super.setKeepRatio(false);
	}
	

	@Override
	public AbstractPictogram clone() {
		return new TrianglePictogram(this);
	}

	@Override
	public IFigure createPictogramFigure() {
		return new TrianglePictogramFigure();
	}

}
