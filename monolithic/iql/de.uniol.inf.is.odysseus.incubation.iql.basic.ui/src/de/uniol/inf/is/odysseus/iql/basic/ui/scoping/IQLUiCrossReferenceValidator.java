package de.uniol.inf.is.odysseus.iql.basic.ui.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLCrossReferenceValidator;

public class IQLUiCrossReferenceValidator implements IIQLCrossReferenceValidator{

	@Override
	public boolean isValidCrossReference(Resource from, IEObjectDescription to) {
		URI fromUri = from.getURI();
		URI toUri = to.getEObjectURI();
		if (fromUri.isPlatform() && toUri.isPlatform()) {
			if (fromUri.segment(0) == toUri.segment(0) && fromUri.segment(1) == toUri.segment(1)) {
				return true;
			} else {
				return false;
			}
		}		
		return true;
	}
	
}
