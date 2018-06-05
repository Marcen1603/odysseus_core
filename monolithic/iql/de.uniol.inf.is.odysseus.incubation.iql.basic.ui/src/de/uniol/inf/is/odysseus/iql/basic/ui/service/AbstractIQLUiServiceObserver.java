package de.uniol.inf.is.odysseus.iql.basic.ui.service;

public abstract class AbstractIQLUiServiceObserver implements IIQLUiServiceObserver, IQLUiServiceBinding.Listener {

	public AbstractIQLUiServiceObserver() {
		IQLUiServiceBinding.getInstance().addListener(this);
		for (IIQLUiService service : IQLUiServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}
	
	@Override
	public void onServiceAdded(IIQLUiService service) {
		
	}

	@Override
	public void onServiceRemoved(IIQLUiService service) {
		
	}
}
