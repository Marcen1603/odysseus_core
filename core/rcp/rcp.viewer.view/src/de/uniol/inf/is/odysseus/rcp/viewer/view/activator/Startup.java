package de.uniol.inf.is.odysseus.rcp.viewer.view.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
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
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try{
				Bundle bundle = Activator.getDefault().getBundle();
				ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
				imageRegistry.put("metadata", ImageDescriptor.createFromURL(bundle.getEntry("icons/metadata.gif")));
				imageRegistry.put("attribute", ImageDescriptor.createFromURL(bundle.getEntry("icons/status.png")));
				imageRegistry.put("predicate", ImageDescriptor.createFromURL(bundle.getEntry("icons/status.png")));
				imageRegistry.put("schema", ImageDescriptor.createFromURL(bundle.getEntry("icons/odata.png")));
				imageRegistry.put("pipe_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/task.png")));
				imageRegistry.put("sink_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/monitor.png")));
				imageRegistry.put("source_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/application-import.png")));
				imageRegistry.put("partof_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/graph.jpg")));
				imageRegistry.put("subscription", ImageDescriptor.createFromURL(bundle.getEntry("icons/arrow-000-small.png")));
				}catch(SWTError e){
					// Ignore :-)
				}
			}
			
		});

	}

}
