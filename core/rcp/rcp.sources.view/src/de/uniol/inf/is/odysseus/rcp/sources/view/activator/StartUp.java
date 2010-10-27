package de.uniol.inf.is.odysseus.rcp.sources.view.activator;

import org.eclipse.ui.IStartup;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
//		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
//
//			@Override
//			public void run() {
//				try{
//				Bundle bundle = Activator.getDefault().getBundle();
//				ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
//				imageRegistry.put("source", ImageDescriptor.createFromURL(bundle.getEntry("Icons/repository_rep.gif")));
//				imageRegistry.put("attribute", ImageDescriptor.createFromURL(bundle.getEntry("Icons/attribute.png")));
//				}catch(SWTError e){
//					// Ignore :-)
//				}
//			}
//			
//		});
	}

}
