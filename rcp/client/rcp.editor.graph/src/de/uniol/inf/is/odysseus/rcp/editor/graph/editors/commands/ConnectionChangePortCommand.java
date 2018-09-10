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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;

/**
 * @author DGeesen
 * 
 */
public class ConnectionChangePortCommand extends Command {

	private Integer oldPort;
	private Integer newPort;
	private Connection conn;
	private boolean target;

	public ConnectionChangePortCommand(boolean target) {
		this.target = target;
	}

	@Override
	public void execute() {
		if (target) {
			oldPort = conn.getTargetPort();
			conn.setTargetPort(newPort);
		} else {
			oldPort = conn.getSourcePort();
			conn.setSourcePort(newPort);
		}
	}

	@Override
	public void undo() {
		if (target) {
			conn.setTargetPort(oldPort);
		} else {
			conn.setSourcePort(oldPort);
		}
	}

	public void setNewPort(Integer newPort) {
		this.newPort = newPort;
	}

	public void setModel(Connection conn) {
		this.conn = conn;
	}

}
