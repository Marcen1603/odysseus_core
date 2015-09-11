package de.uniol.inf.is.odysseus.iql.basic.ui.wizard;

import java.util.Collection;
import java.util.HashSet;

public class IQLDependenciesProviderServiceBinding {

	private static Collection<IIQLDependenciesProvider> providers = new HashSet<>();

	
	public static void bindIQLDependenciesProvider(IIQLDependenciesProvider provider) {
		providers.add(provider);
	}
	
	public static void unbindIQLDependenciesProvider(IIQLDependenciesProvider provider) {
		providers.add(provider);
	}
	
	
	public static Collection<IIQLDependenciesProvider> getProviders() {
		return providers;
	}
	
}
