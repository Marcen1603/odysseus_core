/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.model.commands;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;

public class ConnectionDeleteCommand extends Command {

	private OperatorConnection connection;
	
	public ConnectionDeleteCommand( OperatorConnection connection ) {
		this.connection = connection;
	}
	
	@Override
	public void execute() {
		connection.getSource().removeConnection(connection);
		connection.getTarget().removeConnection(connection);
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public void undo() {
		connection.getSource().addConnection(connection);
		connection.getTarget().addConnection(connection);
	}
}
