package de.uniol.inf.is.odysseus.iql.basic.lookup;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.ClassUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmArrayType;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLLookUp<T extends IIQLTypeDictionary, F extends IIQLTypeExtensionsDictionary, U extends IIQLTypeUtils> implements IIQLLookUp{
		
	protected T typeDictionary;
	protected U typeUtils;

	protected F typeExtensionsDictionary;
	
	@Inject
	protected IQLQualifiedNameConverter converter;

	
	@Inject
	protected IIQLMethodFinder methodFinder;



	public AbstractIQLLookUp(T typeDictionary, F typeExtensionsDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeExtensionsDictionary = typeExtensionsDictionary;
		this.typeUtils = typeUtils;
	}
	
	@Override
	public JvmTypeReference getThisType(EObject node) {
		JvmDeclaredType c = EcoreUtil2.getContainerOfType(node, JvmDeclaredType.class);
		return typeUtils.createTypeRef(c);
	}

	@Override
	public JvmTypeReference getSuperType(EObject node) {
		JvmDeclaredType c = EcoreUtil2.getContainerOfType(node, JvmDeclaredType.class);
		if (c.getExtendedClass() != null) {
			return c.getExtendedClass();
		} else {
			return typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet());
		}		
	}
	
	@Override
	public Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef, boolean extensionAttributes) {
		return getPublicAttributes(typeRef,new HashSet<JvmTypeReference>(),extensionAttributes);
	}
	
	@Override
	public Collection<JvmField> getProtectedAttributes(JvmTypeReference typeRef, boolean extensionAttributes) {
		return getProtectedAttributes(typeRef,new HashSet<JvmTypeReference>(), extensionAttributes);
	}
	
	@Override
	public Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef, Collection<JvmTypeReference> importedTypes,boolean extensionAttributes) {
		Set<JvmField> result = new HashSet<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		
		Map<String, JvmField> attributes = new HashMap<>();
		findAttributes(typeRef,new HashSet<String>(),attributes, true, visibilities, false,extensionAttributes);
		result.addAll(attributes.values());
		
		for (JvmTypeReference importedType : importedTypes) {
			Map<String, JvmField> attributesMap = new HashMap<>();
			findAttributes(importedType,new HashSet<String>(), attributesMap, true, visibilities, true, false);
			result.addAll(attributesMap.values());
		}
		return result;
	}
	
	@Override
	public Collection<JvmField> getProtectedAttributes(JvmTypeReference typeRef, Collection<JvmTypeReference> importedTypes, boolean extensionAttributes) {
		Set<JvmField> result = new HashSet<>();
		int[] visibilities = new int[]{JvmVisibility.PROTECTED_VALUE};
		
		Map<String, JvmField> attributes = new HashMap<>();
		findAttributes(typeRef, new HashSet<String>(), attributes, true, visibilities, false,extensionAttributes);
		result.addAll(attributes.values());
		
		for (JvmTypeReference importedType : importedTypes) {
			Map<String, JvmField> attributesMap = new HashMap<>();
			findAttributes(importedType,new HashSet<String>(), attributesMap, true, visibilities, true, false);
			result.addAll(attributesMap.values());
		}
		return result;
	}
	
	protected void findAttributes(JvmTypeReference typeRef, Set<String> visitedTypes,Map<String, JvmField> attributes, boolean deep, int[] visibilities,boolean onlyStatic, boolean extensionAttributes) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmDeclaredType) {
			findAttributes((JvmDeclaredType)type,typeRef,visitedTypes, attributes, deep, visibilities, onlyStatic, extensionAttributes);
		} 
	}
	
	protected void findAttributes(JvmDeclaredType type,JvmTypeReference typeRef, Set<String> visitedTypes, Map<String, JvmField> attributes, boolean deep, int[] visibilities, boolean onlyStatic,  boolean extensionAttributes) {		
		visitedTypes.add(typeUtils.getLongName(type, false));
		for (JvmField attribute : type.getDeclaredFields()) {
			boolean success = false;
			if (typeUtils.isUserDefinedType(type, false)) {
				success = true;
			}			
			int visibility = attribute.getVisibility().getValue();
			for (int v : visibilities) {
				if (visibility == v) {
					success = true;
					break;
				}
			}
			if (onlyStatic && !attribute.isStatic()) {
				success = false;
			}
			if (success) {
				if (!attributes.containsKey(attribute.getSimpleName())) {
					attributes.put(attribute.getSimpleName(), attribute);
				}
			}
		}
		if (extensionAttributes && typeExtensionsDictionary.hasTypeExtensions(typeRef)) {
			Collection<JvmField> col = typeExtensionsDictionary.getAllExtensionAttributes(typeRef, visibilities);
			for (JvmField attr : col) {
				if (!attributes.containsKey(attr.getSimpleName())) {
					attributes.put(attr.getSimpleName(), attr);
				}
			}			
		}
		if (deep && type.getExtendedClass()!= null && !visitedTypes.contains(typeUtils.getLongName(type.getExtendedClass(), false))) {			
			findAttributes(type.getExtendedClass(), visitedTypes, attributes, deep, visibilities, onlyStatic, extensionAttributes);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (!visitedTypes.contains(typeUtils.getLongName(interf, false))) {
					findAttributes(interf, visitedTypes, attributes, deep, visibilities,  onlyStatic,  extensionAttributes);
				}
			}	
		}
		boolean isObject = converter.toJavaString(typeUtils.getLongName(type, true)).equals(Object.class.getCanonicalName());
		if (!isObject && deep && !visitedTypes.contains(converter.toIQLString(Object.class.getCanonicalName()))) {
			findAttributes(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()),visitedTypes,attributes, deep, visibilities, onlyStatic,  extensionAttributes);
		}
	}
	
	@Override
	public Collection<JvmOperation> getDeclaredPublicMethods(JvmTypeReference typeRef, boolean onlyStatic) {
		Map<String, JvmOperation> methods = new HashMap<>();
		Set<String> visitedTypes = new HashSet<>();
		
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		findMethods(typeRef,visitedTypes, methods, false, visibilities, onlyStatic, false);
		return new HashSet<>(methods.values());
	}
	
	@Override
	public Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef, boolean extensionMethods) {
		return getPublicMethods(typeRef, new HashSet<JvmTypeReference>(),  extensionMethods);
	}

	
	@Override
	public Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef, boolean extensionMethods) {
		return getProtectedMethods(typeRef,new HashSet<JvmTypeReference>(), extensionMethods);
	}
	
	@Override
	public Collection<JvmOperation> getMethodsToOverride(JvmGenericType type, EObject context) {
		Map<String, JvmOperation> methods = new HashMap<>();
		Set<String> visitedTypes = new HashSet<>();
		
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE, JvmVisibility.PROTECTED_VALUE, JvmVisibility.DEFAULT_VALUE};
		if (type.getExtendedClass() != null) {
			findMethods(type.getExtendedClass(),visitedTypes, methods, true, visibilities, false, false);
		} else {
			findMethods(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()),visitedTypes, methods, true, visibilities, false, false);
		}
		for (JvmTypeReference interf : type.getExtendedInterfaces()) {
			findMethods(interf,visitedTypes, methods, true, visibilities, false, false);
		}
		return new HashSet<>(methods.values());
	}
	
	
	@Override
	public Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef,Collection<JvmTypeReference> importedTypes,boolean extensionMethods) {
		Set<JvmOperation> result = new HashSet<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};

		Map<String, JvmOperation> methods = new HashMap<>();
		findMethods(typeRef, new HashSet<String>(), methods, true, visibilities, false, extensionMethods);
		result.addAll(methods.values());
		
		for (JvmTypeReference importedType : importedTypes) {
			Map<String, JvmOperation> methodsMap = new HashMap<>();
			findMethods(importedType, new HashSet<String>(), methodsMap, true, visibilities, true, false);
			result.addAll(methodsMap.values());
		}
		return result;
	}
	
	@Override
	public Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef, Collection<JvmTypeReference> importedTypes,boolean extensionMethods) {
		Set<JvmOperation> result = new HashSet<>();
		int[] visibilities = new int[]{JvmVisibility.PROTECTED_VALUE};

		Map<String, JvmOperation> methods = new HashMap<>();
		findMethods(typeRef, new HashSet<String>(),  methods, true, visibilities, false, extensionMethods);
		result.addAll(methods.values());

		for (JvmTypeReference importedType : importedTypes) {
			Map<String, JvmOperation> methodsMap = new HashMap<>();
			findMethods(importedType, new HashSet<String>(), methodsMap, true, visibilities, true, false);
			result.addAll(methodsMap.values());
		}
		return result;
	}
	
	
	protected void findMethods(JvmTypeReference typeRef,Set<String> visitedTypes, Map<String, JvmOperation> methods, boolean deep, int[] visibilities, boolean onlyStatic,  boolean extensionMethods) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmDeclaredType) {
			findMethods((JvmDeclaredType)type,typeRef,visitedTypes, methods, deep, visibilities,onlyStatic, extensionMethods);
		} 
	}	
	
	protected void findMethods(JvmDeclaredType type,JvmTypeReference typeRef, Set<String> visitedTypes, Map<String, JvmOperation> methods, boolean deep, int[] visibilities, boolean onlyStatic,  boolean extensionMethods) {
		visitedTypes.add(typeUtils.getLongName(type, false));
		for (JvmOperation method : type.getDeclaredOperations()) {
			boolean success = false;
			if (method.getSimpleName() == null) {
				continue;
			}
			if (typeUtils.isUserDefinedType(type, false)) {
				success = true;
			}
			int visibility = method.getVisibility().getValue();
			for (int v : visibilities) {
				if (visibility == v) {
					success = true;
					break;
				}
			}
			if (onlyStatic && !method.isStatic()) {
				success = false;
			}
			if (success) {
				String methodId = methodFinder.createExecutableID(method);
				if (!methods.containsKey(methodId)) {
					methods.put(methodId, method);
				}
			}
		}
		if (extensionMethods && typeExtensionsDictionary.hasTypeExtensions(typeRef)) {
			Collection<JvmOperation> col = typeExtensionsDictionary.getAllExtensionMethods(typeRef,visibilities);
			for (JvmOperation op : col) {
				String methodId = methodFinder.createExecutableID(op);
				if (!methods.containsKey(methodId)) {
					methods.put(methodId, op);
				}
			}
		
		}
		if (deep && type.getExtendedClass()!= null && !visitedTypes.contains(typeUtils.getLongName(type.getExtendedClass(), false))) {
			findMethods(type.getExtendedClass(), visitedTypes, methods, deep, visibilities, onlyStatic, extensionMethods);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (!visitedTypes.contains(typeUtils.getLongName(interf, false))) {
					findMethods(interf, visitedTypes, methods, deep, visibilities, onlyStatic, extensionMethods);
				}
			}
		}
		
		boolean isObject = converter.toJavaString(typeUtils.getLongName(type, true)).equals(Object.class.getCanonicalName());
		if (!isObject && deep && !visitedTypes.contains(converter.toIQLString(Object.class.getCanonicalName()))) {
			findMethods(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()), visitedTypes, methods, deep, visibilities,onlyStatic,  extensionMethods);
		}
	}
	
		
	@Override
	public Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		findConstructors(typeRef, constructors, visibilities);
		return constructors.values();
	}
	
	@Override
	public Collection<JvmExecutable> getDeclaredConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE, JvmVisibility.DEFAULT_VALUE, JvmVisibility.PROTECTED_VALUE, JvmVisibility.PRIVATE_VALUE};
		findConstructors(typeRef, constructors, visibilities);
		return constructors.values();
	}
	
	@Override
	public Collection<JvmExecutable> getSuperConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE, JvmVisibility.PROTECTED_VALUE};
		
		JvmType innerType = typeUtils.getInnerType(typeRef, true);
		if (innerType instanceof JvmGenericType) {
			findConstructors(((JvmGenericType) innerType).getExtendedClass(), constructors, visibilities);
		}
		return constructors.values();
	}
	
	@Override
	public JvmExecutable findPublicConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments) {
		Collection<JvmExecutable> constructors = getPublicConstructors(typeRef);
		return methodFinder.findConstructor(constructors, arguments);
	}
	
	@Override
	public JvmExecutable findDeclaredConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments) {
		Collection<JvmExecutable> constructors = getDeclaredConstructors(typeRef);
		return methodFinder.findConstructor(constructors, arguments);
	}
	
	@Override
	public JvmExecutable findSuperConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments) {
		Collection<JvmExecutable> constructors = getSuperConstructors(typeRef);
		return methodFinder.findConstructor(constructors, arguments);
	}

	
	protected void findConstructors(JvmTypeReference typeRef, Map<String, JvmExecutable> constructors,int[] visibilities) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmDeclaredType) {
			findConstructors((JvmDeclaredType)type,constructors, visibilities);
		} 
	}
	
	protected void findConstructors(JvmDeclaredType type, Map<String, JvmExecutable> constructors,int[] visibilities) {	
		for (JvmConstructor constructor : type.getDeclaredConstructors()) {
			boolean success = false;
			int visibility = constructor.getVisibility().getValue();
			for (int v : visibilities) {
				if (visibility == v) {
					success = true;
					break;
				}
			}			
			if (success) {
				String constructorId = methodFinder.createExecutableID(constructor);
				if (!constructors.containsKey(constructorId)) {
					constructors.put(constructorId, constructor);
				}
			}
		}
		if (typeUtils.isUserDefinedType(type, false)) {
			for (JvmOperation constructor : type.getDeclaredOperations()) {
				if (constructor.getSimpleName() != null && constructor.getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
					String constructorId = methodFinder.createExecutableID(constructor);
					if (!constructors.containsKey(constructorId)) {
						constructors.put(constructorId, constructor);
					}
				}
			}
		}
	}

	
	@Override
	public boolean isInstantiateable(JvmDeclaredType declaredType) {
		if (declaredType instanceof IQLClass) {
			return true;
		} else {
			return declaredType.isInstantiateable();
		}
	}
	
