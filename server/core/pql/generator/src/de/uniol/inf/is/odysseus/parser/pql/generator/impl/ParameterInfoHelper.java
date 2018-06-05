package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;

public class ParameterInfoHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ParameterInfoHelper.class);

	public static void prepareParameterInfos(Collection<ILogicalOperator> operators) {
		for (ILogicalOperator operator : operators) {
			try {
				prepareParameterInfos(operator);
			} catch (IntrospectionException | InstantiationException | IllegalArgumentException e) {
				LOG.error("Could not prepare parameter infos of operator {}", operator, e);
			}
		}
	}

	public static void prepareParameterInfos(ILogicalOperator operator) throws IntrospectionException, InstantiationException {
		Class<? extends ILogicalOperator> operatorClass = operator.getClass();

		BeanInfo beanInfo = Introspector.getBeanInfo(operatorClass, Object.class);
		for (PropertyDescriptor curProperty : beanInfo.getPropertyDescriptors()) {
			Method writeMethod = curProperty.getWriteMethod();
			Method readMethod = curProperty.getReadMethod();

			if (writeMethod != null && writeMethod.isAnnotationPresent(Parameter.class)) {
				Parameter parameterAnnotation = writeMethod.getAnnotation(Parameter.class);
				// TODO Make the skipping of deprecated parameters adjustable. M.B.
				if (parameterAnnotation.deprecated()) {
					continue;
				}

				String parameterName = parameterAnnotation.name();
				if (Strings.isNullOrEmpty(parameterName)) {
					parameterName = curProperty.getName();
				}

				if (readMethod == null) {
					LOG.error("No getter for parameter '" + parameterName + "' of operator " + operatorClass.getName());
					continue;
				}

				@SuppressWarnings("unchecked")
				Class<? extends IParameter<?>> parameterType = (Class<? extends IParameter<?>>) parameterAnnotation.type();
				IParameter<?> parameter = null;
				try {
					parameter = parameterType.newInstance();
				} catch (IllegalAccessException e1) {
					LOG.error("Could not instantial paramatertype {}", parameterType, e1);
					continue;
				}
				Object parameterValue = null;
				try {
					parameterValue = readMethod.invoke(operator, (Object[]) null);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					LOG.error("Could not get parameter value of parameter '{}' of operator {}", new Object[] { parameterName, operator, e1 });
					continue;
				}

				if (parameterValue == null) {
					continue;
				}

				if (parameterAnnotation.isMap()) {
					LOG.error("Parameters of type map are not supported, yet. Here, parameter is {} of operator {}.", parameterName, operator);
					continue;
				}

				String pqlString = null;
				if (parameterAnnotation.isList()) {
					List<?> parameterValueList = (List<?>) parameterValue;
					if (parameterValueList.isEmpty()) {
						continue;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					ListParameter<?> listParameter = new ListParameter(parameter);
					listParameter.setInputValue(parameterValue);

					List<SDFSchema> inputSchema = new LinkedList<>();
					for (LogicalSubscription sub : operator.getSubscribedToSource()) {
						inputSchema.add(sub.getSource().getOutputSchema(sub.getSourceOutPort()));
					}
					DirectAttributeResolver attributeResolver = new DirectAttributeResolver(inputSchema);
					listParameter.setAttributeResolver(attributeResolver);
					if (!listParameter.validate()) { // invokes internal
														// assignment
						LOG.error("Parameter '{}' in operator {} is not valid for value '{}'", new Object[] { parameterName, operatorClass.getName(), parameterValue });
						LOG.error("Following errors occured during validation:");
						for (String e : parameter.getErrors()) {
							LOG.error("", e);
						}
						continue;
					}

					pqlString = listParameter.getPQLString();


				} else {
					parameter.setInputValue(parameterValue);

					List<SDFSchema> inputSchema = new LinkedList<>();
					for (LogicalSubscription sub : operator.getSubscribedToSource()) {
						inputSchema.add(sub.getSource().getOutputSchema(sub.getSourceOutPort()));
					}
					DirectAttributeResolver attributeResolver = new DirectAttributeResolver(inputSchema);

					parameter.setAttributeResolver(attributeResolver);
					if (!parameter.validate()) { // invokes internal assignment
						LOG.error("Parameter '{}' in operator {} is not valid for value '{}'", new Object[] { parameterName, operatorClass.getName(), parameterValue });
						LOG.error("Following errors occured during validation:");
						for (String e : parameter.getErrors()) {
							LOG.error(e);
						}
						continue;
					}

					pqlString = parameter.getPQLString();
				}

				if (!parameterAnnotation.optional()) {
					operator.addParameterInfo(parameterName.toUpperCase(), pqlString);
					LOG.debug("Prepared parameter info for {}: [{}] {} --> {}", new Object[] { operator, parameterAnnotation.keytype().getSimpleName(), parameterName, pqlString != null ? pqlString : "<null>" });
				} else {
					if (!Strings.isNullOrEmpty(pqlString)) {
						operator.addParameterInfo(parameterName.toUpperCase(), pqlString);
						LOG.debug("Prepared parameter info for {}: [{}] {} --> {}", new Object[] { operator, parameterAnnotation.keytype().getSimpleName(), parameterName, pqlString != null ? pqlString : "<null>" });
					}
				}
			}
		}
	}

}
