package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;


public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = Activator.getDefault().getBundle();
				ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
				imageRegistry.put("metadata", ImageDescriptor.createFromURL(bundle.getEntry("images/metadata.gif")));
				imageRegistry.put("node", ImageDescriptor.createFromURL(bundle.getEntry("images/node.png")));
			}
			
		});

	}

}
