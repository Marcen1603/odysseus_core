package de.uniol.inf.is.odysseus.iql.odl.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.scoping.IScope;











import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;


public class ODLScopeProvider extends AbstractIQLScopeProvider<IODLTypeDictionary, IODLLookUp, IODLExpressionEvaluator, IODLTypeUtils> implements IODLScopeProvider{
	
	@Inject
	public ODLScopeProvider(IODLTypeDictionary typeDictionary, IODLLookUp lookUp,IODLExpressionEvaluator exprEvaluator, IODLTypeUtils typeUtils) {
		super(typeDictionary, lookUp, exprEvaluator, typeUtils);
	}
	
	@Override
	protected Collection<JvmField> getIQLMemberSelectionAttributes(IQLMemberSelectionExpression expr, JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
		Collection<JvmField> attributes = super.getIQLMemberSelectionAttributes(expr, typeRef, isThis, isSuper);
		ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);
		if (isThis && operator != null) {
			attributes.addAll(EcoreUtil2.getAllContentsOfType(operator, IQLAttribute.class));		
		}
		return attributes;
	}
	
	@Override
	protected Collection<JvmOperation> getIQLMemberSelectionMethods(IQLMemberSelectionExpression expr, JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
		Collection<JvmOperation> methods = super.getIQLMemberSelectionMethods(expr, typeRef, isThis, isSuper);
		ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);
		if (isThis && operator != null) {
			for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
				if (method.getSimpleName() == null) {
					continue;
				}
				if (isAOContext(expr) && isAOContext(method)) {
					methods.add(method);
				} else if (isPOContext(expr) && isPOContext(method)) {
					methods.add(method);
				} else if (!isAOContext(method) && !isPOContext(method)) {
					methods.add(method);
				}
			}				
		}
		return methods;
	}
	
	@Override
	protected Collection<JvmIdentifiableElement> getAttributesIQLJvmElementCallExpression(EObject node, JvmTypeReference thisType, Collection<JvmTypeReference> importedTypes) {
		Collection<JvmIdentifiableElement> elements = super.getAttributesIQLJvmElementCallExpression(node, thisType, importedTypes);
		ODLOperator operator = EcoreUtil2.getContainerOfType(node, ODLOperator.class);
		if (node instanceof ODLOperator || operator != null) {
			elements.addAll(EcoreUtil2.getAllContentsOfType(operator, IQLAttribute.class));		
		}		
		return elements;
	}
	
	@Override
	protected Collection<JvmIdentifiableElement> getMethodsIQLJvmElementCallExpression(EObject node, JvmTypeReference thisType, Collection<JvmTypeReference> importedTypes) {
		Collection<JvmIdentifiableElement> elements = super.getMethodsIQLJvmElementCallExpression(node, thisType, importedTypes);
		ODLOperator operator = EcoreUtil2.getContainerOfType(node, ODLOperator.class);
		if (node instanceof ODLOperator || operator != null) {
			for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
				if (method.getSimpleName() == null) {
					continue;
				}
				if (isAOContext(node) && isAOContext(method)) {
					elements.add(method);
				} else if (isPOContext(node) && isPOContext(method)) {
					elements.add(method);
				} else if (!isAOContext(method) && !isPOContext(method)) {
					elements.add(method);
				}
			}	
		}
		return elements;
	}
	
	
	private boolean isAOContext(EObject node) {
		ODLMethod method = null;
		if (node instanceof ODLMethod) {
			method = (ODLMethod) node;
		} else {
			method = EcoreUtil2.getContainerOfType(node, ODLMethod.class);
		}
		if (method != null) {
			if (method.isAo() || method.isValidate()) {
				return true;
			} else if (method.isOn() && EventMethodsFactory.getInstance().hasEventMethod(true, method.getSimpleName(), method.getParameters())) {
				return true;
			}
		}
		return false;
	}
	
	
	private boolean isPOContext(EObject node) {
		ODLMethod method = null;
		if (node instanceof ODLMethod) {
			method = (ODLMethod) node;
		} else {
			method = EcoreUtil2.getContainerOfType(node, ODLMethod.class);
		}
		if (method != null) {
			if (method.isPo()) {
				return true;
			} else if (method.isOn() && EventMethodsFactory.getInstance().hasEventMethod(false, method.getSimpleName(), method.getParameters())) {
				return true;
			}
		}
		return false;
	}
	
