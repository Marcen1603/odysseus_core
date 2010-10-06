package de.uniol.inf.is.odysseus.rcp.editor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.ConnectionCreateCommand;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.OperatorDeleteCommand;

public class OperatorEditPart extends AbstractGraphicalEditPart implements NodeEditPart, PropertyChangeListener {

	private static final String ERROR_PREFIX = "- ";
	
	private ConnectionAnchor anchor;

	public OperatorEditPart(Operator op) {
		setModel(op);
	}

	@Override
	protected IFigure createFigure() {
		return new OperatorFigure();
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			@Override
			protected Command createDeleteCommand(GroupRequest deleteRequest) {
				Object model = getHost().getModel();
				Object parent = getHost().getParent().getModel();
				if( parent instanceof OperatorPlan &&  model instanceof Operator) {
					return new OperatorDeleteCommand((OperatorPlan)parent, (Operator)model);
				}
				return super.createDeleteCommand(deleteRequest);
			}
		});
			
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {

			protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
				ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
				cmd.setTarget((Operator) getHost().getModel());
				return cmd;
			}

			protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
				Operator source = (Operator) getHost().getModel();
				ConnectionCreateCommand cmd = new ConnectionCreateCommand(source);
				request.setStartCommand(cmd);
				return cmd;
			}

			protected Command getReconnectSourceCommand(ReconnectRequest request) {
				return null;
			}

			protected Command getReconnectTargetCommand(ReconnectRequest request) {
				return null;
			}
			
		});
	}

	
	@Override
	protected List<?> getModelSourceConnections() {
		return ((Operator) getModel()).getConnectionsAsSource();
	}

	@Override
	protected List<?> getModelTargetConnections() {
		return ((Operator) getModel()).getConnectionsAsTarget();
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
		figure.setText(model.getOperatorBuilderName());
		
		if( model.getOperatorBuilder().validate() ) {
			figure.unmarkError();
			figure.setToolTip( null );
		} else {
			figure.markError();
			figure.setToolTip(new Label(getErrorText(model.getOperatorBuilder().getErrors())));
		}
		
		Rectangle r = new Rectangle(model.getX(), model.getY(), -1, -1);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, r);
	}
	
	protected String getErrorText(List<Exception> errors) {
		StringBuilder sb = new StringBuilder();
		if( !errors.isEmpty() )
			sb.append(ERROR_PREFIX);
		
		for( int i = 0; i < errors.size(); i++) {
			Exception ex = errors.get(i);
			
			if( i != 0 ) 
				sb.append("\n");
			
			sb.append(ex.getMessage());
		}
		return sb.toString();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
		refreshSourceConnections();
		refreshTargetConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			anchor = new ChopboxAnchor(getFigure());
		}
		return anchor;
	}
}
