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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLLookUp<T extends IIQLTypeFactory, F extends IIQLTypeExtensionsFactory, U extends IIQLTypeUtils> implements IIQLLookUp{
	
	@Inject
	protected IResourceDescriptions resources;
	
	protected T typeFactory;
	protected U typeUtils;

	protected F typeOperatorsFactory;
	
	@Inject
	protected IIQLMethodFinder methodFinder;



	public AbstractIQLLookUp(T typeFactory, F typeOperatosFactory, U typeUtils) {
		this.typeFactory = typeFactory;
		this.typeOperatorsFactory = typeOperatosFactory;
		this.typeUtils = typeUtils;
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
				if (!attributes.containsKey(attribute.getSimpleName()) && (typeUtils.isUserDefinedType(type, false) || !typeUtils.isArray(attribute.getType()))) {
					attributes.put(attribute.getSimpleName(), attribute);
				}
			}
		}
		if (extensionAttributes && typeOperatorsFactory.hasTypeExtensions(typeRef)) {
			Collection<JvmField> col = typeOperatorsFactory.getAllExtensionAttributes(typeRef, visibilities);
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
		boolean isObject = typeUtils.getLongName(type, true).equals(Object.class.getCanonicalName());
		if (!isObject && deep && !visitedTypes.contains(Object.class.getCanonicalName())) {
			findAttributes(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()),visitedTypes,attributes, deep, visibilities, onlyStatic,  extensionAttributes);
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
				if (typeUtils.isUserDefinedType(type, false) || (!arrayUsedInMethod(method) && !methods.containsKey(methodId))) {
					methods.put(methodId, method);
				}
			}
		}
		if (extensionMethods && typeOperatorsFactory.hasTypeExtensions(typeRef)) {
			Collection<JvmOperation> col = typeOperatorsFactory.getAllExtensionMethods(typeRef,visibilities);
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
		
		boolean isObject = typeUtils.getLongName(type, true).equals(Object.class.getCanonicalName());
		if (!isObject && deep && !visitedTypes.contains(Object.class.getCanonicalName())) {
			findMethods(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()), visitedTypes, methods, deep, visibilities,onlyStatic,  extensionMethods);
		}
	}
	
		
	@Override
	public Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		findConstructors(typeRef, constructors, visibilities, true);
		return constructors.values();
	}
	
	@Override
	public JvmExecutable findPublicConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		findConstructors(typeRef, constructors, visibilities, true);
		return methodFinder.findConstructor(constructors.values(), arguments);
	}
	
	@Override
	public JvmExecutable findConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE, JvmVisibility.PROTECTED_VALUE, JvmVisibility.DEFAULT_VALUE};
		findConstructors(typeRef, constructors, visibilities, true);
		return methodFinder.findConstructor(constructors.values(), arguments);
	}

	
	protected void findConstructors(JvmTypeReference typeRef, Map<String, JvmExecutable> constructors,int[] visibilities,  boolean deep) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmDeclaredType) {
			findConstructors((JvmDeclaredType)type,constructors, visibilities,deep);
		} 
	}
	
	protected void findConstructors(JvmDeclaredType type, Map<String, JvmExecutable> constructors,int[] visibilities, boolean deep) {	
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
				if (!arrayUsedInExecutable(constructor) && !constructors.containsKey(constructorId)) {
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
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {
			findConstructors(type.getExtendedClass(), constructors, visibilities, deep);
		}	
	}

	
	@Override
	public Collection<JvmType> getAllTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> types = new HashSet<>();
		Collection<IQLModel> files = getAllFiles(context);
		for (IQLModel file : files) {
			types.addAll(EcoreUtil2.getAllContentsOfType(file, JvmType.class));	
		}
		types.addAll(typeFactory.getVisibleTypes(usedNamespaces, context));
		return types;
	}
	
	@Override
	public boolean isInstantiateable(JvmDeclaredType declaredType) {
		return declaredType.isInstantiateable() || typeUtils.isUserDefinedType(declaredType, false);
	}
	
	@Override
	public Collection<JvmType> getAllInstantiateableTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> result = new HashSet<>();
		for (JvmType type : getAllTypes(usedNamespaces, context)) {
			if (type instanceof JvmDeclaredType) {
				JvmDeclaredType declaredType = (JvmDeclaredType) type;
				if (declaredType.isInstantiateable() || typeUtils.isUserDefinedType(declaredType, false)) {
					result.add(declaredType);
				}
			}
		}
		return result;
	}
	
	@Override
	public Collection<JvmType> getAllAssignableTypes(JvmTypeReference target, Collection<String> usedNamespaces,Resource context) {
		Collection<JvmType> result = new HashSet<>();
		for (JvmType type : getAllTypes(usedNamespaces, context)) {
			if (isAssignable(target, type)) {
				result.add(type);
			}
		}
		return result;
	}
	
	@Override
	public boolean isCastable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		if (!isArraySizeEqual(targetRef, typeRef)) {
			return false;
		}
		String targetName = typeUtils.getLongName(targetRef, false);
		String typeName = typeUtils.getLongName(typeRef, false);
		
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
		
		boolean isObject = typeUtils.getLongName(targetRef, true).equals(Object.class.getCanonicalName());
		if (!isObject) {
			boolean result = isCastable(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()), typeRef);
			if (result) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		if (!isArraySizeEqual(targetRef, typeRef)) {
			return false;
		}	
		String targetName = typeUtils.getLongName(targetRef, false);
		String typeName = typeUtils.getLongName(typeRef, false);
		
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
		
		boolean isObject = typeUtils.getLongName(typeRef, true).equals(Object.class.getCanonicalName());
		if (!isObject) {
			boolean result = isAssignable(targetRef, typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()));
			if (result) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Map<String, JvmTypeReference> getProperties(JvmTypeReference typeRef) {
		Map<String, JvmTypeReference> result = new HashMap<>();
		Collection<JvmField> attributes = getPublicAttributes(typeRef, false);
		for (JvmField attr : attributes) {
			result.put(attr.getSimpleName(), attr.getType());			
		}
		
		Collection<JvmOperation> setters = getPublicSetters(typeRef);
		for (JvmOperation op : setters) {
			String name = op.getSimpleName().substring(3);
			result.put(name, op.getParameters().get(0).getParameterType());
		}	
		return result;
	}
	
	protected Collection<JvmOperation> getPublicSetters(JvmTypeReference typeRef) {
		Collection<JvmOperation> result = new HashSet<>();
		for (JvmOperation op : getPublicMethods(typeRef, false)) {
			if (op.getSimpleName().startsWith("set") && op.getParameters().size() == 1 && (op.getReturnType() == null || typeUtils.isVoid(typeRef))){
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
	
	protected boolean arrayUsedInExecutable(JvmExecutable exe) {
		for (JvmFormalParameter p : exe.getParameters()) {
			if (typeUtils.isArray(p.getParameterType())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean arrayUsedInMethod(JvmOperation method) {
		for (JvmFormalParameter p : method.getParameters()) {
			if (typeUtils.isArray(p.getParameterType())) {
				return true;
			}
		}
		if (method.getReturnType() != null && typeUtils.isArray(method.getReturnType())) {
			return true;
		}
		return false;
	}
	
	protected Collection<IQLModel> getAllFiles(Resource context) {
		Collection<IQLModel> files = new HashSet<>();
		for (IResourceDescription res : resources.getAllResourceDescriptions()) {
			Resource r = EcoreUtil2.getResource(context, res.getURI().toString());
			if (r.getContents().size() > 0) {
				EObject obj = r.getContents().get(0);
				if (obj instanceof IQLModel) {
					files.add((IQLModel)obj);
				}
			}
		}
		return files;
	}	
	
	@Override
	public Collection<String> getAllNamespaces() {
		Collection<String> result = new HashSet<>();
		for (Package p : Package.getPackages()) {
			result.add(p.getName());
		}
		result.addAll(typeFactory.getJavaPackages());
		return result;
	}


}
