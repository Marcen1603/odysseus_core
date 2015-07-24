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

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;

@SuppressWarnings("restriction")
public abstract class AbstractIQLLookUp<T extends IIQLTypeFactory> implements IIQLLookUp{
	
	@Inject
	protected IResourceDescriptions resources;
	
	protected T typeFactory;
	
	@Inject
	protected IJvmTypeProvider.Factory typeProviderFactory;

	public AbstractIQLLookUp(T typeFactory) {
		this.typeFactory = typeFactory;
	}

	@Override
	public JvmOperation findMethod(JvmTypeReference typeRef, String methodName, IQLArgumentsList arguments) {		
		for (JvmOperation op : getPublicMethods(typeRef)) {
			if (op.getSimpleName().equalsIgnoreCase(methodName) && op.getParameters().size() == arguments.getElements().size()){
				return op;
			}
		}		
		return null;
	}
	
	@Override
	public JvmExecutable findConstructor(JvmTypeReference typeRef, IQLArgumentsList arguments) {		
		for (JvmExecutable exe : getPublicConstructors(typeRef)) {
			if (exe.getParameters().size() == arguments.getElements().size()){
				return exe;
			}
		}		
		return null;
	}
	
	public boolean isAssignable(JvmTypeReference targetRef, JvmType type) {
		return isAssignable(targetRef, typeFactory.getTypeRef(type));
	}
	
	@Override
	public boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		if (!isArraySizeEqual(targetRef, typeRef)) {
			return false;
		}	
		String targetName = typeFactory.getLongName(targetRef, false, false);
		String typeName = typeFactory.getLongName(typeRef, false, false);
		
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
		
		JvmType innerType = typeFactory.getInnerType(typeRef, false);
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
		
	public boolean isArraySizeEqual(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		return isArraySizeEqual(typeFactory.getInnerType(targetRef, true), typeFactory.getInnerType(typeRef, true));
	}
	
	public boolean isArraySizeEqual(JvmType target, JvmType type) {
		return typeFactory.getArraySize(target) == typeFactory.getArraySize(type);
	}
	
		
	@Override
	public Collection<JvmType> getAllInstantiateableTypes(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		for (JvmType type : getAllTypes(context)) {
			if (type instanceof JvmGenericType) {
				JvmGenericType genericType = (JvmGenericType) type;
				if (genericType.isInstantiateable() || typeFactory.isUserDefinedType(genericType, false)) {
					result.add(genericType);
				}
			}
		}
		return result;
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
	public Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		collectPublicConstructors(typeRef, constructors, true);
		return constructors.values();
	}
	
	@Override
	public Collection<JvmExecutable> getDeclaredConstructors(JvmTypeReference typeRef) {
		Map<String, JvmExecutable> constructors = new HashMap<>();
		collectPublicConstructors(typeRef, constructors, false);
		return constructors.values();
	}
	
