package de.uniol.inf.is.odysseus.iql.qdl.service;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.service.AbstractIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLService;
import de.uniol.inf.is.odysseus.iql.basic.service.IQLServiceBinding;

@Singleton
public class QDLServiceObserver extends AbstractIQLServiceObserver{

	@Inject
	public QDLServiceObserver() {
		super();
		QDLServiceBinding.getInstance().addListener(this);
	}
	
	@Override
	public void onServiceAdded(IIQLService service) {
		super.onServiceAdded(service);
	}

	@Override
	public void onServiceRemoved(IIQLService service) {
		super.onServiceRemoved(service);
	}
	
	@Override
	protected Collection<IIQLService> getServices() {
		Collection<IIQLService> result = new HashSet<>();
		result.addAll(IQLServiceBinding.getInstance().getServices());
		result.addAll(QDLServiceBinding.getInstance().getServices());
		return result;
	}
	
	

}
