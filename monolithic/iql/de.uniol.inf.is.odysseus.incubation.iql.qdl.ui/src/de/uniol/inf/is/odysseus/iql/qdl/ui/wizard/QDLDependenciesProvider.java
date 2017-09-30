package de.uniol.inf.is.odysseus.iql.qdl.ui.wizard;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.incubation.iql.qdl.ui.internal.QdlActivator;
import de.uniol.inf.is.odysseus.iql.basic.ui.wizard.IIQLDependenciesProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;

public class QDLDependenciesProvider implements IIQLDependenciesProvider{

	@Override
	public Collection<String> getRequiredBundles() {
		Injector injector =  QdlActivator.getInstance().getInjector(QdlActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL);
		IQDLTypeDictionary typeDictionary = injector.getInstance(IQDLTypeDictionary.class);
		
		Collection<String> result = new HashSet<>();
		for (Bundle bundle : typeDictionary.getRequiredBundles()) {
			result.add(bundle.getSymbolicName());
		}
		return result;
	}

	@Override
	public Collection<String> getImportedPackages() {
		Injector injector =  QdlActivator.getInstance().getInjector(QdlActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL);
		IQDLTypeDictionary typeDictionary = injector.getInstance(IQDLTypeDictionary.class);
		
		Collection<String> result = new HashSet<>();
		for (String p : typeDictionary.getImportedPackages()) {
			result.add(p);
		}
		return result;
	}

}
