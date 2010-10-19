package de.uniol.inf.is.odysseus.rcp.editor.parameters.activator;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec( new Runnable() {
			@Override
			public void run() {
				Activator.getDefault().getImageRegistry().put("addIcon", Activator.getImageDescriptor("icons/add.gif"));
				Activator.getDefault().getImageRegistry().put("removeIcon", Activator.getImageDescriptor("icons/remove.gif"));
			}
		});
	}

}
