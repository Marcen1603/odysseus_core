package de.uniol.inf.is.odysseus.rcp.viewer.view.activator;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.resource.XMLResourceLoader;


public class Startup implements IStartup {

	@Override
	public void earlyStartup() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = Activator.getDefault().getBundle();
				XMLResourceLoader.loadImages(
						bundle.getEntry("viewer_cfg/resources.xml"), 
						bundle.getEntry("viewer_cfg/resourcesSchema.xsd"),
						Activator.getDefault().getImageRegistry());
			}
			
		});
	}

}
