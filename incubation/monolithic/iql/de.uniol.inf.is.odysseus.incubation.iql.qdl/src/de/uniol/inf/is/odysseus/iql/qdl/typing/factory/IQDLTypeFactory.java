package de.uniol.inf.is.odysseus.iql.qdl.typing.factory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;

public interface IQDLTypeFactory extends IIQLTypeFactory {

	boolean isSourceAttribute(String sourceName, String attrName);

	void addSource(String sourceName, ILogicalOperator source,
			IQLClass sourceClass);

	void removeSource(String sourceName);

	Collection<String> getSources();

	Collection<IQLClass> getSourceTypes();

	void addOperator(IOperatorBuilder opBuilder, IQLClass operatorType,
			Map<String, String[]> parameterNames,
			Map<String, Parameter> parameterTypes);

	Collection<IQLClass> getOperatorTypes();

	boolean isOperatorParameter(String operatorName, String parameterName);

	Class<? extends ILogicalOperator> getLogicalOperator(String operatorName);

	Parameter getOperatorParameterType(String operatorName, String parameterName);

	IParameter<?> getOperatorParameter(String operatorName, String parameterName);

	String getParameterPropertyName(String parameter, String operatorName);

	String getParameterGetterName(String parameter, String operatorName);

	String getParameterSetterName(String parameter, String operatorName);

	boolean isParameter(String parameter, String operatorName);

	void setParameterValueTypes(Set<Class<?>> parameterValueTypes);

	Collection<IOperatorBuilder> getOperators();

	IOperatorBuilder getOperatorBuilder(String name);

	boolean isOperator(JvmTypeReference typeRef);

	boolean isSource(JvmTypeReference typeRef);

}
