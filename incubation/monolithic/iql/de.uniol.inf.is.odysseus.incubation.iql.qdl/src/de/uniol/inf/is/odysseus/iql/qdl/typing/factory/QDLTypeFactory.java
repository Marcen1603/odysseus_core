package de.uniol.inf.is.odysseus.iql.qdl.typing.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.AbstractIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFactory;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceObserver;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLSource;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;



@Singleton
public class QDLTypeFactory extends AbstractIQLTypeFactory<IQDLTypeUtils, QDLServiceObserver> implements IQDLTypeFactory {
	

	private Map<String, ILogicalOperator> sources = new HashMap<String, ILogicalOperator>();	
	private Map<String, IQLClass> sourceTypes = new HashMap<String, IQLClass>();

	private Map<String, IOperatorBuilder> operators = new HashMap<String, IOperatorBuilder>();
	private Map<String, IQLClass> operatorTypes = new HashMap<String, IQLClass>();
	private Map<String, Map<String, String[]>> operatorParameterNames = new HashMap<>();
	private Map<String, Map<String, Parameter>> operatorParameterTypes = new HashMap<>();

	private Set<Class<?>> parameterValueTypes = new HashSet<>();
	
	@Inject
	public QDLTypeFactory(IQDLTypeUtils typeUtils, QDLServiceObserver serviceObserver) {
		super(typeUtils, serviceObserver);
	}
	
	@Override
	public Collection<Bundle> getDependencies() {
		Collection<Bundle> bundles =super.getDependencies();
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.qdl"));
		return bundles;
	}
	
	@Override
	public String getFileExtension() {
		return "qdl";
	}

	@Override
	protected IQLModel createCleanSystemFile() {		
		return QDLFactory.eINSTANCE.createQDLModel();
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
			implicitImports.add(parameterValueType.getCanonicalName());
		}	
		implicitImports.addAll(QDLDefaultTypes.getImplicitImports());	
		return implicitImports;
	}
	
	@Override
	public Collection<String> createImplicitStaticImports() {
		Collection<String> implicitStaticImports = super.createImplicitStaticImports();
		for (Class<?> c : QDLDefaultTypes.getImplicitStaticImports()) {
			implicitStaticImports.add(c.getCanonicalName());
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

	@Override
	public boolean isOperator(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLClass) {
			JvmDeclaredType clazz = (JvmDeclaredType) typeUtils.getInnerType(typeRef, true);
			if (clazz.getExtendedClass() != null) {
				JvmTypeReference superClass = typeUtils.createTypeRef(DefaultQDLOperator.class, getSystemResourceSet());
				return typeUtils.getLongName(clazz.getExtendedClass(), false).equals(typeUtils.getLongName(superClass, false));
			}
		} 
		return false;	
	}

	@Override
	public boolean isSource(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLClass) {
			JvmDeclaredType clazz = (JvmDeclaredType) typeUtils.getInnerType(typeRef, true);
			if (clazz.getExtendedClass() != null) {
				JvmTypeReference superClass = typeUtils.createTypeRef(DefaultQDLSource.class, getSystemResourceSet());
				return typeUtils.getLongName(clazz.getExtendedClass(), false).equals(typeUtils.getLongName(superClass, false));	
			}
		} 
		return false;
	}
	
}