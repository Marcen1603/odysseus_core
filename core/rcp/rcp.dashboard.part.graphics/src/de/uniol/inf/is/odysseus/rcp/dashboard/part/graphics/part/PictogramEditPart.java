package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.ImagePictogramFigure;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.PictogramComponentEditPolicy;

public class PictogramEditPart extends AbstractGraphicalEditPart implements Observer {
	public PictogramEditPart(Pictogram node) {
		setModel(node);
	}

	protected IFigure createFigure() {
		return new ImagePictogramFigure();
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PictogramComponentEditPolicy());
	}

	public void refreshVisuals() {
		ImagePictogramFigure figure = (ImagePictogramFigure) getFigure();
		ImagePictogram node = (ImagePictogram) getModel();
		figure.setVisibile(node.isVisibile());
		figure.setImage(node.getFilename());
		figure.refresh();
		PictogramGroupEditPart parent = (PictogramGroupEditPart) getParent();
		Rectangle r = new Rectangle(node.getConstraint());

		parent.setLayoutConstraint(this, figure, r);
	}

	public void activate() {
		if (!isActive()) {
			((Pictogram) getModel()).addObserver(this);
		}
		super.activate();
	}

	public void deactivate() {
		if (isActive()) {
			((Pictogram) getModel()).deleteObserver(this);
		}
		super.deactivate();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();
	}
}
