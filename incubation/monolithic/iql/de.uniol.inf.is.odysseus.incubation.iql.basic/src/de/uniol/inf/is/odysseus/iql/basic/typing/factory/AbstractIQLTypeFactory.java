package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;






import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;













import de.uniol.inf.is.odysseus.iql.basic.Activator;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;



@SuppressWarnings({ "restriction", "deprecation" })
public abstract class AbstractIQLTypeFactory<U extends IIQLTypeUtils, I extends IIQLServiceObserver> implements IIQLTypeFactory{

	@Inject
	private XtextResourceSet systemResourceSet;
		
	@Inject
	protected IQLClasspathTypeProviderFactory typeProviderFactory;
	
	@Inject
	protected IQLQualifiedNameConverter converter;
	
	protected U typeUtils;
	
	protected I serviceObserver;


	@Inject
	IIQLTypingEntryPoint entryPoint;
		
	public AbstractIQLTypeFactory(U typeUtils, I serviceObserver) {
		this.typeUtils = typeUtils;
		this.serviceObserver = serviceObserver;
	}
	
	
	private Map<String, IQLModel> systemFiles = new HashMap<>();
	private Map<String, IQLSystemType> systemTypes= new HashMap<>();

	
	@Override
	public Collection<JvmTypeReference> getStaticImports(EObject obj) {
		Collection<String> imports = new HashSet<>();
		imports.addAll(getImplicitStaticImports());
		IQLModel model = EcoreUtil2.getContainerOfType(obj, IQLModel.class);
		for (IQLNamespace namespace : model.getNamespaces()) {
			if (namespace.isStatic()) {
				imports.add(namespace.getImportedNamespace().replace(IQLQualifiedNameConverter.DELIMITER, "."));
			}
		}
		
		Collection<JvmTypeReference> result = new HashSet<>();
		for (String i : imports) {
			if (!i.endsWith("*")) {
				JvmTypeReference typeRef = typeUtils.createTypeRef(i, EcoreUtil2.getResourceSet(obj));
				if (typeRef != null) {
					result.add(typeRef);
				}
			}
		}
		return result;
	}
	
	@Override
	public ResourceSet getSystemResourceSet() {
		return systemResourceSet;
	}

	
	@Override
	public Collection<JvmType> getVisibleTypes(Collection<String> usedNamespaces, Resource context) {
		return createVisibleTypes(usedNamespaces, context);
	}
	
	@Override
	public Collection<String> getImplicitImports() {
		Collection<String> implicitImports = createImplicitImports();
		return implicitImports;
	}
	
	@Override
	public Collection<String> getImplicitStaticImports() {
		Collection<String> implicitImports = createImplicitStaticImports();
		return implicitImports;
	}


	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		for (JvmGenericType systemType : getSystemTypes()) {
			implicitImports.add(systemType.getPackageName()+"."+systemType.getSimpleName());
		}
		
		implicitImports.addAll(IQLDefaultTypes.getImplicitImports());		
		implicitImports.addAll(serviceObserver.getImplicitImports());		

