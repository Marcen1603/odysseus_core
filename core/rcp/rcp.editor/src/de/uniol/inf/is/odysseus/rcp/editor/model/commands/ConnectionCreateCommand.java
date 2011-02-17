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

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;

public class ConnectionCreateCommand extends Command {

	private final Operator source;
	private Operator target = null;
	private OperatorConnection connection = null;
	
	public ConnectionCreateCommand( Operator src ) {
		source = src;
	}
	
	@Override
	public boolean canExecute() {
		if( source.equals(target)) 
			return false;
		
		for (OperatorConnection connection : source.getConnectionsAsSource()) {
			if (connection.getTarget().equals(target)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void execute() {
		connection = new OperatorConnection();
		connection.setSource(source);
		connection.setTarget(target);
		source.addConnection(connection);
		target.addConnection(connection);
	}
	
	@Override
	public void undo() {
		target.removeConnection(connection);
		source.removeConnection(connection);
		connection.setSource( null );
		connection.setTarget( null );
	}
	
	@Override
	public void redo() {
		connection.setSource(source);
		connection.setTarget(target);
		target.addConnection(connection);
		source.addConnection(connection);
		super.redo();
	}
	
	public void setTarget( Operator tgt ) {
		target = tgt;
	}
}
