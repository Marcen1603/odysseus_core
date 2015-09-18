package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;



public abstract class AbstractIQLServiceObserver  implements IIQLServiceObserver, IQLServiceBinding.Listener {

	
	public AbstractIQLServiceObserver() {
		IQLServiceBinding.getInstance().addListener(this);
		for (IIQLService service : getServices()) {
			onServiceAdded(service);
		}
	}
	
	protected abstract Collection<IIQLService> getServices();

	@Override
	public void onServiceAdded(IIQLService service) {
		
	}

	@Override
	public void onServiceRemoved(IIQLService service) {
		
	}
	
	@Override
	public Collection<Bundle> getRequiredBundles() {
		Collection<Bundle> bundles = new HashSet<>();
		for (IIQLService service : getServices()) {
			if (service.getRequiredBundles() != null) {
				for (Bundle bundle : service.getRequiredBundles()) {
					bundles.add(bundle);
				}
			}			
		}
		return bundles;
	}
	
	@Override
	public Collection<String> getImportedPackages() {
		Collection<String> packages = new HashSet<>();
		for (IIQLService service : getServices()) {
			if (service.getImportedPackages() != null) {
				for (String p : service.getImportedPackages()) {
					packages.add(p);
				}
			}			
		}
		return packages;
	}
	
	@Override
	public Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> result = new HashSet<>();
		for (IIQLService service : getServices()) {
			if (service.getVisibleTypes() != null) {
				for (Class<?> type : service.getVisibleTypes()) {
					result.add(type);
				}
			}
		}
		return result;
	}
	
	@Override
	public Collection<Bundle> getVisibleTypesFromBundle() {
		Collection<Bundle> result = new HashSet<>();
		for (IIQLService service : getServices()) {
			if (service.getVisibleTypesFromBundle() != null) {
				result.addAll(service.getVisibleTypesFromBundle());
			}
		}
		return result;
	}
	
	

	
	@Override
	public Collection<String> getImplicitImports() {
		Collection<String> result = new HashSet<>();
		for (IIQLService service : getServices()) {
			if (service.getImplicitImports() != null) {
				for (String implicitImport : service.getImplicitImports()) {
					result.add(implicitImport);
				}
			}
		}
		return result;
	}
	
	@Override
	public Collection<String> getImplicitStaticImports() {
		Collection<String> result = new HashSet<>();
		for (IIQLService service : getServices()) {
			if (service.getImplicitStaticImports() != null) {
				for (String type : service.getImplicitStaticImports()) {
					result.add(type);
				}
			}
		}
		return result;
	}
	
	@Override
	public Map<Class<? extends IParameter<?>>, Class<?>> getParameters() {
		Map<Class<? extends IParameter<?>>, Class<?>> result = new HashMap<>();
		for (IIQLService service : getServices()) {
			if (service.getParameters() != null) {
				for (ParameterPair pair : service.getParameters()) {
					result.put(pair.getParameterType(), pair.getParameterValueType());
				}
			}
		}
		return result;
	}
}
