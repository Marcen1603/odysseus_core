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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.ConnectionDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.editing.ConnectionCellEditorLocator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.editing.ConnectionDirectEditManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.ConnectionFigure;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.ConnectionDirectEditPolicy;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.ConnectionEditPolicy;


/**
 * @author DGeesen
 * 
 */
public class ConnectionEditPart extends AbstractConnectionEditPart implements Observer {
	public ConnectionEditPart(Connection connection) {
		setModel(connection);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ConnectionDirectEditPolicy());
	}

	public void refreshVisuals() {
		ConnectionFigure figure = (ConnectionFigure) getFigure();
		Connection connection = (Connection) getModel();
		figure.getTargetPortLabel().setText(connection.getTargetText());
		figure.getSourcePortLabel().setText(connection.getSourceText());
		figure.setLineWidth(connection.getWidth());
		figure.repaint();
	}

	protected IFigure createFigure() {
		return new ConnectionFigure();
	}

	public void activate() {
		if (!isActive())
			((Connection) getModel()).addObserver(this);
		super.activate();
	}

	public void deactivate() {
		if (isActive())
			((Connection) getModel()).deleteObserver(this);
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 */
	@Override
	public void performRequest(Request req) {

		if (req.getType() == RequestConstants.REQ_OPEN || req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			if (req instanceof DirectEditRequest) {
				DirectEditRequest der = (DirectEditRequest) req;
				performDirectEditing(der.getLocation());
			}else{
				if (req.getType() == RequestConstants.REQ_OPEN) {
					try {			
						Connection connection = ((Connection) getModel());
						ConnectionDialog dialog = connection.getConfigurationDialog().newInstance();
						dialog.init(connection);
						if (Window.OK == dialog.open()) {
							// hint: save is invoked by ok button 
							refreshVisuals();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void performDirectEditing(Point location) {
		Label label = getNearestLabel(location);
		ConnectionDirectEditManager manager = new ConnectionDirectEditManager(this, TextCellEditor.class, new ConnectionCellEditorLocator(label), label);
		manager.show();
	}

	private Label getNearestLabel(Point location) {
		ConnectionFigure figure = (ConnectionFigure) getFigure();
		double distanceToEnd = figure.getTargetPortLabel().getLocation().getDistance(location);
		double distanceToStart = figure.getSourcePortLabel().getLocation().getDistance(location);
		if (distanceToEnd < distanceToStart) {
			return figure.getTargetPortLabel();
		} else {
			return figure.getSourcePortLabel();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();
	}

}
