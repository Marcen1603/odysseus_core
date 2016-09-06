package de.uniol.inf.is.odysseus.iql.odl.lookup;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;



















import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLLookUp extends AbstractIQLLookUp<IODLTypeDictionary, IODLTypeExtensionsDictionary, IODLTypeUtils> implements IODLLookUp{

	@Inject
	public ODLLookUp(IODLTypeDictionary typeDictionary, IODLTypeExtensionsDictionary typeExtensionsDictionary,IODLTypeUtils typeUtils) {
		super(typeDictionary, typeExtensionsDictionary, typeUtils);
	}
	
	@Override
	public Collection<JvmOperation> getMethodsToOverride(JvmGenericType type, EObject context) {
		if (type instanceof ODLOperator) {
			JvmTypeReference typeRef = null;

			ODLMethod method = EcoreUtil2.getContainerOfType(context, ODLMethod.class);
			if (method == null && context instanceof ODLMethod) {
				method = (ODLMethod) context;
			} 
			if (method != null) {
				if (method.isAo()) {
					typeRef = typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet());
				} else if (method.isPo()) {
					typeRef = typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet());
				} else {
					typeRef = typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet());
				}
			} else {
				typeRef = typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet());
			}
			
			Map<String, JvmOperation> methods = new HashMap<>();
			Set<String> visitedTypes = new HashSet<>();
			
			int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE, JvmVisibility.PROTECTED_VALUE, JvmVisibility.DEFAULT_VALUE};
			
			findMethods(typeRef,visitedTypes, methods, true, visibilities, false, false);
			return new HashSet<>(methods.values());
		} else {
			return super.getMethodsToOverride(type, context);
		}
	}
	
//	
//	@Override
//	public boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
//		if (typeUtils.getInnerType(targetRef, true) instanceof ODLOperator) {
//			boolean result =  super.isAssignable(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), typeRef);
//			if (!result) {
//				result =  super.isAssignable(typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()), typeRef);
//			}
//			return result;
//		} else if (typeUtils.getInnerType(typeRef, true) instanceof ODLOperator) {
//			boolean result = super.isAssignable(targetRef, typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()));
//			if (!result) {
//				result = super.isAssignable(targetRef, typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()));
//			}
//			return result;
//		} else {
//			return super.isAssignable(targetRef, typeRef);
//		}
//	}
//	
//	@Override
//	public boolean isCastable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
//		if (typeUtils.getInnerType(targetRef, true) instanceof ODLOperator) {
//			boolean result =  super.isCastable(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), typeRef);
//			if (!result) {
//				result =  super.isCastable(typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()), typeRef);
//			}
//			return result;
//		} else if (typeUtils.getInnerType(typeRef, true) instanceof ODLOperator) {
//			boolean result = super.isCastable(targetRef, typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()));
//			if (!result) {
//				result = super.isCastable(targetRef, typeUtils.createTypeRef(AbstractLogicalOperator.class, typeDictionary.getSystemResourceSet()));
//			}
//			return result;
//		} else {
//			return super.isCastable(targetRef, typeRef);
//		}
//	}
	
	@Override
	public JvmTypeReference getThisType(EObject node) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(node, ODLOperator.class);
		if (node instanceof ODLOperator || operator != null) {
			return getOperatorType(node);
		} else {
			return super.getThisType(node);
		}
	}

	
	@Override
	public JvmTypeReference getSuperType(EObject node) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(node, ODLOperator.class);
		if (node instanceof ODLOperator || operator != null) {
			return getOperatorType(node);
		} else {
			return super.getSuperType(node);
		}
	}
	
	protected JvmTypeReference getOperatorType(EObject node) {
		ODLMethod method = null;
		if (node instanceof ODLMethod) {
			method = (ODLMethod) node;
		} else {
			method = EcoreUtil2.getContainerOfType(node, ODLMethod.class);
		}		
		Class<?> c = Object.class;
		if (method != null) {
			if (method.isAo() || method.isValidate()) {
				c = AbstractLogicalOperator.class;
			} else if (method.isPo()) {
				c = AbstractPipe.class;
			} else if (method.isOn() && EventMethodsFactory.getInstance().hasEventMethod(true, method.getSimpleName(), method.getParameters())) {
				c = AbstractLogicalOperator.class;
			} else if (method.isOn() && EventMethodsFactory.getInstance().hasEventMethod(false, method.getSimpleName(), method.getParameters())) {
				c = AbstractPipe.class;
			}		
		}
		return typeUtils.createTypeRef(c, typeDictionary.getSystemResourceSet());
	}

	@Override
	public Collection<String> getOperatorMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : LogicalOperator.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		result.add(IODLTypeDictionary.OPERATOR_OUTPUT_MODE);
		result.add(IODLTypeDictionary.OPERATOR_PERSISTENT);
		return result;
	}
	
	@Override
	public Collection<String> getOperatorMetadataValues(String key) {
		Collection<String> result = new HashSet<>();
		if (key.equalsIgnoreCase(IODLTypeDictionary.OPERATOR_OUTPUT_MODE)) {
			for (OutputMode mode : getOutputModeValues()) {
				result.add("\""+mode.toString()+"\"");
			}
		} else if (key.equalsIgnoreCase(IODLTypeDictionary.OPERATOR_PERSISTENT)) {
			result.add("true");
			result.add("false");
		}
		return result;
	}
	

	@Override
	public Collection<String> getParameterMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : Parameter.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		return result;
	}
	
	@Override
	public Collection<String> getParameterMetadataValues(String key) {
		Collection<String> result = new HashSet<>();		
		return result;
	}

	@Override
	public OutputMode[] getOutputModeValues() {
		return OutputMode.values();
	}
	
	@Override
	public boolean isClonable(JvmTypeReference typeRef) {
		return methodFinder.findMethod(getPublicMethods(typeRef, false), "clone", 0, true) != null;
	}
	
}
