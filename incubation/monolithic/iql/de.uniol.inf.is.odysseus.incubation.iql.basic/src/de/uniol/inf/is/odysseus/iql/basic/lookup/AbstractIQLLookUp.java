package de.uniol.inf.is.odysseus.iql.basic.lookup;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.ClassUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

@SuppressWarnings("restriction")
public abstract class AbstractIQLLookUp<T extends IIQLTypeFactory, F extends IIQLTypeExtensionsFactory, U extends IIQLTypeUtils> implements IIQLLookUp{
	
	@Inject
	protected IResourceDescriptions resources;
	
	protected T typeFactory;
	protected U typeUtils;

	protected F typeOperatosFactory;

	
	@Inject
	protected IJvmTypeProvider.Factory typeProviderFactory;

	public AbstractIQLLookUp(T typeFactory, F typeOperatosFactory, U typeUtils) {
		this.typeFactory = typeFactory;
		this.typeOperatosFactory = typeOperatosFactory;
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
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		Map<String, JvmField> attributes = new HashMap<>();

		findAttributes(typeRef,attributes, true, visibilities, false,extensionAttributes);
		for (JvmTypeReference importedType : importedTypes) {
			findAttributes(importedType,attributes, true, visibilities, true, false);
		}
		return new HashSet<>(attributes.values());
	}
	
	@Override
	public Collection<JvmField> getProtectedAttributes(JvmTypeReference typeRef, Collection<JvmTypeReference> importedTypes, boolean extensionAttributes) {
		int[] visibilities = new int[]{JvmVisibility.PROTECTED_VALUE};
		Map<String, JvmField> attributes = new HashMap<>();

		findAttributes(typeRef, attributes, true, visibilities, false,extensionAttributes);
		for (JvmTypeReference importedType : importedTypes) {
			findAttributes(importedType,attributes, true, visibilities, true, false);
		}
		return new HashSet<>(attributes.values());
	}
	
