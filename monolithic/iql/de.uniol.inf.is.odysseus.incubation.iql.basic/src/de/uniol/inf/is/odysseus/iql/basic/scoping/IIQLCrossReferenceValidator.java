package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;

public interface IIQLCrossReferenceValidator {
	boolean isValidCrossReference(Resource from, IEObjectDescription to);
}
