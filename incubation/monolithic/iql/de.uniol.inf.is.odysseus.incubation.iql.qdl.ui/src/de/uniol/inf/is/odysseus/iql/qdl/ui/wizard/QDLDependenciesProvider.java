package de.uniol.inf.is.odysseus.iql.qdl.ui.wizard;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.iql.basic.ui.wizard.IIQLDependenciesProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.ui.internal.QDLActivator;

public class QDLDependenciesProvider implements IIQLDependenciesProvider{

	@Override
	public Collection<String> getDependencies() {
		Injector injector =  QDLActivator.getInstance().getInjector(QDLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL);
		IQDLTypeDictionary typeDictionary = injector.getInstance(IQDLTypeDictionary.class);
		
		Collection<String> result = new HashSet<>();
		for (Bundle bundle : typeDictionary.getDependencies()) {
			result.add(bundle.getSymbolicName());
		}
		return result;
	}

}
