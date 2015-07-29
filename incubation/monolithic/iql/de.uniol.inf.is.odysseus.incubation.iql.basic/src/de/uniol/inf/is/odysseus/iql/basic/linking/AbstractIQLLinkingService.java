package de.uniol.inf.is.odysseus.iql.basic.linking;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.linking.impl.IllegalNodeException;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;

public abstract class AbstractIQLLinkingService extends DefaultLinkingService{

	@Inject
	protected IQLQualifiedNameConverter qualifiedNameConverter;
	
	@Override
	public List<EObject> getLinkedObjects(EObject context, EReference ref, INode node) throws IllegalNodeException {
		if (context instanceof IQLMethodSelection) {
			return getLinkedObjectsIQLMethodSelection(context, ref, node);
		} else {
			return super.getLinkedObjects(context, ref, node);
		}
	}
	
	protected List<EObject> getLinkedObjectsIQLMethodSelection(EObject context, EReference ref, INode node) throws IllegalNodeException {
		IQLMethodSelection methodSel = (IQLMethodSelection) context;
		IScope scope = getScope(context, ref);
		String crossRefString = getCrossRefNodeAsString(node);
		QualifiedName qualifiedLinkName =  qualifiedNameConverter.toQualifiedName(crossRefString);
		Iterable<IEObjectDescription> eObjectDescriptions = scope.getElements(qualifiedLinkName);
		for (IEObjectDescription desc : eObjectDescriptions) {
			JvmOperation method = (JvmOperation) desc.getEObjectOrProxy();
			if (methodSel.getArgs().getElements().size() == method.getParameters().size()) {
				return Collections.singletonList(desc.getEObjectOrProxy());
			}
		}
		return null;
	}
}
