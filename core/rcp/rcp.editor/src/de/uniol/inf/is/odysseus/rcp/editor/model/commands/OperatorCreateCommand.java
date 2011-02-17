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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;

public class OperatorCreateCommand extends Command {

	private Operator op;
	private OperatorPlan plan; 
	private Rectangle bounds;
	
	public OperatorCreateCommand( Operator op, OperatorPlan plan, Rectangle bounds ) {
		this.op = op;
		this.plan = plan;
		this.bounds = bounds;
	}
	
	@Override
	public void execute() {
		op.setX(bounds.x);
		op.setY(bounds.y);
		plan.addOperator(op);
		super.execute();
	}
	
	@Override
	public void undo() {
		plan.removeOperator(op);
		super.undo();
	}
	
	@Override
	public void redo() {
		op.setX(bounds.x);
		op.setY(bounds.y);
		plan.addOperator(op);
		super.redo();
	}
}
