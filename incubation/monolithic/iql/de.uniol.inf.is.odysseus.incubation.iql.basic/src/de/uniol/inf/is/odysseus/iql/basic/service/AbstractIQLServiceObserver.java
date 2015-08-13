package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.Platform;
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
		Collection<Bundle> bundles = new HashSet<>();
		for (IIQLService service : IQLServiceBinding.getInstance().getServices()) {
			for (String dep : service.getDependencies()) {
				Bundle bundle = Platform.getBundle(dep);
				if (bundle != null) {
					bundles.add(bundle);
				}
			}
		}
		return bundles;
	}
	
	@Override
	public Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> result = new HashSet<>();
		for (IIQLService service : IQLServiceBinding.getInstance().getServices()) {
			for (Class<?> type : service.getVisibleTypes()) {
				result.add(type);
			}
		}
		return result;
	}
	@Override
	public Collection<String> getImplicitImports() {
		Collection<String> result = new HashSet<>();
		for (IIQLService service : IQLServiceBinding.getInstance().getServices()) {
			for (String implicitImport : service.getImplicitImports()) {
				result.add(implicitImport);
			}
		}
		return result;
	}
	
	@Override
	public Collection<Class<?>> getImplicitStaticImports() {
		Collection<Class<?>> result = new HashSet<>();
		for (IIQLService service : IQLServiceBinding.getInstance().getServices()) {
			for (Class<?> type : service.getImplicitStaticImports()) {
				result.add(type);
			}
		}
		return result;
	}
}
