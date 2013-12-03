package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;

public class PictogramChangeConstraintCommand extends Command {
	private Rectangle newConstraint;
	private Rectangle oldConstraint;
	private AbstractPictogram node;

	@Override
	public void execute() {
		if (oldConstraint == null){
			oldConstraint = new Rectangle(node.getConstraint());
		}
		node.setConstraint(newConstraint);		
	}

	@Override
	public void undo() {
		node.setConstraint(oldConstraint);
	}

	public void setNewConstraint(Rectangle newConstraint) {
		this.newConstraint = newConstraint;
	}

	public void setNode(AbstractPictogram node) {
		this.node = node;
	}
}
