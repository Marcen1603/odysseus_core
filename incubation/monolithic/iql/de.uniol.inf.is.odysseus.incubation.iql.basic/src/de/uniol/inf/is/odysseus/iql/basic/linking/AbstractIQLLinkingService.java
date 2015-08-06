package de.uniol.inf.is.odysseus.iql.basic.linking;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.linking.impl.IllegalNodeException;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.IEObjectDescription;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLLinkingService extends DefaultLinkingService{

	@Inject
	protected IQLQualifiedNameConverter qualifiedNameConverter;
	
	@Inject
	protected IIQLScopeProvider scopeProvider;
	
	@Inject
	protected IIQLTypeUtils typeUtils;
	
	@Override
	public List<EObject> getLinkedObjects(EObject context, EReference ref, INode node) throws IllegalNodeException {
		if (context instanceof IQLMemberSelection) {
			return getLinkedObjectsIQLMemberSelection((IQLMemberSelection)context, ref, node);
		} else if (context instanceof IQLJvmElementCallExpression) {
			return getLinkedObjectsIQLJvmElementCallExpression((IQLJvmElementCallExpression)context, ref, node);
		} else {
			return super.getLinkedObjects(context, ref, node);
		}
	}
	
	protected List<EObject> getLinkedObjectsIQLMemberSelection(IQLMemberSelection expr, EReference ref, INode node) throws IllegalNodeException {
		Collection<IEObjectDescription> eObjectDescriptions = scopeProvider.getScopeIQLMemberSelection((IQLMemberSelectionExpression) expr.eContainer());
		String crossRefString = getCrossRefNodeAsString(node);
		EObject result = null;
		for (IEObjectDescription desc : eObjectDescriptions) {
			if (qualifiedNameConverter.toString(desc.getQualifiedName()).equalsIgnoreCase(crossRefString)) {
				EObject obj = desc.getEObjectOrProxy();
				if (obj instanceof JvmField) {
					result = obj;
				} else if (obj instanceof JvmOperation) {
					JvmOperation op = (JvmOperation) obj;
					if(expr.getArgs() != null && expr.getArgs().getElements().size() == op.getParameters().size()) {
						result = obj;
						break;
					} else if (typeUtils.isGetter(op) && !(expr.eContainer().eContainer() instanceof IQLAssignmentExpression)) {
						result = obj;
					} else if (typeUtils.isSetter(op) && (expr.eContainer().eContainer() instanceof IQLAssignmentExpression)) {
						result = obj;
					} else if (result == null) {
						result = obj;
					}
				} 
			}
		}
		return Collections.singletonList(result);
	}
	
	protected List<EObject> getLinkedObjectsIQLJvmElementCallExpression(IQLJvmElementCallExpression expr, EReference ref, INode node) throws IllegalNodeException {
		Collection<IEObjectDescription> eObjectDescriptions = scopeProvider.getIQLJvmElementCallExpression(expr);
		String crossRefString = getCrossRefNodeAsString(node);
		EObject result = null;
		for (IEObjectDescription desc : eObjectDescriptions) {
			if (qualifiedNameConverter.toString(desc.getQualifiedName()).equalsIgnoreCase(crossRefString)) {
				EObject obj = desc.getEObjectOrProxy();
				if (obj instanceof IQLVariableDeclaration) {
					result = obj;
				} else if (obj instanceof JvmFormalParameter) {
					if (result == null) {
						result = obj;
					}
				} else if (obj instanceof JvmField) {
					if (result == null) {
						result = obj;
					}
				} else if (obj instanceof JvmOperation) {
					JvmOperation op = (JvmOperation) obj;
					if(expr.getArgs() != null && expr.getArgs().getElements().size() == op.getParameters().size()) {
						result = obj;
						break;
					} else if (typeUtils.isGetter(op) && !(expr.eContainer().eContainer() instanceof IQLAssignmentExpression)) {
						result = obj;
					} else if (typeUtils.isSetter(op) && (expr.eContainer().eContainer() instanceof IQLAssignmentExpression)) {
						result = obj;
					} else if (result == null) {
						result = obj;
					}
				}
			}
		}
		return Collections.singletonList(result);
	}
}
