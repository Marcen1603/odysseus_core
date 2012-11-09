package de.uniol.inf.is.odysseus.rcp.ac;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.ImageManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class ACRCPPlugIn extends AbstractUIPlugin {

	private static ImageManager imageManager;
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		imageManager = new ImageManager(context.getBundle());
		loadImages(imageManager);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		imageManager.disposeAll();
		imageManager = null;
	}

	private static void loadImages(ImageManager imageManager) {
		Preconditions.checkNotNull(imageManager, "ImageManager to load images must not be null!");
		
	}
}