//	@Override
//	public Collection<JvmType> getAllInstantiateableTypes(Collection<String> usedNamespaces, Resource context) {
//		Collection<JvmType> result = new HashSet<>();
//		for (JvmType type : getAllTypes(usedNamespaces, context)) {
//			if (type instanceof JvmDeclaredType) {
//				JvmDeclaredType declaredType = (JvmDeclaredType) type;
//				if (declaredType.isInstantiateable() || typeUtils.isUserDefinedType(declaredType, false)) {
//					result.add(declaredType);
//				}
//			}
//		}
//		return result;
//	}
//	
//	@Override
//	public Collection<JvmType> getAllAssignableTypes(JvmTypeReference target, Collection<String> usedNamespaces,Resource context) {
//		Collection<JvmType> result = new HashSet<>();
//		for (JvmType type : getAllTypes(usedNamespaces, context)) {
//			if (isAssignable(target, type)) {
//				result.add(type);
//			}
//		}
//		return result;
//	}
	
	@Override
	public boolean isCastable(JvmTypeReference targetRef, JvmTypeReference typeRef) {	
		if (typeUtils.isArray(targetRef) && typeUtils.isArray(typeRef) && !isArraySizeEqual(targetRef, typeRef)) {
			return false;
		}  else if(typeUtils.isArray(targetRef) && isCollection(typeRef)) {
			return true;
		} else if (typeUtils.isArray(targetRef) || typeUtils.isArray(typeRef)) {
			return false;
		}
	
		String targetName = converter.toJavaString(typeUtils.getLongName(targetRef, false));
		String typeName = converter.toJavaString(typeUtils.getLongName(typeRef, false));
		
		if (targetName.equalsIgnoreCase(typeName)) {
			return true;
		}
		
		if (isPrimitive(targetName) && isPrimitive(typeName)) {
			return ClassUtils.isAssignable(getPrimitive(targetName), getPrimitive(typeName), true);
		} else if (isPrimitive(targetName) && isWrapper(typeName)) {
			return ClassUtils.isAssignable(getPrimitive(targetName), getWrapper(typeName), true);
		} else if (isWrapper(targetName) && isPrimitive(typeName)) {
			return ClassUtils.isAssignable(getPrimitive(targetName), getWrapper(typeName), true);
		}
		
		JvmType innerType = typeUtils.getInnerType(targetRef, false);
		if (innerType instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) innerType;
			if (declaredType.getExtendedClass() != null && declaredType != declaredType.getExtendedClass()) {
				boolean result = isCastable(declaredType.getExtendedClass(), typeRef);
				if (result) {
					return true;
				}
			}
			for (JvmTypeReference interf : declaredType.getExtendedInterfaces()) {
				boolean result = isCastable(interf, typeRef);
				if (result) {
					return true;
				}
			}
		}
		
		boolean isObject = converter.toJavaString(typeUtils.getLongName(targetRef, true)).equals(Object.class.getCanonicalName());
		if (!isObject) {
			boolean result = isCastable(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()), typeRef);
			if (result) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		if (typeUtils.isArray(targetRef) && typeUtils.isArray(typeRef) && !isArraySizeEqual(targetRef, typeRef)) {
			return false;
		} else if(typeUtils.isArray(targetRef) && isList(typeRef)) {
			return true;
		} else if(typeUtils.isArray(typeRef) && isCollection(targetRef)) {
			return true;
		} else if (typeUtils.isArray(targetRef) || typeUtils.isArray(typeRef)) {
			return false;
		}
		
		String targetName = converter.toJavaString(typeUtils.getLongName(targetRef, false));
		String typeName = converter.toJavaString(typeUtils.getLongName(typeRef, false));
		
		if (targetName.equalsIgnoreCase(typeName)) {
			return true;
		}
		if (isPrimitive(targetName) && isPrimitive(typeName)) {
			return ClassUtils.isAssignable(getPrimitive(typeName), getPrimitive(targetName), true);
		} else if (isPrimitive(targetName) && isWrapper(typeName)) {
			return ClassUtils.isAssignable(getWrapper(typeName), getPrimitive(targetName), true);
		} else if (isWrapper(targetName) && isPrimitive(typeName)) {
			return ClassUtils.isAssignable(getPrimitive(typeName), getWrapper(targetName), true);
		} 
		
		JvmType innerType = typeUtils.getInnerType(typeRef, false);
		if (innerType instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) innerType;
			if (declaredType.getExtendedClass() != null && declaredType != declaredType.getExtendedClass()) {
				boolean result = isAssignable(targetRef, declaredType.getExtendedClass());
				if (result) {
					return true;
				}
			}
			for (JvmTypeReference interf : declaredType.getExtendedInterfaces()) {
				boolean result = isAssignable(targetRef, interf);
				if (result) {
					return true;
				}
			}
		}
		
		boolean isObject = converter.toJavaString(typeUtils.getLongName(typeRef, true)).equals(Object.class.getCanonicalName());
		if (!isObject) {
			boolean result = isAssignable(targetRef, typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()));
			if (result) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Collection<JvmOperation> getPublicSetters(JvmTypeReference typeRef) {
		Collection<JvmOperation> result = new HashSet<>();
		for (JvmOperation op : getPublicMethods(typeRef, false)) {
			if (op.getSimpleName().startsWith("set") && op.getParameters().size() == 1 && (op.getReturnType() == null || typeUtils.isVoid(op.getReturnType()))){
				result.add(op);
			}
		}
		return result;
	}
	
	protected boolean isAssignable(JvmTypeReference targetRef, JvmType type) {
		return isAssignable(targetRef, typeUtils.createTypeRef(type));
	}
	

	
	
	protected boolean isWrapper(String name) {
		return getWrapper(name)!= null;
	}
	
	protected boolean isPrimitive(String name) {
		return getPrimitive(name) != null;
	}
	
	protected Class<?> getPrimitive(String name) {
		switch (name) {
		case "byte":
			return byte.class;
		case "short":
			return short.class;
		case "int":
			return int.class;
		case "long":
			return long.class;
		case "float":
			return float.class;
		case "double":
			return double.class;
		case "char":
			return char.class;
		case "boolean":
			return boolean.class;
		default:
			return null;
		}
	}
	
	protected Class<?> getWrapper(String name) {
		if(name.equals(Byte.class.getCanonicalName())) {
			return Byte.class;
		} else if(name.equals(Short.class.getCanonicalName())) {
			return Short.class;
		} else if(name.equals(Integer.class.getCanonicalName())) {
			return Integer.class;
		} else if(name.equals(Long.class.getCanonicalName())) {
			return Long.class;
		} else if(name.equals(Float.class.getCanonicalName())) {
			return Float.class;
		} else if(name.equals(Double.class.getCanonicalName())) {
			return Double.class;
		} else if(name.equals(Character.class.getCanonicalName())) {
			return Character.class;
		} else if(name.equals(Boolean.class.getCanonicalName())) {
			return Boolean.class;
		} else {
			return null;
		}

	}
		
	protected boolean isArraySizeEqual(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		return isArraySizeEqual(typeUtils.getInnerType(targetRef, true), typeUtils.getInnerType(typeRef, true));
	}
	
	protected boolean isArraySizeEqual(JvmType target, JvmType type) {
		return typeUtils.getArraySize(target) == typeUtils.getArraySize(type);
	}	
	
	
	@Override
	public Collection<String> getAllNamespaces() {
		Collection<String> result = new HashSet<>();
		for (Package p : Package.getPackages()) {
			result.add(p.getName());
		}
		result.addAll(typeDictionary.getJavaPackages());
		return result;
	}
	
	@Override
	public boolean isMap(JvmTypeReference typeRef) {
		return isAssignable(typeUtils.createTypeRef(Map.class, typeDictionary.getSystemResourceSet()), typeRef);
	}
		
	@Override
	public boolean isList(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else if (typeUtils.getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else {
			return isAssignable(typeUtils.createTypeRef(List.class, typeDictionary.getSystemResourceSet()), typeRef);
		}
	}
	
	protected boolean isCollection(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else if (typeUtils.getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else {
			return isAssignable(typeUtils.createTypeRef(Collection.class, typeDictionary.getSystemResourceSet()), typeRef);
		}
	}



}
