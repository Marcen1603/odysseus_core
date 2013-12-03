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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection.TextPosition;

/**
 * @author DGeesen
 * 
 */
public class ConnectionChangeTextCommand extends Command {

	private String oldText;
	private String newText;
	private Connection conn;
	private TextPosition position;

	public ConnectionChangeTextCommand(TextPosition position) {
		this.position = position;
	}

	@Override
	public void execute() {
		oldText = conn.getTextByPosition(position);
		conn.setTextByPosition(position, newText);
	}

	@Override
	public void undo() {
		conn.setTextByPosition(position, oldText);
	}

	public void setNewText(String text) {
		this.newText = text;
	}

	public void setModel(Connection conn) {
		this.conn = conn;
	}

}
