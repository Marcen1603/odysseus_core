package de.uniol.inf.is.odysseus.iql.odl.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.service.AbstractIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLService;

@Singleton
public class ODLServiceObserver extends AbstractIQLServiceObserver{

	@Inject
	public ODLServiceObserver() {
		super();
		ODLServiceBinding.getInstance().addListener(this);
		for (IIQLService service : ODLServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}
	
	@Override
	public void onServiceAdded(IIQLService service) {
		if (service instanceof IODLService) {
			IODLService qdlService = (IODLService) service;
			System.out.println(qdlService);
		}
		super.onServiceAdded(service);
	}

	@Override
	public void onServiceRemoved(IIQLService service) {
		if (service instanceof IODLService) {
			IODLService qdlService = (IODLService) service;
			System.out.println(qdlService);
		}
		super.onServiceRemoved(service);
	}

}
