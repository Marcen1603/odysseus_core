package de.uniol.inf.is.odysseus.iql.odl.typing.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.AbstractIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFactory;
import de.uniol.inf.is.odysseus.iql.odl.service.ODLServiceObserver;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;



@Singleton
public class ODLTypeFactory extends AbstractIQLTypeFactory<IODLTypeUtils, ODLServiceObserver> implements IODLTypeFactory {


	private Map<String, JvmTypeReference> parametersByType = new HashMap<>();
	private Map<String, List<JvmTypeReference>> parametersByValue = new HashMap<>();
	
	@Inject
	public ODLTypeFactory(IODLTypeUtils typeUtils, ODLServiceObserver serviceObserver) {
		super(typeUtils, serviceObserver);
	}
	
	@Override
	public String getFileExtension() {
		return "odl";
	}
	
	
	@Override
	public Collection<Bundle> getDependencies() {
		Collection<Bundle> bundles = super.getDependencies();
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.transform"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.ruleengine"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.mep"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.odl"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.sweeparea"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.server.intervalapproach"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.relational.base"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.intervalapproach"));
		return bundles;
	}
	
	
	@Override
	public Collection<JvmType> createVisibleTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> types = super.createVisibleTypes(usedNamespaces, context);
		for (JvmTypeReference parameterValueType : parametersByType.values()) {
			types.add(typeUtils.getInnerType(parameterValueType, false));
		}
		
		for (List<JvmTypeReference> parameterTypes : parametersByValue.values()) {
			for (JvmTypeReference parameterType : parameterTypes) {
				types.add(typeUtils.getInnerType(parameterType, false));
			}
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
		for (JvmTypeReference parameterValueType : parametersByType.values()) {
			implicitImports.add(typeUtils.getLongName(parameterValueType, false));
		}
		
		for (List<JvmTypeReference> parameterTypes : parametersByValue.values()) {
			for (JvmTypeReference parameterType : parameterTypes) {
				implicitImports.add(typeUtils.getLongName(parameterType, false));
			}
		}
		implicitImports.addAll(ODLDefaultTypes.getImplicitImports());	
		return implicitImports;
	}
	
	@Override
	public Collection<String> createImplicitStaticImports() {
		Collection<String> implicitStaticImports = super.createImplicitStaticImports();
		for (Class<?> c : ODLDefaultTypes.getImplicitStaticImports()) {
			implicitStaticImports.add(c.getCanonicalName());
		}
		return implicitStaticImports;
	}

	
	@Override
	protected IQLModel createCleanSystemFile() {
		return ODLFactory.eINSTANCE.createODLModel();
	}
		
	@Override
	public void addParameter(JvmTypeReference parameterType, JvmTypeReference parameterValueType) {	
		parametersByType.put(typeUtils.getLongName(parameterType, true),parameterValueType);
		
		List<JvmTypeReference> parameterTypes = parametersByValue.get(typeUtils.getLongName(parameterValueType, true));
		if (parameterTypes == null) {
			parameterTypes = new ArrayList<>();
			parametersByValue.put(typeUtils.getLongName(parameterValueType, true), parameterTypes);
		}
		parameterTypes.add(parameterType);
		
	}
	
	@Override
	public JvmTypeReference getParameterType(JvmTypeReference valueType) {
		String qName = typeUtils.getLongName(valueType, false);
		List<JvmTypeReference> parameters = parametersByValue.get(qName);
		if (parameters != null && !parameters.isEmpty()) {
			return parameters.get(0);
		}
		return null;
	}


	@Override
	public Collection<JvmTypeReference> getAllParameterValues() {
		return parametersByType.values();
	}

	@Override
	public Collection<JvmTypeReference> getAllParameterTypes() {
		Collection<JvmTypeReference> result = new HashSet<>();
		for (List<JvmTypeReference> list : parametersByValue.values()) {
			for (JvmTypeReference typeRef : list) {
				result.add(typeRef);
			}
		}
		return result;
	}
	
}
