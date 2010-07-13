package de.uniol.inf.is.odysseus.rcp.editor.editorpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.OperatorCreateCommand;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.OperatorSetConstraintCommand;

public class OperatorPlanEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener {

	public OperatorPlanEditPart(OperatorPlan plan) {
		setModel(plan);
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setLayoutManager(new XYLayout());
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.white);
		return figure;
	}

	@Override
	public void activate() {
		((OperatorPlan) getModel()).addPropertyChangeListener(this);
		super.activate();
	}

	@Override
	public void deactivate() {
		((OperatorPlan) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {
			@Override
			protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
				return null;
			}

			@Override
			protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
				if (child instanceof OperatorEditPart && constraint instanceof Rectangle) {
					return new OperatorSetConstraintCommand((Operator) child.getModel(), request, (Rectangle) constraint);
				}
				return super.createChangeConstraintCommand(request, child, constraint);
			}

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				Object childClass = request.getNewObjectType();
				if (childClass == Operator.class) {
					return new OperatorCreateCommand((Operator) request.getNewObject(), (OperatorPlan) getHost().getModel(), (Rectangle) getConstraintFor(request));
				}
				return null;
			}
		});
	}

	@Override
	protected List<Operator> getModelChildren() {
		return ((OperatorPlan) getModel()).getOperators();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(OperatorPlan.PROPERTY_OPERATORS))
			refreshChildren();
		else
			refresh();
	}
}
