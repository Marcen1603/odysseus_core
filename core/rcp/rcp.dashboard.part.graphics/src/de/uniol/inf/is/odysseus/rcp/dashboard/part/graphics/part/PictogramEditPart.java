package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.PictogramFigure;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.PictogramComponentEditPolicy;

public class PictogramEditPart extends AbstractGraphicalEditPart implements Observer {
	public PictogramEditPart(Pictogram node) {
		setModel(node);
	}

	protected IFigure createFigure() {
		return new PictogramFigure();
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PictogramComponentEditPolicy());
	}

	public void refreshVisuals() {
		PictogramFigure figure = (PictogramFigure) getFigure();
		ImagePictogram node = (ImagePictogram) getModel();
		PictogramGroupEditPart parent = (PictogramGroupEditPart) getParent();
		if(node.isVisibile()){
			Image image = new Image(Display.getDefault(), new ImageData(node.getFilename()));
			figure.getLabel().setIcon(image);
		}else{
			figure.getLabel().setIcon(null);
		}
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
