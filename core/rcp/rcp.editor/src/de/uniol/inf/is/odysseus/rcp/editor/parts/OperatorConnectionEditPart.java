package de.uniol.inf.is.odysseus.rcp.editor.parts;

import java.util.List;

import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

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
				OperatorConnection conn = (OperatorConnection) getHost().getModel();
				return new ConnectionDeleteCommand(conn);
			}
		});
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
	}
	
	@Override
	public void setSource(EditPart editPart) {
		super.setSource(editPart);
		
		// Quellenbeschriftung
		OperatorEditPart src = (OperatorEditPart) editPart;
		if( src != null ) {
			PolylineConnection connection = (PolylineConnection)getFigure();
			List<?> srcConnections = src.getSourceConnections();

			ConnectionLocator locator = new ConnectionLocator(connection, ConnectionLocator.SOURCE);
			locator.setRelativePosition(PositionConstants.NORTH_EAST);
			connection.add(createLabel(String.valueOf(srcConnections.indexOf(this))), locator);
		}
	}

	@Override
	public void setTarget(EditPart editPart) {
		super.setTarget(editPart);
		
		OperatorEditPart tgt = (OperatorEditPart)editPart;
		if( tgt != null ) {
			PolylineConnection connection = (PolylineConnection)getFigure();
			List<?> tgtConnections = tgt.getTargetConnections();
			
			ConnectionLocator locator2 = new ConnectionLocator(connection, ConnectionLocator.TARGET);
			locator2.setRelativePosition(PositionConstants.SOUTH_EAST);
			connection.add(createLabel(String.valueOf(tgtConnections.indexOf(this))), locator2);
		}
	}
	
	private Label createLabel( String text ) {
		Label label = new Label(text);
		label.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		label.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
//		label.setBorder(new LineBorder(1));
		label.setOpaque(true);	
		
		return label;
	}
}
