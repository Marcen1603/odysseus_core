package de.uniol.inf.is.odysseus.rcp.viewer.stream;

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
					Bundle bundle = Activator.getDefault().getBundle();
					ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
					imageRegistry.put("stopStream", ImageDescriptor.createFromURL(bundle.getEntry("Icons/stopStream.png")));
					imageRegistry.put("startStream", ImageDescriptor.createFromURL(bundle.getEntry("Icons/startStream.png")));
				} catch (SWTError e) {
					// Ignore :-)
				}
			}

		});
	}

}
