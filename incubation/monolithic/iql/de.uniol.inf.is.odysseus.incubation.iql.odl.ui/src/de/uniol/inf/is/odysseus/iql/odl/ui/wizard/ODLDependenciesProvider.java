package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.iql.basic.ui.wizard.IIQLDependenciesProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.ui.internal.ODLActivator;

public class ODLDependenciesProvider implements IIQLDependenciesProvider{

	@Override
	public Collection<String> getDependencies() {
		Injector injector =  ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL);
		IODLTypeDictionary typeDictionary = injector.getInstance(IODLTypeDictionary.class);
		
		Collection<String> result = new HashSet<>();
		for (Bundle bundle : typeDictionary.getDependencies()) {
			result.add(bundle.getSymbolicName());
		}
		return result;
	}

}
