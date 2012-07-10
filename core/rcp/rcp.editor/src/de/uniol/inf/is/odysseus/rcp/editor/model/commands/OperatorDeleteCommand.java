/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import java.util.List;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;

public class OperatorDeleteCommand extends Command {

	private Operator operator;
	private OperatorPlan plan;
	
	public OperatorDeleteCommand( OperatorPlan plan,  Operator op ) {
		operator = op;
		this.plan = plan;
	}
	
	@Override
	public void execute() {
		
		// Verbindungen entfernen
		List<OperatorConnection> connectionsAsSource = operator.getConnectionsAsSource();
		List<OperatorConnection> connectionsAsTarget = operator.getConnectionsAsTarget();
		for( OperatorConnection conn : connectionsAsSource ) {
			Operator op = conn.getTarget();
			op.removeConnection(conn);
		}
		for( OperatorConnection conn : connectionsAsTarget ) {
			Operator op = conn.getSource();
			op.removeConnection(conn);
		}

		plan.removeOperator(operator);
	}
	
	@Override
	public void undo() {
		// Verbindungen entfernen
		List<OperatorConnection> connectionsAsSource = operator.getConnectionsAsSource();
		List<OperatorConnection> connectionsAsTarget = operator.getConnectionsAsTarget();
		for( OperatorConnection conn : connectionsAsSource ) {
			Operator op = conn.getTarget();
			op.addConnection(conn);
		}
		for( OperatorConnection conn : connectionsAsTarget ) {
			Operator op = conn.getSource();
			op.addConnection(conn);
		}

		plan.addOperator(operator);
		
	}
	
	@Override
	public void redo() {
		execute();
	}
}
