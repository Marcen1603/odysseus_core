package de.uniol.inf.is.odysseus.rcp.viewer.graphoutline.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;


public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try{
				Bundle bundle = Activator.getDefault().getBundle();
				ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
				imageRegistry.put("metadata", ImageDescriptor.createFromURL(bundle.getEntry("images/metadata.gif")));
				imageRegistry.put("attribute", ImageDescriptor.createFromURL(bundle.getEntry("images/attribute.png")));
				imageRegistry.put("schema", ImageDescriptor.createFromURL(bundle.getEntry("images/schema.png")));
				imageRegistry.put("pipe", ImageDescriptor.createFromURL(bundle.getEntry("images/pipe.png")));
				imageRegistry.put("sink", ImageDescriptor.createFromURL(bundle.getEntry("images/sink.png")));
				imageRegistry.put("source", ImageDescriptor.createFromURL(bundle.getEntry("images/source.png")));
				}catch(SWTError e){
					// Ignore :-)
				}
			}
			
		});

	}

}
