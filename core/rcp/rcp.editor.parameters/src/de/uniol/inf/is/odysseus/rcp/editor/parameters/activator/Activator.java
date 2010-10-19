package de.uniol.inf.is.odysseus.rcp.editor.parameters.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.editor.parameters.IParametersConstants;

public class Activator extends AbstractUIPlugin {

	private static Activator instance;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		instance = null;
	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(IParametersConstants.PLUGIN_ID, path);
	}
	
	public static Activator getDefault() {
		return instance;
	}
}
