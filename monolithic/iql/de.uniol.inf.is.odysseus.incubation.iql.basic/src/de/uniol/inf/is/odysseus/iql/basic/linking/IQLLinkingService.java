package de.uniol.inf.is.odysseus.iql.basic.linking;

import java.util.ArrayList;
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
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public class IQLLinkingService extends DefaultLinkingService{

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
		EObject result = null;
		if (context instanceof IQLMemberSelection) {
			result = getLinkedObjectIQLMemberSelection((IQLMemberSelection)context, ref, node);
		} else if (context instanceof IQLJvmElementCallExpression) {
			result = getLinkedObjectIQLJvmElementCallExpression((IQLJvmElementCallExpression)context, ref, node);
		} else if (context instanceof IQLArgumentsMapKeyValue) {
			result = getLinkedObjectIQLArgumentsMapKeyValue((IQLArgumentsMapKeyValue)context, ref, node);
		} else {
			return super.getLinkedObjects(context, ref, node);
		}	
		if (result == null) {
			return new ArrayList<EObject>();
		} else {
			return Collections.singletonList(result);
		}
	}
	
	protected EObject getLinkedObjectIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue keyValue, EReference ref, INode node) throws IllegalNodeException {
		Collection<IEObjectDescription> eObjectDescriptions = scopeProvider.getScopeIQLArgumentsMapKey(keyValue);
		String crossRefString = getCrossRefNodeAsString(node);

		EObject result = null;
		Collection<JvmOperation> methods = new HashSet<>();
		for (IEObjectDescription desc : eObjectDescriptions) {
			EObject obj = desc.getEObjectOrProxy();
			if (obj instanceof JvmField && crossRefString.equalsIgnoreCase(((JvmField) obj).getSimpleName())) {
				result = obj;
				break;
			} else if (obj instanceof JvmOperation && ("set"+crossRefString).equalsIgnoreCase(((JvmOperation) obj).getSimpleName())) {
				methods.add((JvmOperation) obj);
			}			
		}	
		if (result == null) {
			List<IQLExpression> list = new ArrayList<>();
			list.add(keyValue.getValue());
			result = methodFinder.findMethod(methods, "set"+crossRefString, list);
		}
		return result;
	}
	
	protected EObject getLinkedObjectIQLMemberSelection(IQLMemberSelection expr, EReference ref, INode node) throws IllegalNodeException {
		String crossRefString = getCrossRefNodeAsString(node);
		IQLMemberSelectionExpression e = EcoreUtil2.getContainerOfType(expr, IQLMemberSelectionExpression.class);
		
		IScope scope = scopeProvider.getScope(e, BasicIQLPackage.eINSTANCE.getIQLMemberSelection_Member());
		Iterable<IEObjectDescription> eObjectDescriptions = scope.getElements(QualifiedName.create(crossRefString));
			
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
				result = methodFinder.findMethod(methods, "get"+crossRefString);
				if (result == null) {
					result = methodFinder.findMethod(methods, "is"+crossRefString);
				}
			} else if (isAssignment(expr)) {
				IQLAssignmentExpression assignmentExpr = getAssignmentExpr(expr);
				List<IQLExpression> parameters = new ArrayList<>();
				parameters.add(assignmentExpr.getRightOperand());
				result = methodFinder.findMethod(methods, "set"+crossRefString,parameters);
			} 
			if (result == null) {
				result = methodFinder.findMethod(methods, crossRefString);
			}
		}
		return result;

	}
	
	private boolean isAssignment(IQLJvmElementCallExpression obj) {
		IQLAssignmentExpression expr = EcoreUtil2.getContainerOfType(obj, IQLAssignmentExpression.class);
		if (expr != null) {
			return true;			
		} 
		return false;
	}
	
	private boolean isAssignment(IQLMemberSelection obj) {
		IQLAssignmentExpression expr = EcoreUtil2.getContainerOfType(obj, IQLAssignmentExpression.class);
		if (expr != null) {
			if (expr.getLeftOperand() instanceof IQLMemberSelectionExpression) {
				IQLMemberSelectionExpression memberSel = (IQLMemberSelectionExpression) expr.getLeftOperand();
				if (memberSel.getSel() == obj) {
					return true;
				}
			}			
		} 
		return false;
	}
	
	private IQLAssignmentExpression getAssignmentExpr(EObject obj) {
		IQLAssignmentExpression expr = EcoreUtil2.getContainerOfType(obj, IQLAssignmentExpression.class);
		return expr;
	}
	
	
	protected EObject getLinkedObjectIQLJvmElementCallExpression(IQLJvmElementCallExpression expr, EReference ref, INode node) throws IllegalNodeException {
		String[] splits = getCrossRefNodeAsString(node).split(IQLQualifiedNameConverter.DELIMITER);
		String crossRefString = splits[splits.length-1];
		String type = null;
		if (splits.length>1) {
			type = splits[0];
		}
		
		IScope scope = scopeProvider.getScope(expr, BasicIQLPackage.eINSTANCE.getIQLJvmElementCallExpression_Element());
		Iterable<IEObjectDescription> eObjectDescriptions = scope.getElements(QualifiedName.create(crossRefString));
				
		EObject result = null;
		if (expr.getArgs() == null) {
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
				result = methodFinder.findMethod(methods, "get"+crossRefString);
				if (result == null) {
					result = methodFinder.findMethod(methods, "is"+crossRefString);
				}
			} else if (isAssignment(expr)) {
				IQLAssignmentExpression assignmentExpr = getAssignmentExpr(expr);
				List<IQLExpression> parameters = new ArrayList<>();
				parameters.add(assignmentExpr.getRightOperand());
				result = methodFinder.findMethod(methods, "set"+crossRefString, parameters);
			}
			if (result == null) {
				result = methodFinder.findMethod(methods, crossRefString);
			}
		}
		return result;
	}
}
