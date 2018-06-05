package de.uniol.inf.is.odysseus.iql.basic.service;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;

public class ParameterPair {
	final private Class<? extends IParameter<?>> parameterType;
	final private Class<?> parameterValueType;

	public ParameterPair(Class<? extends IParameter<?>> parameterType, Class<?> parameterValueType) {
		this.parameterType = parameterType;
		this.parameterValueType = parameterValueType;
	}

	public Class<? extends IParameter<?>> getParameterType() {
		return parameterType;
	}

	public Class<?> getParameterValueType() {
		return parameterValueType;
	}
	
	
}
