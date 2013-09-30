/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

	public GenericOperatorBuilder(Class<? extends ILogicalOperator> operatorClass, String name, Map<Parameter, Method> parameterMap, int minPortCount, int maxPortCount, String doc, String[] categories) {		
		super(name, minPortCount, maxPortCount, doc, categories);		
		this.operatorClass = operatorClass;
		this.parameterAnnotationMap = parameterMap;
		initParameters();
	}

	public GenericOperatorBuilder(GenericOperatorBuilder builder) {
		this(builder.operatorClass, builder.getName(), builder.parameterAnnotationMap, builder.getMinInputOperatorCount(), builder.getMaxInputOperatorCount(), builder.getDoc(), builder.getCategories());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initParameters() {
		this.parameterMap = new HashMap<Method, IParameter<?>>();
		for (Map.Entry<Parameter, Method> curParameterEntry : parameterAnnotationMap.entrySet()) {
			Parameter parameterAnnotation = curParameterEntry.getKey();
			Method method = curParameterEntry.getValue();
			Class<? extends IParameter> parameterType = parameterAnnotation.type();
			Class<? extends IParameter> parameterKeyType = parameterAnnotation.keytype();
			IParameter<?> parameter;
			try {
				parameter = parameterType.newInstance();
				if (parameter instanceof EnumParameter) {
					createEnumParameter(method, parameter);
				}

				String name = parameterAnnotation.name();
				String doc = parameterAnnotation.doc();
				String possibleValueMethod = parameterAnnotation.possibleValues();
				boolean possibleValuesAreDynamic = parameterAnnotation.possibleValuesAreDynamic();
				
				// remove 'set' from set method to get the property name,
				// if no explicit name was set
				name = (name.isEmpty() ? method.getName().substring(3) : name).toUpperCase();
				parameter.setName(name);
				parameter.setDoc(doc);
				parameter.setPossibleValueMethod(possibleValueMethod);	
				parameter.setPossibleValuesAreDynamic(possibleValuesAreDynamic);
				REQUIREMENT requirement = parameterAnnotation.optional() ? REQUIREMENT.OPTIONAL : REQUIREMENT.MANDATORY;

				parameter.setRequirement(requirement);

				USAGE usage = parameterAnnotation.deprecated() ? USAGE.DEPRECATED : USAGE.RECENT;

				parameter.setUsage(usage);

//				if(parameterAnnotation.isList() && parameterAnnotation.isMap()){
//					throw new IllegalParameterException("Parameter '" + name + "' can be either a list or a map");
//				}
				
				if (parameterAnnotation.isList()) {
					if (!List.class.isAssignableFrom(method.getParameterTypes()[0])) {
						if(!parameterAnnotation.isMap()){
							throw new IllegalParameterException("Parameter '" + name + "' is incompatible to method '" + method.getName() + "'");
						}
					}
					parameter = new ListParameter(parameter);
					parameter.setName(name);
					parameter.setRequirement(requirement);
				}
				if(parameterAnnotation.isMap()){
					if (!Map.class.isAssignableFrom(method.getParameterTypes()[0])) {
						throw new IllegalParameterException("Parameter '" + name + "' is incompatible to method '" + method.getName() + "'");
					}
					IParameter<?> keyParameter = parameterKeyType.newInstance();
					keyParameter.setRequirement(requirement);
					keyParameter.setUsage(usage);
					keyParameter.setName(name);
					parameter = new MapParameter(keyParameter, parameter);					
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
		if (methodParameters == null || methodParameters.length != 1 || !methodParameters[0].isEnum()) {
			throw new IllegalParameterException("method '" + method.getName() + "' of class '" + method.getDeclaringClass().getSimpleName()
					+ "' can't be written by an EnumParameter");
		}
		((EnumParameter) parameter).setEnum((Class<? extends Enum>) methodParameters[0]);
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
				this.addErrors(((AbstractLogicalOperator) op).getErrors());
				op.unsubscribeFromAllSources();
				this.operator = null;
			}
			return isValid;
		} catch (Exception e) {
			// TODO: REMOVE
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return this.operator;
	}

	private void initOp(ILogicalOperator op) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < getInputOperatorCount(); ++i) {
			ILogicalOperator inputOperator = getInputOperator(i);
			op.subscribeToSource(inputOperator, i, getInputOperatorItem(i).outputPort, inputOperator.getOutputSchema(getInputOperatorItem(i).outputPort));
		}
		for (Map.Entry<Method, IParameter<?>> parameterEntry : parameterMap.entrySet()) {
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder#getOperatorClass()
	 */
	@Override
	public Class<? extends ILogicalOperator> getOperatorClass() {
		return this.operatorClass;
	}	
}
