package de.uniol.inf.is.odysseus.rcp.editor.navigator.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				loadResources();
			}
		});
	}
	
	private static void loadResources() {
		Activator.getDefault().getImageRegistry().put("project", getImageDescriptor("Icons/project.gif"));
		Activator.getDefault().getImageRegistry().put("plan", getImageDescriptor("Icons/plan.png"));
	}

	private static ImageDescriptor getImageDescriptor( String filename ) {
		return ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry(filename));
	}
}
