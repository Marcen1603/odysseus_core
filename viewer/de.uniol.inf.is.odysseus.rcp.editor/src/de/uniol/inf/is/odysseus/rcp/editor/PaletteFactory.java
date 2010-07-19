package de.uniol.inf.is.odysseus.rcp.editor;

import java.util.Collection;

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

import de.uniol.inf.is.odysseus.rcp.editor.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorFactory;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptorList;
import de.uniol.inf.is.odysseus.rcp.editor.operator.OperatorExtensionRegistry;

public class PaletteFactory {
	static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		createDrawers(palette);
		return palette;
	}
	
	private static void createDrawers(PaletteRoot root ) {
		IOperatorExtensionDescriptorList extensions = OperatorExtensionRegistry.getInstance().getExtensions();
		Collection<String> groups = OperatorExtensionRegistry.getInstance().getGroups();
		
		ImageDescriptor imageDesc = Activator.getImageDescriptor("icons/operatorIcon.png");
		for( String group : groups ) {
			PaletteDrawer drawer = new PaletteDrawer(group);
			
			for( IOperatorExtensionDescriptor desc : extensions.getGroup(group)) {
				CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
						desc.getLabel(), 
						desc.getLabel(), 
						desc,
						new OperatorFactory(desc), 
						imageDesc,
						imageDesc);
				drawer.add(component);
			}
			
			root.add(drawer);
		}
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
		tool = new ConnectionCreationToolEntry(
				"Connection",
				"Create a connection",
				new CreationFactory() {
					public Object getNewObject() { return null; }
					// see ShapeEditPart#createEditPolicies() 
					// this is abused to transmit the desired line style 
					public Object getObjectType() { return new Integer(Graphics.LINE_SOLID); }
				},
				imageDesc,
				imageDesc);
		toolbar.add(tool);

		return toolbar;
	}
}