	private void collectPublicConstructors(JvmTypeReference typeRef, Map<String, JvmExecutable> constructors, boolean deep) {
		JvmType type = typeFactory.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			collectPublicConstructors((JvmGenericType)type,constructors, deep);
		} 
	}
	
	private void collectPublicConstructors(JvmGenericType type, Map<String, JvmExecutable> constructors, boolean deep) {	
		for (JvmConstructor constructor : type.getDeclaredConstructors()) {
			int visibility = constructor.getVisibility().getValue();
			if (visibility!=JvmVisibility.PRIVATE_VALUE && visibility!=JvmVisibility.PROTECTED_VALUE&& (typeFactory.isUserDefinedType(type, false) || visibility!=JvmVisibility.DEFAULT_VALUE)) {
				String constructorId = createExecutableID(constructor);
				if (!arrayUsedInExecutable(constructor) && !constructors.containsKey(constructorId)) {
					constructors.put(constructorId, constructor);
				}
			}
		}
		if (typeFactory.isUserDefinedType(type, false)) {
			for (JvmOperation constructor : type.getDeclaredOperations()) {
				if (constructor.getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
					int visibility = constructor.getVisibility().getValue();
					if (visibility!=JvmVisibility.PRIVATE_VALUE && visibility!=JvmVisibility.PROTECTED_VALUE && (typeFactory.isUserDefinedType(type, false) || visibility!=JvmVisibility.DEFAULT_VALUE)) {
						String constructorId = createExecutableID(constructor);
						if (!arrayUsedInExecutable(constructor) && !constructors.containsKey(constructorId)) {
							constructors.put(constructorId, constructor);
						}
					}
				}
			}
		}
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {
			collectPublicConstructors(type.getExtendedClass(), constructors, deep);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (interf != type) {
					collectPublicConstructors(interf, constructors, deep);
				}
			}
		}		
	}
	
	private boolean arrayUsedInExecutable(JvmExecutable exe) {
		for (JvmFormalParameter p : exe.getParameters()) {
			if (typeFactory.isArray(p.getParameterType())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean arrayUsedInMethod(JvmOperation method) {
		for (JvmFormalParameter p : method.getParameters()) {
			if (typeFactory.isArray(p.getParameterType())) {
				return true;
			}
		}
		if (method.getReturnType() != null && typeFactory.isArray(method.getReturnType())) {
			return true;
		}
		return false;
	}
	
	@Override
	public String createExecutableID(JvmExecutable exe){
		return exe.getSimpleName()+"_"+exe.getParameters().size();
	}
	
	
	@Override
	public Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef) {
		Map<String, JvmOperation> methods = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		collectPublicMethods(typeRef, methods, true, visibilities);
		return methods.values();
	}
	
	public Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef) {
		Map<String, JvmOperation> methods = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PROTECTED_VALUE};
		collectPublicMethods(typeRef, methods, true, visibilities);
		return methods.values();
	}
	
	@Override
	public Collection<JvmOperation> getDeclaredPublicMethods(JvmTypeReference typeRef) {
		Map<String, JvmOperation> methods = new HashMap<>();
		int[] visibilities = new int[]{JvmVisibility.PUBLIC_VALUE};
		collectPublicMethods(typeRef, methods, false, visibilities);
		return methods.values();
	}
	
	private void collectPublicMethods(JvmTypeReference typeRef, Map<String, JvmOperation> methods, boolean deep, int[] visibilities) {
		JvmType type = typeFactory.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			collectPublicMethods((JvmGenericType)type,methods, deep, visibilities);
		} 
	}
	
	private void collectPublicMethods(JvmGenericType type, Map<String, JvmOperation> methods, boolean deep, int[] visibilities) {	
		for (JvmOperation method : type.getDeclaredOperations()) {
			boolean success = false;
			if (typeFactory.isUserDefinedType(type, false)) {
				success = true;
			}			
			int visibility = method.getVisibility().getValue();
			for (int v : visibilities) {
				if (visibility == v) {
					success = true;
					break;
				}
			}
			if (success) {
				String methodId = createExecutableID(method);
				if (!arrayUsedInMethod(method) && !methods.containsKey(methodId)) {
					methods.put(methodId, method);
				}
			}
		}
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {
			collectPublicMethods(type.getExtendedClass(), methods, deep, visibilities);
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (interf != type) {
					collectPublicMethods(interf, methods, deep, visibilities);
				}
			}
		}		
	}
	
	


	@Override
	public Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef) {
		JvmType type = typeFactory.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			return getPublicAttributes((JvmGenericType)type, true);
		} else {
			return new HashSet<>();
		}
	}
	
	@Override
	public Collection<JvmField> getDeclaredPublicAttributes(JvmTypeReference typeRef) {
		JvmType type = typeFactory.getInnerType(typeRef, true);
		if (type instanceof JvmGenericType) {
			return getPublicAttributes((JvmGenericType)type, false);
		} else {
			return new HashSet<>();
		}
	}
	
	private Collection<JvmField> getPublicAttributes(JvmGenericType type, boolean deep) {		
		Collection<JvmField> attributes = new HashSet<>();
		for (JvmField attribute : type.getDeclaredFields()) {
			int visibility = attribute.getVisibility().getValue();
			if (visibility!=JvmVisibility.PRIVATE_VALUE && visibility!=JvmVisibility.PROTECTED_VALUE && (typeFactory.isUserDefinedType(type, false) || visibility!=JvmVisibility.DEFAULT_VALUE)) {
				if (!typeFactory.isArray(attribute.getType())) {
					attributes.add(attribute);
				}
			}
		}
		if (deep && type.getExtendedClass()!= null && type != type.getExtendedClass()) {
			attributes.addAll(getPublicAttributes(type.getExtendedClass()));
		}
		if (deep) {
			for (JvmTypeReference interf : type.getExtendedInterfaces()) {
				if (interf != type) {
					attributes.addAll(getPublicAttributes(interf));
				}
			}	
		}	
		return attributes;
	}
	
	private Collection<IQLFile> getAllFiles(Resource context) {
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
