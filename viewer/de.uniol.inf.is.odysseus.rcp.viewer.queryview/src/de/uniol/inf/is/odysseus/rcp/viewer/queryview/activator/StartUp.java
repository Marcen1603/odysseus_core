package de.uniol.inf.is.odysseus.rcp.viewer.queryview.activator;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
//				try{
//				Bundle bundle = Activator.getDefault().getBundle();
//				ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
//				imageRegistry.put("metadata", ImageDescriptor.createFromURL(bundle.getEntry("icons/viewicon.png")));
//				}catch(SWTError e){
//					// Ignore :-)
//				}
			}
			
		});	}

}
