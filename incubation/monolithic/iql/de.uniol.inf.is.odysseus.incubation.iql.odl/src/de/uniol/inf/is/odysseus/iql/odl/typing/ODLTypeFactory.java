package de.uniol.inf.is.odysseus.iql.odl.typing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.AbstractIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFactory;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;


@Singleton
public class ODLTypeFactory extends AbstractIQLTypeFactory {
	public static final String VALUE_TYPE = "value";
	public static final String KEY_TYPE = "key";
	public static final String ELEMENT_TYPE = "element";
	
	private Map<String, JvmTypeReference> parametersByType = new HashMap<>();
	private Map<String, List<JvmTypeReference>> parametersByValue = new HashMap<>();
	
	@Override
	public String getFileExtension() {
		return "odl";
	}
	
	
	@SuppressWarnings("restriction")
	@Override
	public Collection<JvmType> createVisibleTypes(Resource context) {
		Collection<JvmType> types = super.createVisibleTypes(context);
		for (JvmTypeReference parameterValueType : parametersByType.values()) {
			types.add(getInnerType(parameterValueType, false));
		}
		
		for (List<JvmTypeReference> parameterTypes : parametersByValue.values()) {
			for (JvmTypeReference parameterType : parameterTypes) {
				types.add(getInnerType(parameterType, false));
			}
		}
		for (Class<?> c : ODLDefaultTypes.getVisibleTypes()) {
			types.add(typeReferences.findDeclaredType(c.getCanonicalName(), context));
		}
		return types;
	}
	
	@Override
	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = super.createImplicitImports();
		for (JvmTypeReference parameterValueType : parametersByType.values()) {
			implicitImports.add(getLongName(parameterValueType, false));
		}
		
		for (List<JvmTypeReference> parameterTypes : parametersByValue.values()) {
			for (JvmTypeReference parameterType : parameterTypes) {
				implicitImports.add(getLongName(parameterType, false));
			}
		}
		implicitImports.addAll(ODLDefaultTypes.getImplicitImports());	
		return implicitImports;
	}
	
	@Override
	protected IQLFile createCleanSystemFile() {
		return ODLFactory.eINSTANCE.createODLFile();
	}
	
	@Override
	public Collection<Bundle> getDependencies(IQLFile file) {
		Collection<Bundle> bundles = super.getDependencies(file);
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.slf4j"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.core"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.core.server"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.odl"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.ruleengine"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.transform"));
		return bundles;
	}
	

	public void addParameter(JvmTypeReference parameterType, JvmTypeReference parameterValueType) {	
		parametersByType.put(getLongName(parameterType, true),parameterValueType);
		
		List<JvmTypeReference> parameterTypes = parametersByValue.get(getLongName(parameterValueType, true));
		if (parameterTypes == null) {
			parameterTypes = new ArrayList<>();
			parametersByValue.put(getLongName(parameterValueType, true), parameterTypes);
		}
		parameterTypes.add(parameterType);
		
	}
	

	
	public JvmTypeReference getParameterType(JvmTypeReference valueType) {
		String qName = getLongName(valueType, true);
		List<JvmTypeReference> parameters = parametersByValue.get(qName);
		if (parameters != null && !parameters.isEmpty()) {
			return parameters.get(0);
		}
		return null;
	}
	
	public JvmTypeReference getListElementType(ODLParameter p) {
		JvmTypeReference typeRef = p.getType();
		if (typeRef instanceof IQLArrayTypeRef) {
			IQLArrayTypeRef arrayTypeRef = (IQLArrayTypeRef) typeRef;
			if (arrayTypeRef.getType().getDimensions().size() > 1) {
				return getTypeRef(List.class);
			} else {
				return getTypeRef(getInnerType(arrayTypeRef, false));
			}
		}

		IQLMetadataList metadataList = p.getMetadataList();
		if (metadataList != null) {
			for (IQLMetadata metadata : metadataList.getElements()) {
				if (metadata.getName().equalsIgnoreCase(ELEMENT_TYPE) && metadata.getValue() instanceof IQLMetadataValueSingleTypeRef) {
					IQLMetadataValueSingleTypeRef t = (IQLMetadataValueSingleTypeRef) metadata.getValue();				
					return t.getValue();
				}
			}
		}
		return getTypeRef(Object.class);
	}


	public JvmTypeReference getMapKeyType(ODLParameter p) {
		IQLMetadataList metadataList = p.getMetadataList();
		if (metadataList != null) {
			for (IQLMetadata metadata : metadataList.getElements()) {
				if (metadata.getName().equalsIgnoreCase(KEY_TYPE) && metadata.getValue() instanceof IQLMetadataValueSingleTypeRef) {
					IQLMetadataValueSingleTypeRef t = (IQLMetadataValueSingleTypeRef) metadata.getValue();				
					return t.getValue();
				}
			}
		}
		return getTypeRef(String.class);
	}


	public JvmTypeReference getMapValueType(ODLParameter p) {
		IQLMetadataList metadataList = p.getMetadataList();
		if (metadataList != null) {
			for (IQLMetadata metadata : metadataList.getElements()) {
				if (metadata.getName().equalsIgnoreCase(VALUE_TYPE) && metadata.getValue() instanceof IQLMetadataValueSingleTypeRef) {
					IQLMetadataValueSingleTypeRef t = (IQLMetadataValueSingleTypeRef) metadata.getValue();				
					return t.getValue();
				}
			}
		}
		return getTypeRef(Object.class);
	}


	public Collection<JvmTypeReference> getAllParameterValues() {
		return parametersByType.values();
	}


	
}
