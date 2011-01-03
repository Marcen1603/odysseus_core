package de.uniol.inf.is.odysseus.rcp.editor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import de.uniol.inf.is.odysseus.rcp.editor.anchor.OperatorAnchor;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.ConnectionCreateCommand;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.OperatorDeleteCommand;

public class OperatorEditPart extends AbstractGraphicalEditPart implements NodeEditPart, PropertyChangeListener {

	private static final String ERROR_PREFIX = "- ";
	
	private ConnectionAnchor standardAnchor;
	
	private Map<ConnectionEditPart, OperatorAnchor> inputAnchors = new HashMap<ConnectionEditPart, OperatorAnchor>();
	private Map<ConnectionEditPart, OperatorAnchor> outputAnchors = new HashMap<ConnectionEditPart, OperatorAnchor>();

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

			@Override
			protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
				ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
				cmd.setTarget((Operator) getHost().getModel());
				return cmd;
			}

			@Override
			protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
				Operator source = (Operator) getHost().getModel();
				ConnectionCreateCommand cmd = new ConnectionCreateCommand(source);
				request.setStartCommand(cmd);
				return cmd;
			}

			@Override
			protected Command getReconnectSourceCommand(ReconnectRequest request) {
				return null;
			}

			@Override
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
		
		model.build();
		
		figure.unmarkError();
		figure.setToolTip( null );
		if( !model.hasLogicalOperator() ) {
			figure.markError();
			figure.setToolTip(new Label(getErrorText(model.getOperatorBuilder().getErrors())));
		}
		
		Rectangle r = new Rectangle(model.getX(), model.getY(), -1, -1);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, r);
		
		refreshAnchors();
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

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		if( !outputAnchors.containsKey(connection)) {
			OperatorAnchor a = new OperatorAnchor(getFigure(), OperatorAnchor.Type.OUTPUT,outputAnchors.size()+1, outputAnchors.size()+1);
			outputAnchors.put(connection, a);

			return a;
		}
		return outputAnchors.get(connection);
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getStandardConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		if( !inputAnchors.containsKey(connection)) {
			OperatorAnchor a = new OperatorAnchor(getFigure(), OperatorAnchor.Type.INPUT,inputAnchors.size()+1, inputAnchors.size()+1);
			inputAnchors.put(connection, a);
			
			return a;
		}
		return inputAnchors.get(connection);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getStandardConnectionAnchor();
	}
	
	@Override
	protected void removeSourceConnection(ConnectionEditPart connection) {
		super.removeSourceConnection(connection);
		outputAnchors.remove(connection);
		refreshVisuals();
	}
	
	@Override
	protected void removeTargetConnection(ConnectionEditPart connection) {
		super.removeTargetConnection(connection);
		inputAnchors.remove(connection);
		refreshVisuals();
	}

	protected ConnectionAnchor getStandardConnectionAnchor() {
		if (standardAnchor == null) {
			standardAnchor = new ChopboxAnchor(getFigure());
		}
		return standardAnchor;
	}
	
	protected void refreshAnchors() {
		for( OperatorAnchor o : inputAnchors.values())
			o.setMaxNumber(inputAnchors.size());
		
		for( OperatorAnchor o : outputAnchors.values())
			o.setMaxNumber(outputAnchors.size());
	}
}
