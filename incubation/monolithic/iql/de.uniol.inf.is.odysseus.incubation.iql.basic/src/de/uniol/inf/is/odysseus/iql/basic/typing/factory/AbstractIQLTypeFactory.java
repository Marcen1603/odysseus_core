package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;



@SuppressWarnings("restriction")
public abstract class AbstractIQLTypeFactory<U extends IIQLTypeUtils> implements IIQLTypeFactory{

	@Inject
	private XtextResourceSet systemResourceSet;

	@Inject
	protected TypeReferences typeReferences;
	
	protected U typeUtils;

	@Inject
	IIQLTypingEntryPoint entryPoint;
	
	public AbstractIQLTypeFactory(U typeUtils) {
		this.typeUtils = typeUtils;
	}
	
	private Map<Resource, Collection<JvmType>> resourceTypes = new HashMap<>();
	
	private Map<String, IQLFile> systemFiles = new HashMap<>();
	private Map<String, IQLSystemType> systemTypes= new HashMap<>();

	
	@Override
	public Collection<JvmTypeReference> getImportedTypes(EObject obj) {
		Collection<String> imports = new HashSet<>();
		imports.addAll(getImplicitImports());
		IQLFile file = EcoreUtil2.getContainerOfType(obj, IQLFile.class);
		for (IQLNamespace namespace : file.getNamespaces()) {
			imports.add(namespace.getImportedNamespace().replace(IQLQualifiedNameConverter.DELIMITER, "."));
		}
		
		Collection<JvmTypeReference> result = new HashSet<>();
		for (String i : imports) {
			JvmTypeReference typeRef = typeUtils.createTypeRef(i, getSystemResourceSet());
			if (typeRef != null) {
				result.add(typeRef);
			}
		}
		return result;
	}
	
	@Override
	public ResourceSet getSystemResourceSet() {
		return systemResourceSet;
	}

	
	@Override
	public Collection<JvmType> getVisibleTypes(Resource context) {
		Collection<JvmType> result = resourceTypes.get(context);
		if (result == null) {
			result = createVisibleTypes(context);
			resourceTypes.put(context, result);
		}
		return result;
	}
	
