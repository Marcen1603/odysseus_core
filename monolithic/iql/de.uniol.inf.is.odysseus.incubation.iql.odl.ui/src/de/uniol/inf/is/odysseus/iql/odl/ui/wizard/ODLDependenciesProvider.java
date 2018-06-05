package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.incubation.iql.odl.ui.internal.OdlActivator;
import de.uniol.inf.is.odysseus.iql.basic.ui.wizard.IIQLDependenciesProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;

public class ODLDependenciesProvider implements IIQLDependenciesProvider{

	@Override
	public Collection<String> getRequiredBundles() {
		Injector injector =  OdlActivator.getInstance().getInjector(OdlActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL);
		IODLTypeDictionary typeDictionary = injector.getInstance(IODLTypeDictionary.class);
		
		Collection<String> result = new HashSet<>();
		for (Bundle bundle : typeDictionary.getRequiredBundles()) {
			result.add(bundle.getSymbolicName());
		}
		return result;
	}

	@Override
	public Collection<String> getImportedPackages() {
		Injector injector =  OdlActivator.getInstance().getInjector(OdlActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL);
		IODLTypeDictionary typeDictionary = injector.getInstance(IODLTypeDictionary.class);
		
		Collection<String> result = new HashSet<>();
		for (String p : typeDictionary.getImportedPackages()) {
			result.add(p);
		}
		return result;
	}

}
