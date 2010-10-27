package de.uniol.inf.is.odysseus.rcp.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.rcp.editor.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorFactory;

public class PaletteFactory {
	static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		createDrawers(palette);
		return palette;
	}

	private static void createDrawers(PaletteRoot root) {
		List<String> builderNames = new ArrayList<String>(OperatorBuilderFactory.getOperatorBuilderNames());

		Collections.sort(builderNames);
		
		ImageDescriptor imageDesc = Activator.getImageDescriptor("icons/operatorIcon.png");
		PaletteDrawer drawer = new PaletteDrawer("Operators");

		for (String builderName : builderNames) {
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					builderName, 
					builderName,
					builderName, 
					new OperatorFactory(builderName), 
					imageDesc, 
					imageDesc);
			drawer.add(component);
		}

		root.add(drawer);
	}

	/** Create the "Tools" group. */
	private static PaletteContainer createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());

		// Add (solid-line) connection tool
		ImageDescriptor imageDesc = Activator.getImageDescriptor("icons/connection.gif");
		tool = new ConnectionCreationToolEntry("Connection", "Create a connection", new CreationFactory() {
			@Override
			public Object getNewObject() {
				return null;
			}

			// see ShapeEditPart#createEditPolicies()
			// this is abused to transmit the desired line style
			@Override
			public Object getObjectType() {
				return new Integer(Graphics.LINE_SOLID);
			}
		}, imageDesc, imageDesc);
		toolbar.add(tool);

		return toolbar;
	}
}
