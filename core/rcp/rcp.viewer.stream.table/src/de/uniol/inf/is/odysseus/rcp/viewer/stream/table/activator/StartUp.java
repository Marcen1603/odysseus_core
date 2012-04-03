package de.uniol.inf.is.odysseus.rcp.viewer.stream.table.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Bundle bundle = ViewerStreamTablePlugIn.getDefault().getBundle();
					ImageRegistry imageRegistry = ViewerStreamTablePlugIn.getDefault().getImageRegistry();
					imageRegistry.put("filter", ImageDescriptor.createFromURL(bundle.getEntry("icons/filter.png")));
				} catch (SWTError e) {
					// Ignore :-)
				}
			}

		});

	}

}
