package de.uniol.inf.is.odysseus.rcp.p2p_new;

import org.eclipse.ui.IStartup;

import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PNetworkManagerService;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		RCPP2PNewPlugIn.setWindowTitleSuffix(RCPP2PNewPlugIn.determineP2PTitleSuffix(P2PNetworkManagerService.get()));
	}
}
