package de.uniol.inf.is.odysseus.rcp.editor.script.impl;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.ImageManager;

public class VisualOdysseusScriptPlugIn extends AbstractUIPlugin {

	private static ImageManager imageManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		imageManager = new ImageManager(context.getBundle());

		loadImages();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		imageManager.disposeAll();
		imageManager = null;
	}

	public static ImageManager getImageManager() {
		return imageManager;
	}

	private static void loadImages() {
		imageManager.register("add", "icons/add.png");
		imageManager.register("remove", "icons/remove.png");
		imageManager.register("moveUp", "icons/moveUp.gif");
		imageManager.register("moveDown", "icons/moveDown.gif");
		
		imageManager.register("edit", "icons/edit.png");
		
		imageManager.register("expand", "icons/expand.png");
		imageManager.register("expandAll", "icons/expandAll.png");
		imageManager.register("collapse", "icons/collapse.png");
		imageManager.register("collapseAll", "icons/collapseAll.png");
	}

}
