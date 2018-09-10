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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy;


import org.eclipse.draw2d.Label;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.ConnectionChangeTextCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.ConnectionFigure;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection.TextPosition;

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
//		Point location = request.getCellEditor().getControl().getLocation();		
		ConnectionFigure figure = ((ConnectionFigure) getHostFigure());
		TextPosition pos = figure.getNearestPosition(request.getLocation());		
		ConnectionChangeTextCommand command = new ConnectionChangeTextCommand(pos);
		command.setModel((Connection) getHost().getModel());
		command.setNewText(request.getCellEditor().getValue().toString());
		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		ConnectionFigure figure = ((ConnectionFigure) getHostFigure());
		TextPosition pos = (TextPosition) request.getDirectEditFeature();
		Label label = figure.getLabelByPosition(pos);
		label.setText(value);
	}
}
