package de.uniol.inf.is.odysseus.iql.qdl.ui.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.ui.service.AbstractIQLUiServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.ui.service.IIQLUiService;

@Singleton
public class QDLUiServiceObserver extends AbstractIQLUiServiceObserver implements IQDLUiServiceObserver{

	@Inject
	public QDLUiServiceObserver() {
		super();
		QDLUiServiceBinding.getInstance().addListener(this);
		for (IIQLUiService service : QDLUiServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}
	
	@Override
	public void onServiceAdded(IIQLUiService service) {
		super.onServiceAdded(service);
	}

	@Override
	public void onServiceRemoved(IIQLUiService service) {
		super.onServiceRemoved(service);
	}
	
	

}