	@Override
	public Collection<String> getImplicitImports() {
		Collection<String> implicitImports = createImplicitImports();
		return implicitImports;
	}


	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		for (JvmGenericType systemType : getSystemTypes()) {
			implicitImports.add(systemType.getPackageName()+"."+systemType.getSimpleName());
		}
		implicitImports.addAll(IQLDefaultTypes.getImplicitImports());		
		return implicitImports;
	}
		
	
	@Override
	public Collection<IQLFile> getSystemFiles() {
		return systemFiles.values();
	}
	
	protected abstract IQLFile createCleanSystemFile();
	
	@Override
	public IQLSystemType addSystemType(JvmGenericType type, Class<?> javaType) {
		resourceTypes.clear();
		IQLSystemType systemType=  new IQLSystemType(type, javaType);
		systemTypes.put(systemType.getIqlTypeDef().getPackageName()+"."+systemType.getIqlTypeDef().getSimpleName(), systemType);		
		String packageName = type.getPackageName();
		IQLFile systemFile = systemFiles.get(packageName);
		if (systemFile == null) {
			systemFile = createCleanSystemFile();		
			systemFile.setName(packageName);
			systemFiles.put(packageName, systemFile);
		}
		IQLTypeDefinition typeDef = BasicIQLFactory.eINSTANCE.createIQLTypeDefinition();
		typeDef.setInner(type);
		systemFile.getElements().add(typeDef);	
		return systemType;
	}
	
	@Override
	public void removeSystemType(String name) {
		IQLSystemType systemType = systemTypes.remove(name);
		if (systemType != null) {
			String packageName = systemType.getIqlTypeDef().getPackageName();
			IQLFile systemFile = systemFiles.get(packageName);
			if (systemFile != null) {
				systemFile.getElements().remove(systemType.getIqlTypeDef());
				if (systemFile.getElements().size() == 0) {
					systemFiles.remove(systemFile);
				}
			}
		}
	}
		
	public Collection<JvmType> createVisibleTypes(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.addAll(getPrimitives(context));
		result.addAll(getSystemTypes());
		
		for (Class<?> c : IQLDefaultTypes.getVisibleTypes()) {
			result.add(typeReferences.findDeclaredType(c.getCanonicalName(), context));
		}
		return result;
	}	
		
	private Collection<JvmType> getPrimitives(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.add(typeReferences.findDeclaredType("byte", context));
		result.add(typeReferences.findDeclaredType("short", context));
		result.add(typeReferences.findDeclaredType("int", context));
		result.add(typeReferences.findDeclaredType("long", context));
		result.add(typeReferences.findDeclaredType("float", context));
		result.add(typeReferences.findDeclaredType("double", context));
		result.add(typeReferences.findDeclaredType("boolean", context));
		result.add(typeReferences.findDeclaredType("char", context));
		result.add(typeReferences.findDeclaredType(Byte.class, context));
		result.add(typeReferences.findDeclaredType(Short.class, context));
		result.add(typeReferences.findDeclaredType(Integer.class, context));
		result.add(typeReferences.findDeclaredType(Long.class, context));
		result.add(typeReferences.findDeclaredType(Float.class, context));
		result.add(typeReferences.findDeclaredType(Double.class, context));
		result.add(typeReferences.findDeclaredType(Boolean.class, context));
		result.add(typeReferences.findDeclaredType(Character.class, context));
		return result;
	}
	
	private Collection<JvmGenericType> getSystemTypes() {
		Collection<JvmGenericType> types = new HashSet<>();
		for (IQLSystemType systemType : systemTypes.values()) {
			types.add(systemType.getIqlTypeDef());
		}
		return types;
	}	

	@Override
	public boolean isSystemFile(String fileName) {
		return getSystemFile(fileName)!= null;
	}

	@Override
	public IQLFile getSystemFile(String fileName) {
		return systemFiles.get(fileName);
	}
		
	@Override
	public boolean isSystemType(String name) {
		return getSystemType(name) != null;
	}

	@Override
	public IQLSystemType getSystemType(String name) {
		return systemTypes.get(name);
	}
	
	@Override
	public Collection<Bundle> getDependencies(IQLFile file) {
		Collection<Bundle> bundles = new HashSet<>();
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.basic"));
		
		for (JvmTypeReference type : EcoreUtil2.getAllContentsOfType(file, JvmTypeReference.class)) {
			addDependancy(type, bundles);
		}		
		return bundles;
	}
	
	private void addDependancy(JvmTypeReference typeRef, Collection<Bundle> bundles) {
		String name = typeUtils.getLongName(typeRef, false);
		if (isSystemType(name)) {
			IQLSystemType systemType = getSystemType(name);
			Bundle bundle = FrameworkUtil.getBundle(systemType.getJavaType());
			if (bundle != null) {
				bundles.add(bundle);
			}
		} else if (!typeUtils.isUserDefinedType(typeRef, false)) {
			try {
				Class<?> c = Class.forName(typeUtils.getLongName(typeRef, false));
				Bundle bundle = FrameworkUtil.getBundle(c);
				if (bundle != null) {
					bundles.add(bundle);
				}
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	public String getImportName(JvmType type) {		
		String name = typeUtils.getLongName(type, false);
		IQLSystemType systemType = this.getSystemType(name);
		if (systemType != null) {
			Class<?> javaType = systemType.getJavaType();
			return javaType.getCanonicalName();
		} else {
			return name;
		}		
	}

	@Override
	public String getSimpleName(JvmType type, String text, boolean wrapper, boolean array) {
		String qualifiedName = typeUtils.getLongName(type, array);	
		IQLSystemType systemType = this.getSystemType(qualifiedName);
		if (systemType != null) {
			if (!isImportNeeded(type, text)) {
				return text;
			} else {
				Class<?> javaType = systemType.getJavaType();
				return javaType.getSimpleName();
			}
		} else {
			Class<?> javaType = typeUtils.getJavaType(qualifiedName);
			if (wrapper && type instanceof JvmPrimitiveType) {
				return toWrapper(qualifiedName);
			} else if (javaType != null) {
				if (!isImportNeeded(type, text)) {
					return text;
				} else {
					return javaType.getSimpleName();
				}
			} else if (text != null){
				return text;
			} else {
				return qualifiedName;
			}
		}
	}
	
	@Override
	public boolean isImportNeeded(JvmType type, String text) {
		if (typeUtils.isUserDefinedType(type, false)) {
			return false;
		} else if (typeUtils.isPrimitive(type)) {
			return false;
		}else {
			try {
				Class.forName(text);
				return false;
			} catch (Exception e) {
				return true;
			}
		}
	}
	
	protected String toWrapper(String name) {
		if (name.equals("byte")) {
			return Byte.class.getSimpleName();
		} else if (name.equals("short")) {
			return Short.class.getSimpleName();
		} else if (name.equals("int")) {
			return Integer.class.getSimpleName();
		} else if (name.equals("long")) {
			return Long.class.getSimpleName();
		} else if (name.equals("float")) {
			return Float.class.getSimpleName();
		} else if (name.equals("double")) {
			return Double.class.getSimpleName();
		} else if (name.equals("char")) {
			return Character.class.getSimpleName();
		} else if (name.equals("boolean")) {
			return Boolean.class.getSimpleName();
		} else {
			return name;
		}
	}
	
}

