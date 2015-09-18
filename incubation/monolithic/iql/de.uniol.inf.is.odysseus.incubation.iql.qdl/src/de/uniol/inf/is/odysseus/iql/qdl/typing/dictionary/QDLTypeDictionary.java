package de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.AbstractIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceObserver;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;



@Singleton
public class QDLTypeDictionary extends AbstractIQLTypeDictionary<IQDLTypeUtils, QDLServiceObserver> implements IQDLTypeDictionary {
	
	private Map<String, ILogicalOperator> sources = new HashMap<String, ILogicalOperator>();	
	private Map<String, IQLClass> sourceTypes = new HashMap<String, IQLClass>();

	private Map<String, IOperatorBuilder> operators = new HashMap<String, IOperatorBuilder>();
	private Map<String, IQLClass> operatorTypes = new HashMap<String, IQLClass>();
	private Map<String, Map<String, String[]>> operatorParameterNames = new HashMap<>();
	private Map<String, Map<String, Parameter>> operatorParameterTypes = new HashMap<>();

	private Set<Class<?>> parameterValueTypes = new HashSet<>();
	
	@Inject
	public QDLTypeDictionary(IQDLTypeUtils typeUtils,QDLServiceObserver serviceObserver,XtextResourceSet systemResourceSet,
			IQLClasspathTypeProviderFactory typeProviderFactory,IQLQualifiedNameConverter converter) {
		super(typeUtils, serviceObserver, systemResourceSet, typeProviderFactory,converter);
	}

	
	
	@Override
	public Collection<String> getImportedPackages() {
		Collection<String> packages = super.getImportedPackages();
		for (String p : QDLDefaultTypes.getImportedPackages()) {
			packages.add(p);
		}
		return packages;
	}
	
	@Override
	public Collection<Bundle> getRequiredBundles() {
		Collection<Bundle> bundles =super.getRequiredBundles();
		for (String bundleName : QDLDefaultTypes.getDependencies()) {
			bundles.add(Platform.getBundle(bundleName));
		}
		return bundles;
	}
	
	@Override
	public Collection<Bundle> getVisibleTypesFromBundle() {
		Collection<Bundle> bundles =super.getVisibleTypesFromBundle();
		for (String bundleName : QDLDefaultTypes.getVisibleTypesFromBundle()) {
			bundles.add(Platform.getBundle(bundleName));
		}
		return bundles;
	}
	
	@Override
	public String getFileExtension() {
		return "qdl";
	}
	
	
	@Override
	public Collection<JvmType> createVisibleTypes(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> types = super.createVisibleTypes(usedNamespaces, context);
		for (Class<?> parameterValueType : parameterValueTypes) {
			JvmType t = getType(parameterValueType.getCanonicalName(), context);
			if (t != null) {
				types.add(t);
			}
		}
		for (Class<?> c : QDLDefaultTypes.getVisibleTypes()) {
			types.add(getType(c.getCanonicalName(), context));
		}
		return types;
	}
	
