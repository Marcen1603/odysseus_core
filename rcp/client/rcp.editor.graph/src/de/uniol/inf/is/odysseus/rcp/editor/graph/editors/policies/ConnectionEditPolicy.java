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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands.ConnectionDeleteCommand;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;

/**
 * @author DGeesen
 * 
 */
public class ConnectionEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {
	
		
	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		ConnectionDeleteCommand result = new ConnectionDeleteCommand();
		result.setConnection((Connection) getHost().getModel());
		return result;
	}	

}
