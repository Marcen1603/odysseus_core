package de.uniol.inf.is.odysseus.iql.qdl.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.service.AbstractIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLService;

@Singleton
public class QDLServiceObserver extends AbstractIQLServiceObserver{

	@Inject
	public QDLServiceObserver() {
		super();
		QDLServiceBinding.getInstance().addListener(this);
		for (IIQLService service : QDLServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}
	
	@Override
	public void onServiceAdded(IIQLService service) {
		if (service instanceof IQDLService) {
			IQDLService qdlService = (IQDLService) service;
			System.out.println(qdlService);
		}
		super.onServiceAdded(service);
	}

	@Override
	public void onServiceRemoved(IIQLService service) {
		if (service instanceof IQDLService) {
			IQDLService qdlService = (IQDLService) service;
			System.out.println(qdlService);
		}
		super.onServiceRemoved(service);
	}
	
	

}
