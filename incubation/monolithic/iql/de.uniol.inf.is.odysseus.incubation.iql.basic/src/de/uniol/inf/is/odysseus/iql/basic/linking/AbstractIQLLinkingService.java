package de.uniol.inf.is.odysseus.iql.basic.linking;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
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
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLMethodFinder;
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
	
	@Inject
	protected IIQLMethodFinder methodFinder;
	
	@Inject
	protected IIQLLookUp lookUp;
	
	@Override
	public List<EObject> getLinkedObjects(EObject context, EReference ref, INode node) throws IllegalNodeException {
		List<EObject> result = null;
		if (context instanceof IQLMemberSelection) {
			result = getLinkedObjectsIQLMemberSelection((IQLMemberSelection)context, ref, node);
		} else if (context instanceof IQLJvmElementCallExpression) {
			result =  getLinkedObjectsIQLJvmElementCallExpression((IQLJvmElementCallExpression)context, ref, node);
		}
		if (result == null || result.isEmpty()) {
			return super.getLinkedObjects(context, ref, node);
		} else {
			return result;
		}
	}
	
	protected List<EObject> getLinkedObjectsIQLMemberSelection(IQLMemberSelection expr, EReference ref, INode node) throws IllegalNodeException {
		IQLMemberSelectionExpression container = (IQLMemberSelectionExpression) expr.eContainer();
		Collection<IEObjectDescription> eObjectDescriptions = scopeProvider.getScopeIQLMemberSelection(container);
		String crossRefString = getCrossRefNodeAsString(node);
		EObject result = null;
		for (IEObjectDescription desc : eObjectDescriptions) {
			if (qualifiedNameConverter.toString(desc.getQualifiedName()).equalsIgnoreCase(crossRefString)) {
				EObject obj = desc.getEObjectOrProxy();
				if (obj instanceof JvmField) {
					result = obj;
					break;
				} 
			}
		}
		if (result == null) {
			Collection<JvmOperation> methods = new HashSet<>();
			for (IEObjectDescription desc : eObjectDescriptions) {
				EObject obj = desc.getEObjectOrProxy();
				if (obj instanceof JvmOperation) {
					methods.add((JvmOperation) obj);
				}
			}
			if (expr.getArgs() != null) {				
				result = methodFinder.findMethod(methods, crossRefString, expr.getArgs().getElements());
			} else if (!isAssignment(expr)) {
				result = methodFinder.findMethod(methods, "get"+crossRefString, 0);
				if (result == null) {
					result = methodFinder.findMethod(methods, "is"+crossRefString, 0);
				}
			} else if (isAssignment(expr)) {
				result = methodFinder.findMethod(methods, "set"+crossRefString,1 );
			} 
			if (result == null) {
				result = methodFinder.findMethod(methods, crossRefString);
			}
		}
		if (result != null) {
			return Collections.singletonList(result);
		} else {
			return null;
		}
	}
	
	private boolean isAssignment(EObject obj) {
		IQLAssignmentExpression expr = EcoreUtil2.getContainerOfType(obj, IQLAssignmentExpression.class);
		return expr != null;
	}
	
	
	protected List<EObject> getLinkedObjectsIQLJvmElementCallExpression(IQLJvmElementCallExpression expr, EReference ref, INode node) throws IllegalNodeException {
		Collection<IEObjectDescription> eObjectDescriptions = scopeProvider.getIQLJvmElementCallExpression(expr);
		String[] splits = getCrossRefNodeAsString(node).split(IQLQualifiedNameConverter.DELIMITER);
		String crossRefString = splits[splits.length-1];
		String type = null;
		if (splits.length>1) {
			type = splits[0];
		}

		EObject result = null;
		for (IEObjectDescription desc : eObjectDescriptions) {
			if (qualifiedNameConverter.toString(desc.getQualifiedName()).equalsIgnoreCase(crossRefString)) {
				EObject obj = desc.getEObjectOrProxy();
				if (obj instanceof IQLVariableDeclaration) {
					result = obj;
					break;
				} else if (obj instanceof JvmFormalParameter) {
					result = obj;
					break;
				} else if (obj instanceof JvmField) {
					JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(obj, JvmDeclaredType.class);
					if (type == null) {
						result = obj;
					} else if (type != null && declaredType.getSimpleName().equalsIgnoreCase(type)) {
						result = obj;
					}
				} 
			}
		}
		if (result == null) {
			Collection<JvmOperation> methods = new HashSet<>();
			for (IEObjectDescription desc : eObjectDescriptions) {
				EObject obj = desc.getEObjectOrProxy();
				if (obj instanceof JvmOperation) {
					JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(obj, JvmDeclaredType.class);
					if (type == null) {
						methods.add((JvmOperation) obj);
					} else if (type != null && declaredType.getSimpleName().equalsIgnoreCase(type)) {
						methods.add((JvmOperation) obj);
					}
				}
			}
			
			if (expr.getArgs() != null) {				
				result = methodFinder.findMethod(methods, crossRefString, expr.getArgs().getElements());
			} else if (!isAssignment(expr)) {
				result = methodFinder.findMethod(methods, "get"+crossRefString, 0);
				if (result == null) {
					result = methodFinder.findMethod(methods, "is"+crossRefString, 0);
				}
			} else if (isAssignment(expr)) {
				result = methodFinder.findMethod(methods, "set"+crossRefString, 1);
			}
			if (result == null) {
				result = methodFinder.findMethod(methods, crossRefString);
			}
		}
		if (result != null) {
			return Collections.singletonList(result);
		} else {
			return null;
		}
	}
}