//	
//	@Override
//	protected Collection<JvmField> getAttributes(IQLMemberSelectionExpression expr, JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
//		JvmType innerType = typeUtils.getInnerType(typeRef, true);
//		if (innerType instanceof ODLOperator && isThis) {
//			Collection<JvmField> result = new HashSet<>();
//			result.addAll(lookUp.getPublicAttributes(typeRef, true));
//			result.addAll(lookUp.getProtectedAttributes(typeRef, true));
//			boolean isAOContext = false;
//			IQLMethod method = EcoreUtil2.getContainerOfType(expr, IQLMethod.class);
//			if (method != null) {
//				isAOContext = isAOMethod(method);				
//			}
//			if (isAOContext) {
//				result.addAll(lookUp.getPublicAttributes(typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()), true));
//				result.addAll(lookUp.getProtectedAttributes(typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()), true));
//			} else {
//				result.addAll(lookUp.getPublicAttributes(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), true));
//				result.addAll(lookUp.getProtectedAttributes(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), true));
//			}
//			return result;
//		} else {
//			return super.getAttributes(expr, typeRef, isThis, isSuper);
//		}
//	}
//	
//	@Override
//	protected Collection<JvmOperation> getMethods(IQLMemberSelectionExpression expr, JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
//		JvmType innerType = typeUtils.getInnerType(typeRef, true);
//		if (innerType instanceof ODLOperator && isThis) {
//			Collection<JvmOperation> result = new HashSet<>();
//			boolean isAOContext = false;
//			IQLMethod method = EcoreUtil2.getContainerOfType(expr, IQLMethod.class);
//			if (method != null) {
//				isAOContext = isAOMethod(method);				
//			}
//			if (isAOContext) {
//				result.addAll(lookUp.getPublicMethods(typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()), true));
//				result.addAll(lookUp.getProtectedMethods(typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()), true));
//			} else {
//				result.addAll(lookUp.getPublicMethods(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), true));
//				result.addAll(lookUp.getProtectedMethods(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), true));
//			}
//			
//			for (JvmOperation op : lookUp.getPublicMethods(typeRef,true)) {
//				if (isAOContext && isAOMethod(op)) {
//					result.add(op);
//				} else if (!isAOContext && isPOMethod(op)){
//					result.add(op);
//				}
//								
//			}
//			for (JvmOperation op : lookUp.getProtectedMethods(typeRef,true)) {
//				if (isAOContext && isAOMethod(op)) {
//					result.add(op);
//				} else if (!isAOContext && isPOMethod(op)){
//					result.add(op);
//				}
//			}
//			
//			return result;
//		} else {
//			return super.getMethods(expr, typeRef, isThis, isSuper);
//		}
//	}
//	@Override
//	protected Collection<JvmIdentifiableElement> getThisElements(EObject node, Collection<JvmTypeReference> importedTypes) {
//		JvmDeclaredType thisType = EcoreUtil2.getContainerOfType(node, JvmDeclaredType.class);
//		JvmTypeReference type = typeUtils.createTypeRef(thisType);
//		if (thisType instanceof ODLOperator) {
//			Collection<JvmIdentifiableElement> elements = new HashSet<>();
//			elements.addAll(lookUp.getPublicAttributes(type, importedTypes, true));
//			elements.addAll(lookUp.getProtectedAttributes(type, importedTypes, true));
//		
//			boolean isAOContext = false;
//			IQLMethod method = EcoreUtil2.getContainerOfType(node, IQLMethod.class);
//			if (method != null) {
//				isAOContext = isAOMethod(method);				
//			}
//			
//			for (JvmOperation op : lookUp.getPublicMethods(type, importedTypes,true)) {
//				if (isAOContext && isAOMethod(op)) {
//					elements.add(op);
//				} else if (!isAOContext && isPOMethod(op)){
//					elements.add(op);
//				}				
//			}
//			for (JvmOperation op : lookUp.getProtectedMethods(type, importedTypes,true)) {
//				if (isAOContext && isAOMethod(op)) {
//					elements.add(op);
//				} else if (!isAOContext && isPOMethod(op)){
//					elements.add(op);
//				}
//			}
//			return elements;
//		} else {
//			return super.getThisElements(node, importedTypes);
//		}
//	}
//	
//	private boolean isAOMethod(JvmOperation op) {
//		if (op instanceof ODLMethod) {
//			ODLMethod odlMethod = (ODLMethod) op;
//			if (odlMethod.isAo()) {
//				return true;
//			} else if(odlMethod.isValidate()) {
//				return true;
//			} else if (odlMethod.isOn() && EventMethodsFactory.getInstance().hasEventMethod(true, odlMethod.getSimpleName(), odlMethod.getParameters())) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	private boolean isPOMethod(JvmOperation op) {
//		if (isAOMethod(op)) {
//			return false;
//		} else {
//			if (op instanceof ODLMethod) {
//				ODLMethod odlMethod = (ODLMethod) op;
//				if (odlMethod.isPo()) {
//					return true;
//				} else if (odlMethod.isOn() && EventMethodsFactory.getInstance().hasEventMethod(false, odlMethod.getSimpleName(), odlMethod.getParameters())) {
//					return true;
//				}
//			}
//		}
//		return true;
//	}


	@Override
	protected IScope getJdtScope(ResourceSet set,IIQLJdtTypeProvider typeProvider) {
		return IScope.NULLSCOPE;
	}

}
