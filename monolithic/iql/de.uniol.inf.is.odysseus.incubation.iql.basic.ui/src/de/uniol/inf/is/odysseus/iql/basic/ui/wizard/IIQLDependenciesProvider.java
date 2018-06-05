package de.uniol.inf.is.odysseus.iql.basic.ui.wizard;

import java.util.Collection;

public interface IIQLDependenciesProvider {
	Collection<String> getRequiredBundles();
	Collection<String> getImportedPackages();

}
