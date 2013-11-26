package de.uniol.inf.is.odysseus.rcp.p2p_new;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class RCPP2PNewPlugIn extends AbstractUIPlugin {

	public static final String NO_P2P_CONNECTION_TEXT = "<no p2p connection>";

	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
	
	public static String determineP2PTitleSuffix(IP2PNetworkManager manager) {
		return manager.getLocalPeerGroupName() + ":" + manager.getLocalPeerName();
	}
	
	public static void setWindowTitleSuffix(final String titleSuffix) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					window.getShell().setText( window.getShell().getText() + " - " + titleSuffix);
				}
			}
		});
	}
}
