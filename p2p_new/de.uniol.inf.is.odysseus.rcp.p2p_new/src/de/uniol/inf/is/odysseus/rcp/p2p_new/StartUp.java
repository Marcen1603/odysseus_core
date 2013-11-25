package de.uniol.inf.is.odysseus.rcp.p2p_new;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PNetworkManagerService;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					window.getShell().setText( window.getShell().getText() + " - " + determineP2PTitleSuffix());
				}
			}

		});
	}

	private static String determineP2PTitleSuffix() {
		return P2PNetworkManagerService.get().getLocalPeerGroupName() + ":" + P2PNetworkManagerService.get().getLocalPeerName();
	}
}
