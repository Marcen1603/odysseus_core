package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.GraphicsLayer;

public class PictogramCreateCommand extends Command {

	private AbstractPictogram pictogram;
	private Point location;
	private GraphicsLayer parent;	

	@Override
	@SuppressWarnings("unchecked")
	public void execute() {

		try {
			@SuppressWarnings("rawtypes")
			AbstractPictogramDialog dialog = pictogram.getConfigurationDialog().newInstance();
			pictogram.setGraphicsLayer(parent);
			dialog.init(pictogram);
			if (Window.OK == dialog.open()) {					
				parent.addPictogram(pictogram);					
				pictogram.setLocation(location);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void undo() {
		parent.removePictogram(pictogram);
	}
		

	public void setPictogram(AbstractPictogram pg) {
		this.pictogram = pg;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public void setParent(GraphicsLayer parent) {
		this.parent = parent;
	}
}