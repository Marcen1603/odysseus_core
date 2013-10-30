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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.ConnectionCreateCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.ConnectionReconnectCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;


/**
 * @author DGeesen
 * 
 */
public class PictogramGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {
		
	
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand result = new ConnectionCreateCommand();
		result.setSource((Pictogram) getHost().getModel());
		result.setConnection((Connection) request.getNewObject());
		request.setStartCommand(result);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand result = (ConnectionCreateCommand) request.getStartCommand();
		result.setTarget((Pictogram) getHost().getModel());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Connection conn = (Connection)request.getConnectionEditPart().getModel();
		Pictogram targetNode = (Pictogram)getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        cmd.setNewTargetNode(targetNode);       
        return cmd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Connection conn = (Connection)request.getConnectionEditPart().getModel();
		Pictogram sourceNode = (Pictogram)getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        cmd.setNewSourceNode(sourceNode);       
        return cmd;
	}

}
