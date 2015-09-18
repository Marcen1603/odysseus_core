package de.uniol.inf.is.odysseus.iql.odl.typing.dictionary;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.ParameterFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.AbstractIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.service.ODLServiceObserver;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;



@Singleton
public class ODLTypeDictionary extends AbstractIQLTypeDictionary<IODLTypeUtils, ODLServiceObserver> implements IODLTypeDictionary {

	@Inject
	public ODLTypeDictionary(IODLTypeUtils typeUtils,ODLServiceObserver serviceObserver,XtextResourceSet systemResourceSet,
			IQLClasspathTypeProviderFactory typeProviderFactory,IQLQualifiedNameConverter converter) {
		super(typeUtils, serviceObserver, systemResourceSet, typeProviderFactory,converter);
	}

	
	@Override
	public String getFileExtension() {
		return "odl";
	}
	
	@Override
	public Collection<String> getImportedPackages() {
		Collection<String> packages = super.getImportedPackages();
		for (String p : ODLDefaultTypes.getImportedPackages()) {
			packages.add(p);
		}
		return packages;
	}
		
	@Override
	public Collection<Bundle> getRequiredBundles() {
		Collection<Bundle> bundles = super.getRequiredBundles();
		for (String bundleName : ODLDefaultTypes.getDependencies()) {
			bundles.add(Platform.getBundle(bundleName));
		}
		return bundles;
	}
	
	@Override
	public Collection<Bundle> getVisibleTypesFromBundle() {
		Collection<Bundle> bundles = super.getVisibleTypesFromBundle();
		for (String bundleName : ODLDefaultTypes.getVisibleTypesFromBundle()) {
			bundles.add(Platform.getBundle(bundleName));
		}
		return bundles;
	}
	
	
	@Override
	public Collection<JvmType> createVisibleTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> types = super.createVisibleTypes(usedNamespaces, context);

		for (JvmTypeReference typeRef : getAllParameterTypes(context)) {
			types.add(typeUtils.getInnerType(typeRef, false));
		}
		for (JvmTypeReference typeRef : getAllParameterValues(context)) {
			types.add(typeUtils.getInnerType(typeRef, false));
		}
		for (Class<?> c : ODLDefaultTypes.getVisibleTypes()) {
			JvmType t = getType(c.getCanonicalName(), context);
			types.add(t);
		}
		return types;
	}
	
	@Override
	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = super.createImplicitImports();
		for (Class<? extends IParameter<?>> parameterType : ParameterFactory.getParameters()) {
			Class<?> valueType = ParameterFactory.getParameterValue(parameterType);
			implicitImports.add(converter.toIQLString(parameterType.getCanonicalName()));
			implicitImports.add(converter.toIQLString(valueType.getCanonicalName()));
		}
		for (Entry<Class<? extends IParameter<?>>, Class<?>> entry : serviceObserver.getParameters().entrySet()) {
			implicitImports.add(converter.toIQLString(entry.getKey().getCanonicalName()));
			implicitImports.add(converter.toIQLString(entry.getValue().getCanonicalName()));
		}
		
		for (String i : ODLDefaultTypes.getImplicitImports()) {
			implicitImports.add(converter.toIQLString(i));
		}
	
		return implicitImports;
	}
	
	@Override
	public Collection<String> createImplicitStaticImports() {
		Collection<String> implicitStaticImports = super.createImplicitStaticImports();
		for (String type : ODLDefaultTypes.getImplicitStaticImports()) {
			implicitStaticImports.add(converter.toIQLString(type));
		}
		return implicitStaticImports;
	}

	
	@Override
	public JvmTypeReference getParameterType(JvmTypeReference valueType, Notifier context) {
		if (typeUtils.isPrimitive(valueType)) {
			valueType = typeUtils.getWrapper(valueType, context);
		} 
		String qName = converter.toJavaString(typeUtils.getLongName(valueType, false));
		for (Class<? extends IParameter<?>> parameterType : ParameterFactory.getParameters()) {
			Class<?> valueTypeClass = ParameterFactory.getParameterValue(parameterType);
			if (qName.equalsIgnoreCase(valueTypeClass.getCanonicalName())) {
				return typeUtils.createTypeRef(parameterType, context);
			}			
		}
		for (Entry<Class<? extends IParameter<?>>, Class<?>> entry : serviceObserver.getParameters().entrySet()) {
			if (qName.equalsIgnoreCase(entry.getValue().getCanonicalName())) {
				return typeUtils.createTypeRef(entry.getKey(), context);
			}
		}
		return null;
	}


	@Override
	public Collection<JvmTypeReference> getAllParameterValues(Notifier context) {
		Collection<JvmTypeReference> result = new HashSet<>();
		for (Class<? extends IParameter<?>> parameterType : ParameterFactory.getParameters()) {
			Class<?> valueType = ParameterFactory.getParameterValue(parameterType);
			JvmTypeReference typeRef = typeUtils.createTypeRef(valueType, context);
			if (typeRef != null) {
				result.add(typeRef);
			}
		}
		for (Entry<Class<? extends IParameter<?>>, Class<?>> entry : serviceObserver.getParameters().entrySet()) {
			JvmTypeReference typeRef = typeUtils.createTypeRef(entry.getValue(), context);
			if (typeRef != null) {
				result.add(typeRef);
			}
		}
		return result;
	}

	@Override
	public Collection<JvmTypeReference> getAllParameterTypes(Notifier context) {
		Collection<JvmTypeReference> result = new HashSet<>();
		for (Class<? extends IParameter<?>> parameterType : ParameterFactory.getParameters()) {
			JvmTypeReference typeRef = typeUtils.createTypeRef(parameterType, context);
			if (typeRef != null) {
				result.add(typeRef);
			}
		}
		for (Entry<Class<? extends IParameter<?>>, Class<?>> entry : serviceObserver.getParameters().entrySet()) {
			JvmTypeReference typeRef = typeUtils.createTypeRef(entry.getKey(), context);
			if (typeRef != null) {
				result.add(typeRef);
			}
		}
		return result;
	}
	
}
