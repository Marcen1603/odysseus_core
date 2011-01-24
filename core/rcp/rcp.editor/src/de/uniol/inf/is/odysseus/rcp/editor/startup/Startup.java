package de.uniol.inf.is.odysseus.rcp.editor.startup;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.editor.OdysseusRCPEditorPlugIn;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				OdysseusRCPEditorPlugIn.getDefault().getImageRegistry().put("operatorIcon", OdysseusRCPEditorPlugIn.getImageDescriptor("icons/operatorIcon.png"));
				OdysseusRCPEditorPlugIn.getDefault().getImageRegistry().put("operatorGroupIcon", OdysseusRCPEditorPlugIn.getImageDescriptor("icons/operatorGroupIcon.png"));
				OdysseusRCPEditorPlugIn.getDefault().getImageRegistry().put("connectionIcon", OdysseusRCPEditorPlugIn.getImageDescriptor("icons/connection.gif"));
			}
		});
	}

}
