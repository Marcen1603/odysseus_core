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
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class ConnectionCreateCommand extends Command {
	private OperatorNode source;
	private OperatorNode target;
	private Connection connection;	

	public void setSource(OperatorNode source) {
		this.source = source;
	}

	public void setTarget(OperatorNode target) {
		this.target = target;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public boolean canExecute() {
		if(source == null || target == null){
			return false;
		}
		if(source==target){
			return false;
		}
		if(target.getOperatorInformation().getMaxPorts()==target.getTargetConnections().size()){
			return false;
		}
		if(!source.isSatisfied()){
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		connection.setSource(source);
		connection.setTarget(target);
	}

	@Override
	public void undo() {
		connection.setSource(null);
		connection.setTarget(null);
	}

}
