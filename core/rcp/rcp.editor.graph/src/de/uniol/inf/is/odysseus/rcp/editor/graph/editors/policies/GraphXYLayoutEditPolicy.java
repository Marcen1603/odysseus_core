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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands.OperatorNodeChangeConstraintCommand;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands.OperatorNodeCreateCommand;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class GraphXYLayoutEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		OperatorNodeChangeConstraintCommand changeConstraintCommand = new OperatorNodeChangeConstraintCommand();
		changeConstraintCommand.setNode((OperatorNode) child.getModel());
		changeConstraintCommand.setNewConstraint((Rectangle) constraint);
		return changeConstraintCommand;
	}

		
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getNewObjectType().equals(OperatorNode.class)) {
			OperatorNodeCreateCommand result = new OperatorNodeCreateCommand();
			result.setLocation(request.getLocation());
			result.setNode((OperatorNode) request.getNewObject());
			result.setParent((Graph) getHost().getModel());
			return result;
		}
		return null;
	}
}