	protected void findAttributes(JvmTypeReference typeRef,Map<String, JvmField> attributes, boolean deep, int[] visibilities,boolean onlyStatic, boolean extensionAttributes) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			findAttributes((JvmGenericType)type,typeRef,attributes, deep, visibilities, onlyStatic, extensionAttributes);
		} 
	}
	
	protected void findAttributes(JvmGenericType type,JvmTypeReference typeRef, Map<String, JvmField> attributes, boolean deep, int[] visibilities, boolean onlyStatic,  boolean extensionAttributes) {		
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
		if (extensionAttributes && typeOperatosFactory.hasTypeExtensions(typeRef)) {
			Collection<JvmField> col = typeOperatosFactory.getAllExtensionAttributes(typeRef, visibilities);
			for (JvmField attr : col) {
				if (!attributes.containsKey(attr.getSimpleName())) {
					attributes.put(attr.getSimpleName(), attr);
				}
			}			
		}
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {			
			findAttributes(type.getExtendedClass(), attributes, deep, visibilities, onlyStatic, extensionAttributes);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (interf != type) {
					findAttributes(interf, attributes, deep, visibilities,  onlyStatic,  extensionAttributes);
				}
			}	
		}
		boolean isObject = typeUtils.getLongName(type, true).equals(Object.class.getCanonicalName());
		if (!isObject && deep && type.getExtendedClass() == null && !type.getExtendedInterfaces().iterator().hasNext()) {
			findAttributes(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()),attributes, deep, visibilities, onlyStatic,  extensionAttributes);
		}
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
		Map<String, JvmOperation> methods = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		findMethods(typeRef, methods, true, visibilities, false, extensionMethods);
		for (JvmTypeReference importedType : importedTypes) {
			findMethods(importedType, methods, true, visibilities, true, false);
		}
		return new HashSet<>(methods.values());
	}
	
	@Override
	public Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef,Collection<JvmTypeReference> importedTypes,boolean extensionMethods) {
		Map<String, JvmOperation> methods = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PROTECTED_VALUE};
		findMethods(typeRef, methods, true, visibilities, false, extensionMethods);
		for (JvmTypeReference importedType : importedTypes) {
			findMethods(importedType, methods, true, visibilities, true, false);
		}
		return new HashSet<>(methods.values());
	}
	
	
	protected void findMethods(JvmTypeReference typeRef, Map<String, JvmOperation> methods, boolean deep, int[] visibilities, boolean onlyStatic,  boolean extensionMethods) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			findMethods((JvmGenericType)type,typeRef,methods, deep, visibilities,onlyStatic, extensionMethods);
		} 
	}	
	
	protected void findMethods(JvmGenericType type,JvmTypeReference typeRef, Map<String, JvmOperation> methods, boolean deep, int[] visibilities, boolean onlyStatic,  boolean extensionMethods) {	
		for (JvmOperation method : type.getDeclaredOperations()) {
			boolean success = false;
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
				String methodId = createExecutableID(method);
				if (typeUtils.isUserDefinedType(type, false) || (!arrayUsedInMethod(method) && !methods.containsKey(methodId))) {
					methods.put(methodId, method);
				}
			}
		}
		if (extensionMethods && typeOperatosFactory.hasTypeExtensions(typeRef)) {
			Collection<JvmOperation> col = typeOperatosFactory.getAllExtensionMethods(typeRef,visibilities);
			for (JvmOperation op : col) {
				String methodId = createExecutableID(op);
				if (!methods.containsKey(methodId)) {
					methods.put(methodId, op);
				}
			}
		
		}
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {
			findMethods(type.getExtendedClass(), methods, deep, visibilities, onlyStatic, extensionMethods);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (interf != type) {
					findMethods(interf, methods, deep, visibilities, onlyStatic, extensionMethods);
				}
			}
		}
		
		boolean isObject = typeUtils.getLongName(type, true).equals(Object.class.getCanonicalName());
		if (!isObject && deep && type.getExtendedClass() == null && !type.getExtendedInterfaces().iterator().hasNext()) {
			findMethods(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()), methods, deep, visibilities,onlyStatic,  extensionMethods);
		}
	}
	
		
	@Override
	public Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		findConstructors(typeRef, constructors, true);
		return constructors.values();
	}
	
	@Override
	public Collection<JvmExecutable> getDeclaredConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		findConstructors(typeRef, constructors, false);
		return constructors.values();
	}
	
	protected void findConstructors(JvmTypeReference typeRef, Map<String, JvmExecutable> constructors, boolean deep) {
		JvmType type = typeUtils.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			findConstructors((JvmGenericType)type,constructors, deep);
		} 
	}
	
	protected void findConstructors(JvmGenericType type, Map<String, JvmExecutable> constructors, boolean deep) {	
		for (JvmConstructor constructor : type.getDeclaredConstructors()) {
			int visibility = constructor.getVisibility().getValue();
			if (visibility!=JvmVisibility.PRIVATE_VALUE && visibility!=JvmVisibility.PROTECTED_VALUE&& (typeUtils.isUserDefinedType(type, false) || visibility!=JvmVisibility.DEFAULT_VALUE)) {
				String constructorId = createExecutableID(constructor);
				if (!arrayUsedInExecutable(constructor) && !constructors.containsKey(constructorId)) {
					constructors.put(constructorId, constructor);
				}
			}
		}
		if (typeUtils.isUserDefinedType(type, false)) {
			for (JvmOperation constructor : type.getDeclaredOperations()) {
				if (constructor.getSimpleName() != null && constructor.getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
					int visibility = constructor.getVisibility().getValue();
					if (visibility!=JvmVisibility.PRIVATE_VALUE && visibility!=JvmVisibility.PROTECTED_VALUE && (typeUtils.isUserDefinedType(type, false) || visibility!=JvmVisibility.DEFAULT_VALUE)) {
						String constructorId = createExecutableID(constructor);
						if (!constructors.containsKey(constructorId)) {
							constructors.put(constructorId, constructor);
						}
					}
				}
			}
		}
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {
			findConstructors(type.getExtendedClass(), constructors, deep);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (interf != type) {
					findConstructors(interf, constructors, deep);
				}
			}
		}		
	}
	
	@Override
	public JvmExecutable findConstructor(JvmTypeReference typeRef, int arguments) {		
		for (JvmExecutable exe : getPublicConstructors(typeRef)) {
			if (exe.getParameters().size() == arguments){
				return exe;
			}
		}		
		return null;
	}
	
	
	@Override
	public Collection<JvmType> getAllTypes(Resource context) {
		Collection<JvmType> types = new HashSet<>();
		Collection<IQLFile> files = getAllFiles(context);
		for (IQLFile file : files) {
			types.addAll(EcoreUtil2.getAllContentsOfType(file, JvmType.class));	
		}
		types.addAll(typeFactory.getVisibleTypes(context));
		return types;
	}
	
	@Override
	public Collection<JvmType> getAllInstantiateableTypes(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		for (JvmType type : getAllTypes(context)) {
			if (type instanceof JvmGenericType) {
				JvmGenericType genericType = (JvmGenericType) type;
				if (genericType.isInstantiateable() || typeUtils.isUserDefinedType(genericType, false)) {
					result.add(genericType);
				}
			}
		}
		return result;
	}
	
	@Override
	public Collection<JvmType> getAllAssignableTypes(JvmTypeReference target, Resource context) {
		Collection<JvmType> result = new HashSet<>();
		for (JvmType type : getAllTypes(context)) {
			if (isAssignable(target, type)) {
				result.add(type);
			}
		}
		return result;
	}
	
	@Override
	public boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		if (!isArraySizeEqual(targetRef, typeRef)) {
			return false;
		}	
		String targetName = typeUtils.getLongName(targetRef, false, false);
		String typeName = typeUtils.getLongName(typeRef, false, false);
		
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
		if (innerType instanceof JvmGenericType) {
			JvmGenericType genericType = (JvmGenericType) innerType;
			if (genericType.getExtendedClass() != null) {
				boolean result = isAssignable(targetRef, genericType.getExtendedClass());
				if (result) {
					return true;
				}
			}
			for (JvmTypeReference interf : genericType.getExtendedInterfaces()) {
				boolean result = isAssignable(targetRef, interf);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Map<String, JvmTypeReference> getProperties(JvmTypeReference typeRef) {
		Map<String, JvmTypeReference> result = new HashMap<>();
		Collection<JvmField> attributes = getPublicAttributes(typeRef, new HashSet<JvmTypeReference>(),false);
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
		for (JvmOperation op : getPublicMethods(typeRef,new HashSet<JvmTypeReference>(), false)) {
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
	
	protected String createExecutableID(JvmExecutable exe){
		return exe.getSimpleName()+"_"+exe.getParameters().size();
	}	
	
	protected Collection<IQLFile> getAllFiles(Resource context) {
		Collection<IQLFile> files = new HashSet<>();
		for (IResourceDescription res : resources.getAllResourceDescriptions()) {
			Resource r = EcoreUtil2.getResource(context, res.getURI().toString());
			if (r.getContents().size() > 0) {
				files.add((IQLFile) r.getContents().get(0));
			}
		}
		return files;
	}
	

	


}
