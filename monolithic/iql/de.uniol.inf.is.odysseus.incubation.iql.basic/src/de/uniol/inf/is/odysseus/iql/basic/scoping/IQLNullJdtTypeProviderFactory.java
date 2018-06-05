package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;

public class IQLNullJdtTypeProviderFactory implements IIQLJdtTypeProviderFactory{

	@Override
	public IIQLJdtTypeProvider createJdtTypeProvider(ResourceSet resourceSet) {
		return null;
	}

}
