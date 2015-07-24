package de.uniol.inf.is.odysseus.iql.qdl.typing;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.AbstractIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFactory;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.IQDLSource;


@Singleton
public class QDLTypeFactory extends AbstractIQLTypeFactory {
	
	private Map<String, ILogicalOperator> sources = new HashMap<String, ILogicalOperator>();
	private Map<String, IOperatorBuilder> operators = new HashMap<String, IOperatorBuilder>();
	private Map<IOperatorBuilder, Map<String, String[]>> operatorParameterNames = new HashMap<>();
	private Set<Class<?>> parameterValueTypes = new HashSet<>();
	
	@Override
	public String getFileExtension() {
		return "qdl";
	}

	@Override
	protected IQLFile createCleanSystemFile() {
		
		return QDLFactory.eINSTANCE.createQDLFile();
	}
	@SuppressWarnings("restriction")
	@Override
	public Collection<JvmType> createVisibleTypes(Resource context) {
		Collection<JvmType> types = super.createVisibleTypes(context);
		for (Class<?> parameterValueType : parameterValueTypes) {
			JvmType t = typeReferences.findDeclaredType(parameterValueType.getCanonicalName(), context);
			if (t != null) {
				types.add(t);
			}
		}
		return types;
	}
	
	@Override
	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = super.createImplicitImports();
		for (Class<?> parameterValueType : parameterValueTypes) {
			implicitImports.add(parameterValueType.getCanonicalName());
		}	
		return implicitImports;
	}

	
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

	public void addSource(ILogicalOperator source) {
		sources.put(source.getName().toLowerCase(), source);
	}

	public void removeSource(ILogicalOperator source) {
		sources.remove(source.getName().toLowerCase());		
	}

	public void addOperator(IOperatorBuilder opBuilder, Map<String, String[]> parameterNames) {
		operators.put(opBuilder.getName().toLowerCase(), opBuilder);
		operatorParameterNames.put(opBuilder, parameterNames);
	}
	
	
	
	public boolean isOperatorParameter(String operatorName, String parameterName) {
		return getOperatorParameter(operatorName, parameterName) != null;
	}

	
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

	public String getParameterPropertyName(String parameter, String operatorName) {
		IOperatorBuilder builder = operators.get(operatorName.toLowerCase());
		if (builder != null) {
			return operatorParameterNames.get(builder).get(parameter.toLowerCase())[0];
		}
		return null;
	}
	
	public String getParameterGetterName(String parameter, String operatorName) {
		IOperatorBuilder builder = operators.get(operatorName.toLowerCase());
		if (builder != null) {
			return operatorParameterNames.get(builder).get(parameter.toLowerCase())[1];
		}
		return null;
	}
	
	public String getParameterSetterName(String parameter, String operatorName) {
		IOperatorBuilder builder = operators.get(operatorName.toLowerCase());
		if (builder != null) {
			return operatorParameterNames.get(builder).get(parameter.toLowerCase())[2];
		}
		return null;
	}

	public boolean isParameter(String parameter, String operatorName) {
		IOperatorBuilder builder = operators.get(operatorName.toLowerCase());
		if (builder != null) {
			Map<String, String[]> parameters = operatorParameterNames.get(builder);
			return parameters.get(parameter.toLowerCase()) != null;
		}
		return false;
	}
	
	@Override
	public Collection<Bundle> getDependencies(IQLFile file) {
		Collection<Bundle> bundles = super.getDependencies(file);
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.slf4j"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.core"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.core.server"));
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.qdl"));
		return bundles;
	}

	public boolean isOperator(JvmTypeReference typeRef) {
		if (getInnerType(typeRef, true) instanceof IQLClass) {
			JvmGenericType clazz = (JvmGenericType) getInnerType(typeRef, true);
			JvmTypeReference opInterface = getTypeRef(IQDLOperator.class);
			for (JvmTypeReference i : clazz.getExtendedInterfaces()) {
				if (getLongName(i, false).equals(getLongName(opInterface, false))) {
					return true;
				}
			}
		} 
		return false;	
	}

	public boolean isSource(JvmTypeReference typeRef) {
		if (getInnerType(typeRef, true)  instanceof IQLClass) {
			JvmGenericType clazz = (JvmGenericType) getInnerType(typeRef, true) ;
			JvmTypeReference sourceInterface = getTypeRef(IQDLSource.class);
			for (JvmTypeReference i : clazz.getExtendedInterfaces()) {
				if (getLongName(i, false).equals(getLongName(sourceInterface, false))) {
					return true;
				}
			}
		} 
		return false;
	}

	public void setParameterValueTypes(Set<Class<?>> parameterValueTypes) {
		this.parameterValueTypes.addAll(parameterValueTypes);
	}

	public Collection<IOperatorBuilder> getOperators() {
		return operators.values();
	}

	
}
