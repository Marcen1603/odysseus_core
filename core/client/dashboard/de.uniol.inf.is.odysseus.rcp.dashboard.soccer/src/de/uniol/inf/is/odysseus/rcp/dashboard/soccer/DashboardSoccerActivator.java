package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.ImageManager;

public class DashboardSoccerActivator extends AbstractUIPlugin {

	public static final String SOCCER_FIELD_IMAGE = "soccerField";
	
	private static ImageManager imageManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		imageManager = new ImageManager(context.getBundle());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		imageManager.disposeAll();
		imageManager = null;

		super.stop(context);
	}

	public static ImageManager getImageManager() {
		return imageManager;
	}
}
