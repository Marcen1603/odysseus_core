package de.uniol.inf.is.odysseus.rcp.editor.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;

public class OperatorConnectionEditPart extends AbstractConnectionEditPart {

	public OperatorConnectionEditPart( OperatorConnection model ) {
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

	}
	
	@Override
	protected void refreshVisuals() {
		
		super.refreshVisuals();
	}

}
