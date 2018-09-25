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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;

/**
 * @author DGeesen
 * 
 */
public class ConnectionReconnectCommand extends Command {

	private Connection conn;
	private AbstractPictogram oldSourceNode;
	private AbstractPictogram oldTargetNode;
	private AbstractPictogram newSourceNode;
	private AbstractPictogram newTargetNode;

	public ConnectionReconnectCommand(Connection conn) {
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		this.conn = conn;
		this.oldSourceNode = conn.getSource();
		this.oldTargetNode = conn.getTarget();
	}

	@Override
	public boolean canExecute() {
		if (newSourceNode != null) {
			return checkSourceReconnection();
		} else if (newTargetNode != null) {
			return checkTargetReconnection();
		}
		return false;
	}

	private boolean checkSourceReconnection() {
		if (newSourceNode == null)
			return false;
		else if (newSourceNode.equals(oldTargetNode))
			return false;
		return true;
	}

	private boolean checkTargetReconnection() {
		if (newTargetNode == null)
			return false;
		else if (oldSourceNode.equals(newTargetNode))
			return false;
		return true;
	}

	public void setNewSourceNode(AbstractPictogram sourceNode) {
		if (sourceNode == null) {
			throw new IllegalArgumentException();
		}
		this.newSourceNode = sourceNode;
		this.newTargetNode = null;
	}

	public void setNewTargetNode(AbstractPictogram targetNode) {
		if (targetNode == null) {
			throw new IllegalArgumentException();
		}
		this.newSourceNode = null;
		this.newTargetNode = targetNode;
	}

	@Override
	public void execute() {
		if (newSourceNode != null) {
			conn.reconnect(newSourceNode, oldTargetNode);
		} else if (newTargetNode != null) {
			conn.reconnect(oldSourceNode, newTargetNode);
		} else {
			throw new IllegalStateException("Should not happen");
		}
	}

	@Override
	public void undo() {
		conn.reconnect(oldSourceNode, oldTargetNode);
	}
}