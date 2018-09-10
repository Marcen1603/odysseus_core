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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.policies;


import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands.ConnectionChangePortCommand;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.ConnectionFigure;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;

/**
 * @author DGeesen
 * 
 */
public class ConnectionDirectEditPolicy extends DirectEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		Point location = request.getCellEditor().getControl().getLocation();
		ConnectionChangePortCommand command = new ConnectionChangePortCommand(isTargetRequest(location));
		command.setModel((Connection) getHost().getModel());
		command.setNewPort((Integer.valueOf(request.getCellEditor().getValue().toString())));
		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
//		String value = (String) request.getCellEditor().getValue();
//		if (isTargetRequest(request)) {
//			((ConnectionFigure) getHostFigure()).getTargetPortLabel().setText(value);
//		} else {
//			((ConnectionFigure) getHostFigure()).getSourcePortLabel().setText(value);
//		}
	}

	private boolean isTargetRequest(Point location) {
		ConnectionFigure figure = (ConnectionFigure) getHostFigure();
		org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(location.x, location.y);
		double distanceToEnd = figure.getTargetPortLabel().getLocation().getDistance(p);
		double distanceToStart = figure.getSourcePortLabel().getLocation().getDistance(p);
		if (distanceToEnd < distanceToStart) {
			return true;
		} 
		return false;
	}

}
