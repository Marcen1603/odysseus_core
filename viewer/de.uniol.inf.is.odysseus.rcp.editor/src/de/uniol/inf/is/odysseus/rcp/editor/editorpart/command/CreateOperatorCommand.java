package de.uniol.inf.is.odysseus.rcp.editor.editorpart.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;

public class CreateOperatorCommand extends Command {

	private Operator op;
	private OperatorPlan plan; 
	private Rectangle bounds;
	
	public CreateOperatorCommand( Operator op, OperatorPlan plan, Rectangle bounds ) {
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
