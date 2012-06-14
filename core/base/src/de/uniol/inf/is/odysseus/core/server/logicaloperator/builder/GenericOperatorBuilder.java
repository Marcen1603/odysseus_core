package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.USAGE;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;

public class GenericOperatorBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = 1L;
	private Class<? extends ILogicalOperator> operatorClass;
	private Map<Method, IParameter<?>> parameterMap;
	private ILogicalOperator operator;
	private Map<Parameter, Method> parameterAnnotationMap;

	public GenericOperatorBuilder(
			Class<? extends ILogicalOperator> operatorClass,
			Map<Parameter, Method> parameterMap, int minPortCount,
			int maxPortCount) {
		super(minPortCount, maxPortCount);
		this.operatorClass = operatorClass;
		this.parameterAnnotationMap = parameterMap;
		initParameters();
	}

	public GenericOperatorBuilder(GenericOperatorBuilder builder) {
		this(builder.operatorClass, builder.parameterAnnotationMap, builder
				.getMinInputOperatorCount(), builder.getMaxInputOperatorCount());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initParameters() {
		this.parameterMap = new HashMap<Method, IParameter<?>>();
		for (Map.Entry<Parameter, Method> curParameterEntry : parameterAnnotationMap
				.entrySet()) {
			Parameter parameterAnnotation = curParameterEntry.getKey();
			Method method = curParameterEntry.getValue();
			Class<? extends IParameter> parameterType = parameterAnnotation
					.type();
			IParameter<?> parameter;
			try {
				parameter = parameterType.newInstance();
				if (parameter instanceof EnumParameter) {
					createEnumParameter(method, parameter);
				}

				String name = parameterAnnotation.name();
				// remove 'set' from set method to get the property name,
				// if no explicit name was set
				name = (name.isEmpty() ? method.getName().substring(3) : name)
						.toUpperCase();
				parameter.setName(name);
				REQUIREMENT requirement = parameterAnnotation.optional() ? REQUIREMENT.OPTIONAL
						: REQUIREMENT.MANDATORY;

				parameter.setRequirement(requirement);
				
				USAGE usage = parameterAnnotation.deprecated() ? USAGE.DEPRECATED : USAGE.RECENT;
				
				parameter.setUsage(usage);

				if (parameterAnnotation.isList()) {
					if (!List.class
							.isAssignableFrom(method.getParameterTypes()[0])) {
						throw new IllegalParameterException("Parameter '"
								+ name + "' is incompatible to method '"
								+ method.getName() + "'");
					}
					parameter = new ListParameter(parameter);
					parameter.setName(name);
					parameter.setRequirement(requirement);
				}

				parameterMap.put(method, parameter);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		addParameters(parameterMap.values().toArray(new IParameter<?>[0]));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void createEnumParameter(Method method, IParameter<?> parameter) {
		Class<?>[] methodParameters = method.getParameterTypes();
		if (methodParameters == null || methodParameters.length != 1
				|| !methodParameters[0].isEnum()) {
			throw new IllegalParameterException("method '" + method.getName()
					+ "' of class '"
					+ method.getDeclaringClass().getSimpleName()
					+ "' can't be written by an EnumParameter");
		}
		((EnumParameter) parameter)
				.setEnum((Class<? extends Enum>) methodParameters[0]);
	}

	@Override
	protected boolean internalValidation() {
		try {
			if (this.operator != null) {
				return true;
			}
			ILogicalOperator op = operatorClass.newInstance();
			initOp(op);
			boolean isValid = op.isValid();
			if (isValid) {
				this.operator = op;
			} else {
				this.addErrors(((AbstractLogicalOperator)op).getErrors());
				op.unsubscribeFromAllSources();
				this.operator = null;
			}
			return isValid;
		} catch (Exception e) {
			// TODO: REMOVE
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return this.operator;
	}

	private void initOp(ILogicalOperator op) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < getInputOperatorCount(); ++i) {
			ILogicalOperator inputOperator = getInputOperator(i);
			op.subscribeToSource(inputOperator, i,
					getInputOperatorItem(i).outputPort,
					inputOperator.getOutputSchema());
		}
		for (Map.Entry<Method, IParameter<?>> parameterEntry : parameterMap
				.entrySet()) {
			IParameter<?> parameter = parameterEntry.getValue();
			if (parameter.hasValue()) {
				parameterEntry.getKey().invoke(op, parameter.getValue());
			}
		}
		op.initialize();
	}

	@Override
	public GenericOperatorBuilder cleanCopy() {
		return new GenericOperatorBuilder(this);
	}
}
