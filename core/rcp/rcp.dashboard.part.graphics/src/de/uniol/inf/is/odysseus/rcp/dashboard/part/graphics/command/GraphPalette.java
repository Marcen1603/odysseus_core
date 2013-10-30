package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory.ConnectionFactory;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory.PictogramFactory;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.MultipleImagePictogram;

public class GraphPalette extends PaletteRoot {

	public static PaletteRoot createGraphPalette() {
		PaletteRoot pr = new PaletteRoot();
		createToolsGroup(pr);
		createOperatorsGroup(pr);
		return pr;
	}

	private static void createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);
		toolbar.add(new MarqueeToolEntry());
		toolbar.add(new ConnectionCreationToolEntry("Connection", "Creates a new connection.", new ConnectionFactory(), Activator.getImage("graph_edge_directed_16.png"), Activator.getImage("graph_edge_directed_32.png")));
		palette.add(toolbar);
	}

	public static void createOperatorsGroup(PaletteRoot palette) {
		PaletteGroup group = new PaletteGroup("Controls");
		group.add(new PaletteSeparator());
		group.add(new CreationToolEntry("Image", "An image that is shown if the predicate is true", new PictogramFactory(ImagePictogram.class), Activator.getImage("image.png"), null));
		group.add(new CreationToolEntry("Multiple Images", "A list of images where a predicate selects the image", new PictogramFactory(MultipleImagePictogram.class), Activator.getImage("images.png"), null));
		palette.add(group);
	}
}
