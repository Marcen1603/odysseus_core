package de.uniol.inf.is.odysseus.rcp.editor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;

public class OperatorEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener {

	public OperatorEditPart(Operator op) {
		setModel(op);
	}

	@Override
	protected IFigure createFigure() {
		return new OperatorFigure();
	}

	@Override
	protected void createEditPolicies() {
	}

	@Override
	public void showSourceFeedback(Request request) {
		System.out.println("showSourceFeedback "+ request.getClass().getSimpleName());
		super.showSourceFeedback(request);
	}

	@Override
	public void showTargetFeedback(Request request) {
		System.out.println("showTargetFeedback "+ request.getClass().getSimpleName());
		super.showTargetFeedback(request);
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		System.out.println("eraseSourceFeedback "+ request.getClass().getSimpleName());
		super.eraseSourceFeedback(request);
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		System.out.println("eraseTargetFeedback " + request.getClass().getSimpleName());
		super.eraseTargetFeedback(request);
	}

	@Override
	public void activate() {
		((Operator) getModel()).addPropertyChangeListener(this);
		super.activate();
	}

	@Override
	public void deactivate() {
		((Operator) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	protected void refreshVisuals() {
		OperatorFigure figure = (OperatorFigure) getFigure();
		Operator model = (Operator) getModel();
		figure.setText(model.getOperatorExtensionDescriptor().getLabel());

		Rectangle r = new Rectangle(model.getX(), model.getY(), -1, -1);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, r);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
	}
}
