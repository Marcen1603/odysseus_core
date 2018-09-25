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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class OperatorNodeChangeConstraintCommand extends Command {
	private Rectangle newConstraint;
	private Rectangle oldConstraint;
	private OperatorNode node;

	@Override
	public void execute() {
		if (oldConstraint == null)
			oldConstraint = new Rectangle(node.getConstraint());
		node.setConstraint(newConstraint);
	}

	@Override
	public void undo() {
		node.setConstraint(oldConstraint);
	}

	public void setNewConstraint(Rectangle constraint) {
		this.newConstraint = constraint;
	}

	public void setNode(OperatorNode node) {
		this.node = node;
	}
}