	@Override
	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = super.createImplicitImports();
		for (Class<?> parameterValueType : parameterValueTypes) {
			implicitImports.add(converter.toIQLString(parameterValueType.getCanonicalName()));
		}
		for (String i : QDLDefaultTypes.getImplicitImports()) {
			implicitImports.add(converter.toIQLString(i));
		}
		return implicitImports;
	}
	
	@Override
	public Collection<String> createImplicitStaticImports() {
		Collection<String> implicitStaticImports = super.createImplicitStaticImports();
		for (String type : QDLDefaultTypes.getImplicitStaticImports()) {
			implicitStaticImports.add(converter.toIQLString(type));
		}
		return implicitStaticImports;
	}


	@Override
	public boolean isSourceAttribute(String sourceName, String attrName) {
		ILogicalOperator source = sources.get(sourceName.toLowerCase());
		if (source != null) {
			for (SDFAttribute attr : source.getOutputSchema().getAttributes()) {
				if (attr.getAttributeName().equalsIgnoreCase(attrName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void addSource(String sourceName, ILogicalOperator source, IQLClass sourceClass) {
		sources.put(sourceName.toLowerCase(), source);
		sourceTypes.put(sourceName.toLowerCase(), sourceClass);
	}

	@Override
	public void removeSource(String sourceName) {
		sources.remove(sourceName.toLowerCase());	
		sourceTypes.remove(sourceName.toLowerCase());
	}
	
	@Override
	public Collection<String> getSources() {
		return sources.keySet();
	}	
	
	@Override
	public Collection<IQLClass> getSourceTypes() {
		return sourceTypes.values();
	}

	@Override
	public void addOperator(IOperatorBuilder opBuilder, IQLClass operatorType,  Map<String, String[]> parameterNames, Map<String, Parameter> parameterTypes) {
		operators.put(opBuilder.getName().toLowerCase(), opBuilder);
		operatorParameterNames.put(opBuilder.getName().toLowerCase(), parameterNames);
		operatorParameterTypes.put(opBuilder.getName().toLowerCase(), parameterTypes);
		operatorTypes.put(opBuilder.getName().toLowerCase(), operatorType);
	}
	
	@Override
	public Collection<IQLClass> getOperatorTypes() {
		return operatorTypes.values();
	}
		
	@Override
	public boolean isOperatorParameter(String operatorName, String parameterName) {
		return getOperatorParameter(operatorName, parameterName) != null;
	}

	@Override
	public Class<? extends ILogicalOperator> getLogicalOperator(String operatorName) {
		IOperatorBuilder builder = operators.get(operatorName.toLowerCase());
		if (builder != null) {
			return builder.getOperatorClass();
		}
		return null;
	}
	
	@Override
	public Parameter getOperatorParameterType(String operatorName, String parameterName) {
		Map<String, Parameter> parameters = operatorParameterTypes.get(operatorName.toLowerCase());
		if (parameters != null) {
			return parameters.get(parameterName.toLowerCase());
		}
		return null;
	}
	
	@Override
	public IParameter<?> getOperatorParameter(String operatorName, String parameterName) {
		IOperatorBuilder builder = operators.get(operatorName.toLowerCase());
		if (builder != null) {
			for (IParameter<?> parameter  : builder.getParameters()) {
				if (parameter.getName().equalsIgnoreCase(parameterName)) {
					return parameter;
				}
			}
		}
		return null;
	}

	@Override
	public String getParameterPropertyName(String parameter, String operatorName) {		
		Map<String, String[]> properties = operatorParameterNames.get(operatorName.toLowerCase());
		if (properties != null) {
			return properties.get(parameter.toLowerCase())[0];
		}
		return null;
	}
	
	@Override
	public String getParameterGetterName(String parameter, String operatorName) {
		Map<String, String[]> properties = operatorParameterNames.get(operatorName.toLowerCase());
		if (properties != null) {
			return properties.get(parameter.toLowerCase())[1];
		}
		return null;
	}
	
	@Override
	public String getParameterSetterName(String parameter, String operatorName) {
		Map<String, String[]> properties = operatorParameterNames.get(operatorName.toLowerCase());
		if (properties != null) {
			return properties.get(parameter.toLowerCase())[2];
		}
		return null;
	}

	@Override
	public boolean isParameter(String parameter, String operatorName) {
		Map<String, String[]> properties = operatorParameterNames.get(operatorName.toLowerCase());
		if (properties != null) {
			return properties.get(parameter.toLowerCase()) != null;
		}
		return false;
	}

	@Override
	public void setParameterValueTypes(Set<Class<?>> parameterValueTypes) {
		this.parameterValueTypes.addAll(parameterValueTypes);
	}

	@Override
	public Collection<IOperatorBuilder> getOperators() {
		return operators.values();
	}

	@Override
	public IOperatorBuilder getOperatorBuilder(String name) {
		return this.operators.get(name.toLowerCase());
	}
	
}
