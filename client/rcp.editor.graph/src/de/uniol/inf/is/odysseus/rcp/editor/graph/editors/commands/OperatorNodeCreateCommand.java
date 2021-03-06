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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class OperatorNodeCreateCommand extends Command {
	private static final Dimension INITIAL_NODE_DIMENSION = new Dimension(160, 80);
	private OperatorNode node;
	private Rectangle constraint;
	private Graph parent;

	@Override
	public void execute() {
		setNodeConstraint();	
		parent.addNode(node);
	}

	
	private void setNodeConstraint() {
		if (constraint != null)
			node.setConstraint(constraint);
	}

	@Override
	public void undo() {
		parent.removeNode(node);
	}

	public void setNode(OperatorNode node) {
		this.node = node;
	}

	public void setLocation(Point location) {
		this.constraint = new Rectangle(location, INITIAL_NODE_DIMENSION);
	}

	public void setParent(Graph parent) {
		this.parent = parent;
	}

}
