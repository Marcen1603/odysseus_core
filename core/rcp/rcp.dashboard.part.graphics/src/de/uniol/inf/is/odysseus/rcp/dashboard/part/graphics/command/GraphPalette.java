package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory.PictogramFactory;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.MultipleImagePictogram;

public class GraphPalette extends PaletteRoot {
	public GraphPalette() {
		PaletteGroup group = new PaletteGroup("Controls");
		SelectionToolEntry entry = new SelectionToolEntry();
		group.add(entry);
		setDefaultEntry(entry);
		group.add(new PaletteSeparator());
		group.add(new CreationToolEntry("Image", "An image that is shown if the predicate is true", new PictogramFactory(ImagePictogram.class), Activator.getImage("image.png"), null));
		group.add(new CreationToolEntry("Multiple Images", "A list of images where a predicate selects the image", new PictogramFactory(MultipleImagePictogram.class), Activator.getImage("images.png"), null));
		add(group);
	}
}
