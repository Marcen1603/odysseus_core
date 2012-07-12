package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

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
					Bundle bundle = PQLEditorTextPlugIn.getDefault().getBundle();
					ImageRegistry imageRegistry = PQLEditorTextPlugIn.getDefault().getImageRegistry();
					imageRegistry.put("pqlOperator", ImageDescriptor.createFromURL(bundle.getEntry("icons/operator.png")));
					imageRegistry.put("pqlAttribute", ImageDescriptor.createFromURL(bundle.getEntry("icons/manParameter.png")));
					imageRegistry.put("pqlOptionalAttribute", ImageDescriptor.createFromURL(bundle.getEntry("icons/optParameter.png")));
				} catch (SWTError e) {
					// Ignore
				}
			}

		});
	}

}
