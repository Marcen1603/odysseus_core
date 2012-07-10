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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;

public class OperatorSetConstraintCommand extends Command {

	private final Rectangle newBounds;
	private Rectangle oldBounds;
	private final ChangeBoundsRequest request;
	private final Operator operator;

	public OperatorSetConstraintCommand(Operator operator, ChangeBoundsRequest req, Rectangle newBounds) {
		if (operator == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.operator = operator;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel("move / resize");
	}

	@Override
	public boolean canExecute() {
		Object type = request.getType();
		return (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type) || RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN
				.equals(type));
	}

	@Override
	public void execute() {
		oldBounds = new Rectangle(operator.getX(), operator.getY(), -1, -1);
		redo();
	}

	@Override
	public void redo() {
		operator.setX(newBounds.x);
		operator.setY(newBounds.y);
	}

	@Override
	public void undo() {
		operator.setX(oldBounds.x);
		operator.setY(oldBounds.y);
	}
}
