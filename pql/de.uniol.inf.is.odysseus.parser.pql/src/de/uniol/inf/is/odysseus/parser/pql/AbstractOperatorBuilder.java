package de.uniol.inf.is.odysseus.parser.pql;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractOperatorBuilder implements IOperatorBuilder {

	private Set<Parameter<?>> parameters;

	public void setParameters(Parameter<?>... parameters) {
		this.parameters = new HashSet<Parameter<?>>();
		for (Parameter<?> parameter : parameters) {
			if (this.parameters.contains(parameter)) {
				throw new IllegalArgumentException(
						"duplicate parameter definition: "
								+ parameter.getName());
			}
			this.parameters.add(parameter);
		}
	}
	
	@Override
	public Set<Parameter<?>> getParameters() {
		return Collections.unmodifiableSet(this.parameters);
	}

	protected void createSubscriptions(ILogicalOperator operator,
			List<ILogicalOperator> inputOps, int inputPortCount, SDFAttributeList schema) {
		checkInputSize(inputOps, inputPortCount);
		int i = 0;
		for (ILogicalOperator op : inputOps) {
			operator.subscribeToSource(op, i++, 0, schema);
		}
	}

	protected void checkInputSize(List<ILogicalOperator> inputOps,
			int inputPortCount) {
		if (inputOps.size() != inputPortCount) {
			throw new IllegalArgumentException("operator expects "
					+ inputPortCount + " inputs, but got " + inputOps.size());
		}
	}

	@Override
	final public ILogicalOperator createOperator(
			Map<String, Object> parameters, List<ILogicalOperator> inputOps) {
		initOperatorCreation(parameters, inputOps);
		initParameters(parameters);
		return createOperator(inputOps);
	}

	protected void initOperatorCreation(Map<String, Object> parameters2,
			List<ILogicalOperator> inputOps) {
		IAttributeResolver attributeResolver = buildAttributeResolver(inputOps);
		for(Parameter<?> parameter : parameters) {
			if (parameter instanceof PredicateParameter){
				((PredicateParameter) parameter).setAttributeResolver(attributeResolver);
			}
		}
	}

	private IAttributeResolver buildAttributeResolver(
			List<ILogicalOperator> inputOps) {
		SDFAttributeList attributes = new SDFAttributeList();
		for(ILogicalOperator op : inputOps) {
			attributes.addAll(op.getOutputSchema());
		}
		
		return new DirectAttributeResolver(attributes);
	}

	protected abstract ILogicalOperator createOperator(
			List<ILogicalOperator> inputOps);

	private void initParameters(Map<String, Object> parameters) {
		Set<Parameter<?>> params = getParameters();
		for (Parameter<?> parameter : params) {
			String parameterName = parameter.getName();
			boolean hasParameter = parameters.containsKey(parameterName);
			if (!hasParameter) {
				parameter.setNoValueAvailable();
				if (parameter.isMandatory()) {
					throw new IllegalArgumentException(
							"missing mandatory parameter: " + parameterName);
				}
			} else {
				Object value = parameters.get(parameterName);
				parameter.setValueOf(value);
			}
		}
	}
	
}
