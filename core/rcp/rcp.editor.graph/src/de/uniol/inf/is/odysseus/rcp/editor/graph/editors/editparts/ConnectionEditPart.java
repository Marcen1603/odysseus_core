/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.ConnectionFigure;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.policies.ConnectionEditPolicy;

/**
 * @author DGeesen
 * 
 */
public class ConnectionEditPart extends AbstractConnectionEditPart implements Observer{
	public ConnectionEditPart(Connection connection) {
		setModel(connection);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());		
	}
	
	public void refreshVisuals() {
		ConnectionFigure figure = (ConnectionFigure) getFigure();
		Connection connection = (Connection) getModel();		
		figure.getTargetPortLabel().setText(Integer.toString(connection.getTargetPort()));
		figure.getSourcePortLabel().setText(Integer.toString(connection.getSourcePort()));
		figure.repaint();
	}
	
	protected IFigure createFigure() {
		return new ConnectionFigure();		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();		
	}

}
