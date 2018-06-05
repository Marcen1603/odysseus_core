package de.uniol.inf.is.odysseus.iql.basic.ui.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.access.jdt.NullJdtTypeProvider;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;

public class IQLNullJdtTypeProvider extends NullJdtTypeProvider implements IIQLJdtTypeProvider{

	public IQLNullJdtTypeProvider(ResourceSet resourceSet) {
		super(resourceSet);
	}
}
