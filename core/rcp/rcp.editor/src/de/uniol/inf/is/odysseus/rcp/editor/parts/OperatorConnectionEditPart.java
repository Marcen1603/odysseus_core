package de.uniol.inf.is.odysseus.rcp.editor.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;
import de.uniol.inf.is.odysseus.rcp.editor.model.commands.ConnectionDeleteCommand;

public class OperatorConnectionEditPart extends AbstractConnectionEditPart {

	public OperatorConnectionEditPart(OperatorConnection model) {
		setModel(model);
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection connection = (PolylineConnection) super.createFigure();
		connection.setTargetDecoration(new PolygonDecoration());
		return connection;
	}

	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void deactivate() {
		super.deactivate();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy() {
			@Override
			protected Command getDeleteCommand(GroupRequest request) {
				OperatorConnection conn = (OperatorConnection)getHost().getModel();
				return new ConnectionDeleteCommand(conn);
			}
		});
	}

	@Override
	protected void refreshVisuals() {

		super.refreshVisuals();
	}

}
