package de.uniol.inf.is.odysseus.rcp.editor.editorpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;

public class OperatorEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener  {

	public OperatorEditPart( Operator op ) {
		setModel(op);
	}

	@Override
	protected IFigure createFigure() {
		return new Label();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {

			@Override
			protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
				System.out.println("changeConstraint");
				return null;
			}

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				System.out.println("createCommand");
				return null;
			}
			
		});
		
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
	}
	
	@Override
	public void activate() {
		((Operator)getModel()).addPropertyChangeListener(this);
		super.activate();
	}
	
	@Override
	public void deactivate() {
		((Operator)getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	protected void refreshVisuals() {
		Label figure = (Label)getFigure();
		Operator model = (Operator)getModel();
		figure.setText(model.getOperatorExtensionDescriptor().getLabel() + "[" + model.getX() + "," + model.getY() + "]");
		
		Rectangle r = new Rectangle(model.getX(), model.getY(), -1, -1);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, r);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
	}
}
