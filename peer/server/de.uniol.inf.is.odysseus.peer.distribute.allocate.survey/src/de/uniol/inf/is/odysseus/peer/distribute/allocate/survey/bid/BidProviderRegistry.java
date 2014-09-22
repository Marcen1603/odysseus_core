package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid;

import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;

public class BidProviderRegistry extends NamedInterfaceRegistry<IBidProvider> {

	private static BidProviderRegistry instance;
	
	// called by OSGi-DS
	public void bindBidProvider(IBidProvider serv) {
		add(serv);
	}

	// called by OSGi-DS
	public void unbindBidProvider(IBidProvider serv) {
		remove(serv);
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}

	public static BidProviderRegistry getInstance() {
		return instance;
	}
}
