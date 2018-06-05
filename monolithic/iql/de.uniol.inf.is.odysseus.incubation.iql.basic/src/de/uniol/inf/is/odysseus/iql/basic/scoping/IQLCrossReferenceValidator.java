package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;

public class IQLCrossReferenceValidator implements IIQLCrossReferenceValidator{

	@Override
	public boolean isValidCrossReference(Resource from, IEObjectDescription to) {
		return true;
	}
	
}
