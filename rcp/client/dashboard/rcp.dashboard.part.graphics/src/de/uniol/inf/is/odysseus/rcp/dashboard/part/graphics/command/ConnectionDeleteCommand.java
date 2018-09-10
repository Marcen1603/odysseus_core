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
public class ConnectionDeleteCommand extends Command {
	private AbstractPictogram source;
	private AbstractPictogram target;
	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void execute() {
		if (source == null) {
			source = connection.getSource();
		}
		if (target == null) {
			target = connection.getTarget();
		}
		connection.setSource(null);
		connection.setTarget(null);
		source.getGraphicsLayer().removePart(connection);
	}

	@Override
	public void undo() {
		connection.setSource(source);
		connection.setTarget(target);
		source.getGraphicsLayer().addPart(connection);
	}
}
