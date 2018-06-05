package de.uniol.inf.is.odysseus.iql.basic.typing.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
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
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.Activator;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.ParameterFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

@SuppressWarnings({ "restriction", "deprecation" })
public abstract class AbstractIQLTypeDictionary<U extends IIQLTypeUtils, I extends IIQLServiceObserver>
		implements IIQLTypeDictionary {

	private XtextResourceSet systemResourceSet;

	protected IQLClasspathTypeProviderFactory typeProviderFactory;

	protected IQLQualifiedNameConverter converter;

	protected U typeUtils;

	protected I serviceObserver;

	private Map<String, Collection<Class<?>>> visibleBundleTypes = new HashMap<>();

	@Inject
	IIQLTypingEntryPoint entryPoint;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractIQLTypeDictionary.class);

	public AbstractIQLTypeDictionary(U typeUtils, I serviceObserver, XtextResourceSet systemResourceSet,
			IQLClasspathTypeProviderFactory typeProviderFactory, IQLQualifiedNameConverter converter) {
		this.systemResourceSet = systemResourceSet;
		this.typeUtils = typeUtils;
		this.serviceObserver = serviceObserver;
		this.typeProviderFactory = typeProviderFactory;
		this.converter = converter;
	}

	private Map<String, IQLModel> systemFiles = new HashMap<>();
	private Map<String, IQLSystemType> systemTypes = new HashMap<>();
	private Map<String, IIQLSystemTypeCompiler> systemTypeCompilers = new HashMap<>();

	@Override
	public Class<?> getParameterValue(Class<? extends IParameter<?>> parameterType) {
		Class<?> result = ParameterFactory.getParameterValue(parameterType);
		if (result == null) {
			result = serviceObserver.getParameters().get(parameterType);
		}
		return result;
	}

	@Override
	public Collection<JvmTypeReference> getStaticImports(EObject obj) {
		Collection<String> imports = new HashSet<>();
		imports.addAll(getImplicitStaticImports());
		IQLModel model = EcoreUtil2.getContainerOfType(obj, IQLModel.class);
		for (IQLNamespace namespace : model.getNamespaces()) {
			if (namespace.isStatic()) {
				imports.add(namespace.getImportedNamespace());
			}
		}

		Collection<JvmTypeReference> result = new HashSet<>();
		for (String i : imports) {
			if (!i.endsWith("*")) {
				if (isSystemType(i)) {
					JvmGenericType type = getSystemType(i).getType();
					result.add(typeUtils.createTypeRef(type));
				} else {
					JvmTypeReference typeRef = typeUtils.createTypeRef(i, EcoreUtil2.getResourceSet(obj));
					if (typeRef != null) {
						result.add(typeRef);
					}
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
		for (JvmDeclaredType systemType : getSystemTypes()) {
			implicitImports.add(
					systemType.getPackageName() + IQLQualifiedNameConverter.DELIMITER + systemType.getSimpleName());
		}
		for (String i : IQLDefaultTypes.getImplicitImports()) {
			implicitImports.add(converter.toIQLString(i));
		}
		for (String i : serviceObserver.getImplicitImports()) {
			implicitImports.add(converter.toIQLString(i));
		}
		return implicitImports;
	}

	public Collection<String> createImplicitStaticImports() {
		Collection<String> implicitStaticImports = new HashSet<>();

		for (String type : IQLDefaultTypes.getImplicitStaticImports()) {
			implicitStaticImports.add(converter.toIQLString(type));
		}
		for (String type : serviceObserver.getImplicitStaticImports()) {
			implicitStaticImports.add(converter.toIQLString(type));
		}
		return implicitStaticImports;
	}

	@Override
	public Collection<IQLModel> getSystemFiles() {
		return systemFiles.values();
	}

	public Collection<JvmType> createVisibleTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.addAll(getPrimitives(context));
		result.addAll(getSystemTypes());

		for (Class<?> c : IQLDefaultTypes.getVisibleTypes()) {
			JvmType type = getType(c.getCanonicalName(), context);
			if (type != null) {
				result.add(type);
			}
		}

		for (Class<?> c : serviceObserver.getVisibleTypes()) {
			JvmType type = getType(c.getCanonicalName(), context);
			if (type != null) {
				result.add(type);
			}
		}

		for (Bundle bundle : getVisibleTypesFromBundle()) {
			Collection<Class<?>> types = visibleBundleTypes.get(bundle.getSymbolicName());

			if (types == null) {
				types = getTypesOfBundle(bundle);
				visibleBundleTypes.put(bundle.getSymbolicName(), types);
			}
			for (Class<?> c : types) {
				JvmType type = getType(c.getCanonicalName(), context);
				if (type != null) {
					result.add(type);
				}
			}
		}

		for (String namespace : usedNamespaces) {
			if (namespace.endsWith("*")) {
				String packageName = namespace.substring(0, namespace.length() - 2);
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

	private Collection<Class<?>> getTypesOfBundle(Bundle bundle) {
		Collection<Class<?>> result = new HashSet<>();
		result.addAll(findTypesOfBundle(bundle));
		String[] classPathEntries = getBundleClasspath(bundle);
		if (classPathEntries != null) {
			for (String entry : classPathEntries) {
				result.addAll(getTypesOfJar(entry, bundle));
			}
		}
		return result;
	}

	@Override
	public String[] getBundleClasspath(Bundle bundle) {
		Dictionary<String, String> d = bundle.getHeaders();
		String classPath = d.get(Constants.BUNDLE_CLASSPATH);
		if (classPath != null) {
			return classPath.split(",");
		}
		return null;
	}

	private Collection<Class<?>> findTypesOfBundle(Bundle bundle) {
		Collection<Class<?>> result = new HashSet<>();

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		Collection<String> resources = bundleWiring.listResources("/", "*.class", BundleWiring.LISTRESOURCES_RECURSE);

		for (String resource : resources) {
			URL localResource = bundle.getEntry(resource);
			if (localResource != null) {
				String className = resource.replaceAll("/", ".").replace(".class", "").replace("bin.", "");
				try {
					result.add(Class.forName(className));
				} catch (ClassNotFoundException e) {
				}
			}
		}
		return result;
	}

	private Collection<Class<?>> getTypesOfJar(String jarName, Bundle bundle) {
		File bundleDir = getBundleDir(bundle);
		File jarFile = new File(bundleDir, jarName);
		Collection<Class<?>> result = new HashSet<>();
		try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile))) {
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					String className = entry.getName().replace('/', '.');
					className = className.substring(0, className.length() - ".class".length());
					try {
						Class<?> c = Class.forName(className);
						if (c != null) {
							result.add(c);
						}
					} catch (Exception e1) {
					}
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	private File getBundleDir(Bundle bundle) {
		try {
			URL url = FileLocator.toFileURL(FileLocator.find(bundle, new Path(""), null));
			return new File(url.toURI());
		} catch (Exception e) {
			LOG.warn("could not find plugin dir " + bundle.getSymbolicName(), e);
		}
		return null;
	}

	public JvmType getType(String name, Notifier context) {
		ResourceSet set = EcoreUtil2.getResourceSet(context);
		try {
			return typeProviderFactory.findOrCreateTypeProvider(set).findTypeByName(name);
		} catch (Exception e) {
			return null;
		}
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

	private Collection<JvmDeclaredType> getSystemTypes() {
		Collection<JvmDeclaredType> types = new HashSet<>();
		for (IQLSystemType systemType : systemTypes.values()) {
			types.add(systemType.getType());
		}
		return types;
	}

	@Override
	public boolean isSystemFile(String fileName) {
		return getSystemFile(fileName) != null;
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
	public boolean hasSystemTypeCompiler(String name) {
		return systemTypeCompilers.containsKey(name.toLowerCase());
	}

	@Override
	public IIQLSystemTypeCompiler getSystemTypeCompiler(String name) {
		return systemTypeCompilers.get(name.toLowerCase());
	}

	@Override
	public void addSystemType(IQLSystemType systemType) {
		String name = systemType.getType().getPackageName() + IQLQualifiedNameConverter.DELIMITER
				+ systemType.getType().getSimpleName();
		systemTypes.put(name.toLowerCase(), systemType);
		String packageName = systemType.getType().getPackageName();
		IQLModel systemFile = systemFiles.get(packageName);
		if (systemFile == null) {
			systemFile = BasicIQLFactory.eINSTANCE.createIQLModel();
			systemFile.setName(packageName);
			systemFiles.put(packageName, systemFile);
		}
		IQLModelElement element = BasicIQLFactory.eINSTANCE.createIQLModelElement();
		element.setInner(systemType.getType());
		systemFile.getElements().add(element);
	}

	@Override
	public void addSystemType(IQLSystemType systemType, IIQLSystemTypeCompiler compiler) {
		String name = systemType.getType().getPackageName() + IQLQualifiedNameConverter.DELIMITER
				+ systemType.getType().getSimpleName();
		systemTypeCompilers.put(name.toLowerCase(), compiler);
		addSystemType(systemType);
	}

	@Override
	public void removeSystemType(String name) {
		systemTypeCompilers.remove(name.toLowerCase());
		IQLSystemType systemType = systemTypes.remove(name.toLowerCase());
		if (systemType != null) {
			String packageName = systemType.getType().getPackageName();
			IQLModel systemFile = systemFiles.get(packageName);
			if (systemFile != null) {
				systemFile.getElements().remove(systemType.getType());
				if (systemFile.getElements().size() == 0) {
					systemFiles.remove(systemFile);
				}
			}
		}
	}

	@Override
	public IQLSystemType getSystemType(String name) {
		return systemTypes.get(name.toLowerCase());
	}

	@Override
	public Collection<String> getImportedPackages() {
		Collection<String> packages = new HashSet<>();
		for (String p : IQLDefaultTypes.getImportedPackages()) {
			packages.add(p);
		}
		packages.addAll(this.serviceObserver.getImportedPackages());
		return packages;
	}

	@Override
	public Collection<Bundle> getRequiredBundles() {
		Collection<Bundle> bundles = new HashSet<>();
		for (String bundleName : IQLDefaultTypes.getDependencies()) {
			bundles.add(Platform.getBundle(bundleName));
		}
		bundles.addAll(this.serviceObserver.getRequiredBundles());
		return bundles;
	}

	@Override
	public Collection<Bundle> getVisibleTypesFromBundle() {
		Collection<Bundle> bundles = new HashSet<>();
		for (String bundleName : IQLDefaultTypes.getVisibleTypesFromBundle()) {
			bundles.add(Platform.getBundle(bundleName));
		}
		bundles.addAll(this.serviceObserver.getVisibleTypesFromBundle());
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
			return converter.toJavaString(name);
		}
	}

	@Override
	public boolean isImportNeeded(JvmType type, String text) {
		if (type instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) type;
			if (isSystemType(typeUtils.getLongName(type, false))) {
				return true;
			} else if (typeUtils.isUserDefinedType(declaredType, false)
					&& !typeUtils.getShortName(type, false).equalsIgnoreCase(typeUtils.getLongName(type, false))) {
				return true;
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
				javaType = Class.forName(converter.toJavaString(qualifiedName));
			} catch (ClassNotFoundException e) {
			}
			if (wrapper && typeUtils.isPrimitive(type)) {
				return toWrapper(qualifiedName);
			} else if (text != null && !isImportNeeded(type, text)) {
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

		for (Bundle bundle : getRequiredBundles()) {
			try {
				for (ExportedPackage p : packageAdmin.getExportedPackages(bundle)) {
					result.add(p.getName());
				}
			} catch (IllegalArgumentException | SecurityException e) {
				LOG.warn("error while getting java packages", e);
			}
		}
		return result;
	}

}
