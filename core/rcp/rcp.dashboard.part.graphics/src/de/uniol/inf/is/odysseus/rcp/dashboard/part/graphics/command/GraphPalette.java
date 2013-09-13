package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory.ImagePictogramFactory;

public class GraphPalette extends PaletteRoot {
	public GraphPalette() {
		PaletteGroup group = new PaletteGroup("Controls");
		SelectionToolEntry entry = new SelectionToolEntry();
		group.add(entry);
		setDefaultEntry(entry);
		group.add(new PaletteSeparator());
		group.add(new CreationToolEntry("Image", "Creates a new image.", new ImagePictogramFactory(), Activator.getImage("image.png"), null));
		add(group);
	}
}
