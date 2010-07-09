package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				Activator.getDefault().getImageRegistry().put("operatorIcon", Activator.getImageDescriptor("icons/operatorIcon.png"));
				Activator.getDefault().getImageRegistry().put("operatorGroupIcon", Activator.getImageDescriptor("icons/operatorGroupIcon.png"));
			}
		});
	}

}
