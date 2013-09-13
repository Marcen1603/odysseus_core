package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.ImagePictogramCreateCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.PictogramChangeConstraintCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.PictogramGroup;

public class GraphXYLayoutEditPolicy extends XYLayoutEditPolicy {
	
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		PictogramChangeConstraintCommand changeConstraintCommand = new PictogramChangeConstraintCommand();
		changeConstraintCommand.setNode((Pictogram) child.getModel());
		changeConstraintCommand.setNewConstraint((Rectangle) constraint);
		return changeConstraintCommand;
	}

	
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getNewObjectType().equals(Pictogram.class)) {
			ImagePictogramCreateCommand result = new ImagePictogramCreateCommand();
			result.setLocation(request.getLocation());
			result.setPictogram((ImagePictogram) request.getNewObject());
			result.setParent((PictogramGroup) getHost().getModel());
			return result;
		}
		return null;
	}
}
