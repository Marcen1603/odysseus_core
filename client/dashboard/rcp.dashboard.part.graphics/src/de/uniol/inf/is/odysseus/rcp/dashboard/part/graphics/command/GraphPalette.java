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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.CirclePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.EllipsePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.HexagonPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.MultipleImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.OctagonPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.PentagonPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RhombPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.SquarePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.TrianglePictogram;

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
		group.add(new PaletteSeparator());
		group.add(new CreationToolEntry("Rectangle", "Creates a rectangle", new PictogramFactory(RectanglePictogram.class), Activator.getImage("shape_rectangle.png"), null));
		group.add(new CreationToolEntry("Square", "Creates a square", new PictogramFactory(SquarePictogram.class), Activator.getImage("shape_square.png"), null));
		group.add(new CreationToolEntry("Circle", "Creates a circle", new PictogramFactory(CirclePictogram.class), Activator.getImage("shape_circle.png"), null));
		group.add(new CreationToolEntry("Ellipse", "Creates an ellipse", new PictogramFactory(EllipsePictogram.class), Activator.getImage("shape_ellipse.png"), null));
		group.add(new CreationToolEntry("Triangle", "Creates a triangle", new PictogramFactory(TrianglePictogram.class), Activator.getImage("shape_triangle.png"), null));
		group.add(new CreationToolEntry("Rhomb", "Creates a rhomb", new PictogramFactory(RhombPictogram.class), Activator.getImage("shape_rhomb.png"), null));
		group.add(new CreationToolEntry("Pentagon", "Creates a pentagon", new PictogramFactory(PentagonPictogram.class), Activator.getImage("shape_pentagon.png"), null));
		group.add(new CreationToolEntry("Hexagon", "Creates a hexagon", new PictogramFactory(HexagonPictogram.class), Activator.getImage("shape_hexagon.png"), null));
		group.add(new CreationToolEntry("Octagon", "Creates a octagon", new PictogramFactory(OctagonPictogram.class), Activator.getImage("shape_octagon.png"), null));
		palette.add(group);
	}
}
