package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.IAcceptor;

import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLResourceDescriptionStrategy;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

public class QDLResourceDescriptionStrategy extends AbstractIQLResourceDescriptionStrategy {

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		if (eObject instanceof QDLQuery) {
			return false;
		} else {
			return super.createEObjectDescriptions(eObject, acceptor);
		}
	}
}
