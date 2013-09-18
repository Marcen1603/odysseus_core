package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.PictogramGroup;

public class ImagePictogramCreateCommand extends Command {

	private ImagePictogram pictogram;
	private Rectangle constraint;
	private PictogramGroup parent;
	private Dimension size = new Dimension(65, 35);

	public void execute() {				
		Shell currentShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		NewPictogramDialog dialog = new NewPictogramDialog(currentShell);
		if (Window.OK == dialog.open()) {
			pictogram.setFilename(dialog.getLocation());
			pictogram.setPredicate(dialog.getPredicate());
			pictogram.setStretch(dialog.isStretch());
			setConstraint();
			parent.addPictogram(pictogram);
		}				
		
	}

	private void setConstraint() {
		if (constraint != null)
			pictogram.setConstraint(constraint);
	}

	public void undo() {
		parent.removePictogram(pictogram);
	}

	public void setPictogram(ImagePictogram pg) {
		this.pictogram = pg;
	}

	public void setLocation(Point location) {
		this.constraint = new Rectangle(location, size);
	}

	public void setParent(PictogramGroup parent) {
		this.parent = parent;
	}
}