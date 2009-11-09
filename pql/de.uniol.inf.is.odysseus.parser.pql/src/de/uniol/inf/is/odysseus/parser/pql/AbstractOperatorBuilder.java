package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractOperatorBuilder implements IOperatorBuilder {
	@SuppressWarnings("unchecked")
	protected <T> T getParameter(Map<String, Object> parameters,
			String parameterName, Class<T> parameterType) {
		parameterName=parameterName.toUpperCase();
		if (!parameters.containsKey(parameterName)) {
			throw new IllegalArgumentException("missing parameter: "
					+ parameterName);
		}
		Object value = parameters.get(parameterName);
		if (parameterType.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("wrong type for parameter '"
					+ parameterName + "', " + parameterType.getSimpleName()
					+ " expected");
		}
		return (T) value;
	}

	protected void createSubscriptions(ILogicalOperator operator,
			List<ILogicalOperator> inputOps, int inputPortCount, SDFAttributeList schema) {
		checkInputSize(inputOps, inputPortCount);
		int i = 0;
		for (ILogicalOperator op : inputOps) {
			operator.subscribeTo(op, i++, 0, schema);
		}
	}

	private void checkInputSize(List<ILogicalOperator> inputOps,
			int inputPortCount) {
		if (inputOps.size() != inputPortCount) {
			throw new IllegalArgumentException("operator expects "
					+ inputPortCount + " inputs, but got " + inputOps.size());
		}
	}
}