		return implicitImports;
	}
	
	public Collection<String> createImplicitStaticImports() {
		Collection<String> implicitStaticImports = new HashSet<>();
		
		for (Class<?> c : IQLDefaultTypes.getImplicitStaticImports()) {
			implicitStaticImports.add(c.getCanonicalName());
		}
		for (Class<?> c : serviceObserver.getImplicitStaticImports()) {
			implicitStaticImports.add(c.getCanonicalName());
		}
		return implicitStaticImports;
	}
		
	
	@Override
	public Collection<IQLModel> getSystemFiles() {
		return systemFiles.values();
	}
	
	protected abstract IQLModel createCleanSystemFile();
	
	@Override
	public IQLSystemType addSystemType(JvmGenericType type, Class<?> javaType) {
		IQLSystemType systemType=  new IQLSystemType(type, javaType);
		systemTypes.put(systemType.getIqlTypeDef().getPackageName()+"."+systemType.getIqlTypeDef().getSimpleName(), systemType);		
		String packageName = type.getPackageName();
		IQLModel systemFile = systemFiles.get(packageName);
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
			IQLModel systemFile = systemFiles.get(packageName);
			if (systemFile != null) {
				systemFile.getElements().remove(systemType.getIqlTypeDef());
				if (systemFile.getElements().size() == 0) {
					systemFiles.remove(systemFile);
				}
			}
		}
	}
		
	public Collection<JvmType> createVisibleTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.addAll(getPrimitives(context));
		result.addAll(getSystemTypes());
		
		for (Class<?> c : IQLDefaultTypes.getVisibleTypes()) {
			result.add(getType(c.getCanonicalName(), context));
		}
		
		for (Class<?> c : serviceObserver.getVisibleTypes()) {
			result.add(getType(c.getCanonicalName(), context));
		}
		
		for (String namespace : usedNamespaces) {
			if (namespace.endsWith("*")) {
				String packageName = namespace.substring(0, namespace.length()-2);
				result.addAll(getTypesOfPackage(packageName, context));
			} else {
				JvmType t = getType(namespace, context);
				if (t != null) {
					result.add(t);
				}
			}
		}
		
		return result;
	}	
	
	public JvmType getType(String name, Notifier context) {
		ResourceSet set = EcoreUtil2.getResourceSet(context);
		return typeProviderFactory.findOrCreateTypeProvider(set).findTypeByName(name);
	}
		
	public Collection<JvmType> getTypesOfPackage(String packageName, Resource context) {
		Collection<JvmType> result = new HashSet<>();

		Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
		for (Class<?> c : reflections.getSubTypesOf(Object.class)) {
			String str = c.getCanonicalName();
			if (str != null) {
				result.add(getType(c.getCanonicalName(), context));
			}
		}
		return result;
	}
	
	public Collection<JvmType> getTypesOfBundle(Bundle bundle, Resource context) {
		Collection<JvmType> result = new HashSet<>();

		BundleWiring bundleWiring = Activator.getContext().getBundle().adapt(BundleWiring.class);
		Collection<String> resources = bundleWiring.listResources("/", "*.class", BundleWiring.LISTRESOURCES_RECURSE);

		for (String resource : resources) {
		    URL localResource = bundle.getEntry(resource);
		    if (localResource != null) {
		        String className = resource.replaceAll("/", ".").replace(".class", "").replace("bin.", "");
		        result.add(getType(className, context));
		    }
		}
		return result;
	}
	
	
	private Collection<JvmType> getPrimitives(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.add(getType("byte", context));
		result.add(getType("short", context));
		result.add(getType("int", context));
		result.add(getType("long", context));
		result.add(getType("float", context));
		result.add(getType("double", context));
		result.add(getType("boolean", context));
		result.add(getType("char", context));
		result.add(getType(Byte.class.getCanonicalName(), context));
		result.add(getType(Short.class.getCanonicalName(), context));
		result.add(getType(Integer.class.getCanonicalName(), context));
		result.add(getType(Long.class.getCanonicalName(), context));
		result.add(getType(Float.class.getCanonicalName(), context));
		result.add(getType(Double.class.getCanonicalName(), context));
		result.add(getType(Boolean.class.getCanonicalName(), context));
		result.add(getType(Character.class.getCanonicalName(), context));
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
	public IQLModel getSystemFile(String fileName) {
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
	public Collection<Bundle> getDependencies() {
		Collection<Bundle> bundles = new HashSet<>();
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.basic"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.core"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.core.server"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.slf4j"));
		bundles.addAll(this.serviceObserver.getDependencies());
		return bundles;
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
	public boolean isImportNeeded(JvmType type, String text) {
		if (type instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) type;
			if (isSystemType(typeUtils.getLongName(type, false))) {
				return true;
			} else if (typeUtils.isUserDefinedType(declaredType, false)) {
				return false;
			} else if (typeUtils.isPrimitive(declaredType)) {
				return false;
			} else if (declaredType.getPackageName() == null) {
				return false;
			} else {
				try {
					Class.forName(text);
					return false;
				} catch (Exception e) {
					return true;
				}
			}
			
		} else {
			return false;
		}
	}

	@Override
	public String getSimpleName(JvmType type, String text, boolean wrapper, boolean array) {
		String qualifiedName = typeUtils.getLongName(type, array);	
		String simpleName = typeUtils.getShortName(type, array);	
		IQLSystemType systemType = this.getSystemType(qualifiedName);
		if (systemType != null) {
			if (!isImportNeeded(type, text)) {
				return converter.toJavaString(text);
			} else {
				Class<?> javaType = systemType.getJavaType();
				return javaType.getSimpleName();
			}
		} else {
			Class<?> javaType = null;
			try {
				javaType =  Class.forName(qualifiedName);
			} catch (ClassNotFoundException e) {
			}
			if (wrapper && typeUtils.isPrimitive(type)) {
				return toWrapper(qualifiedName);
			} else if (!isImportNeeded(type, text)) {
				return text;				
			} else if (javaType != null) {
				return javaType.getSimpleName();
			} else {
				return simpleName;
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<String> getJavaPackages() {
		Collection<String> result = new HashSet<>();
		
		ServiceReference ref = Activator.getContext().getServiceReference(PackageAdmin.class.getName());
		PackageAdmin packageAdmin = (PackageAdmin) Activator.getContext().getService(ref);
		
		for (Bundle bundle : getDependencies()) {
			try {
				for (ExportedPackage p : packageAdmin.getExportedPackages(bundle)) {
					result.add(p.getName());
				}
			} catch (IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}

