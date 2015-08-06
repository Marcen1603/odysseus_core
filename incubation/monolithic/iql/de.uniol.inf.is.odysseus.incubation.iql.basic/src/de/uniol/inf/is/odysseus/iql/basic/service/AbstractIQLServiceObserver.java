package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;


public abstract class AbstractIQLServiceObserver  implements IIQLServiceObserver, IQLServiceBinding.Listener {

	
	public AbstractIQLServiceObserver() {
		IQLServiceBinding.getInstance().addListener(this);
		for (IIQLService service : IQLServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}

	@Override
	public void onServiceAdded(IIQLService service) {
		
	}

	@Override
	public void onServiceRemoved(IIQLService service) {
		
	}
	
	@Override
	public Collection<Bundle> getDependencies() {
		return new HashSet<>();
	}
	
	@Override
	public Collection<Class<?>> getVisibleTypes() {
		// TODO Auto-generated method stub
		return new HashSet<>();
	}
	
	@Override
	public Collection<String> getImplicitImports() {
		// TODO Auto-generated method stub
		return new HashSet<>();
	}
}
