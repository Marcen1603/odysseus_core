package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;

public interface IIQLJdtTypeProviderFactory {

	IIQLJdtTypeProvider createJdtTypeProvider(ResourceSet resourceSet);

}
