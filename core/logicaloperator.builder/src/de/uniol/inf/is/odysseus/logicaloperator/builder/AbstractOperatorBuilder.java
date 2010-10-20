package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractOperatorBuilder implements IOperatorBuilder {

	private Set<IParameter<?>> parameters;
	private List<Exception> errors;
	private int minPortCount;
	private int maxPortCount;
	private Map<Integer, InputOperatorItem> inputOperators;
	private User caller;
	
	public AbstractOperatorBuilder(int minPortCount, int maxPortCount) {
		if (minPortCount > maxPortCount) {
			throw new IllegalArgumentException(
					"minimum number of ports may not be higher than maximum number");
		}
		this.minPortCount = minPortCount;
		this.maxPortCount = maxPortCount;
		this.parameters = new HashSet<IParameter<?>>();
		this.errors = new ArrayList<Exception>();
		this.inputOperators = new TreeMap<Integer, InputOperatorItem>();
	}

	public void setParameters(IParameter<?>... parameters) {
		for (IParameter<?> parameter : parameters) {
			if (this.parameters.contains(parameter)) {
				throw new IllegalArgumentException(
						"duplicate parameter definition: "
								+ parameter.getName());
			}
			this.parameters.add(parameter);
		}
	}
	
	protected void addError(Exception e) {
		this.errors.add(e);
	}

	@Override
	public List<Exception> getErrors() {
		return this.errors;
	}

	@Override
	public int getMaxInputOperatorCount() {
		return maxPortCount;
	}

	@Override
	public int getMinInputOperatorCount() {
		return minPortCount;
	}
	
	public void setCaller(User caller){
		this.caller = caller;
	}
	
	public User getCaller() {
		return caller;
	}

	@Override
	public boolean validate() {
		this.errors.clear();
		boolean isValid = true;

		// check input operators
		int inputOperatorCount = this.inputOperators.size();
		if (inputOperatorCount < minPortCount) {
			isValid = false;
			this.errors.add(new MissingInputsException(this.minPortCount));
		}
		// > maxPortCount cannot occur, as this is checked in setInputOperator
		// TODO do input operators have to be contiguous with regard to
		// inputPorts
		// e.g. is input port 0, 2, 3 set, but 1 not set allowed?

		// check parameters
		for (IParameter<?> parameter : getParameters()) {
			SDFAttributeList schema = new SDFAttributeList();
			for (InputOperatorItem opItem : inputOperators.values()) {
				schema = SDFAttributeList.union(schema, opItem.operator
						.getOutputSchema());
			}
			IAttributeResolver attributeResolver = new DirectAttributeResolver(
					schema);
			parameter.setAttributeResolver(attributeResolver);
			if (!parameter.validate()) {
				isValid = false;
				this.errors.addAll(parameter.getErrors());
			}
		}

		if (isValid) {
			return internalValidation();
		}
		return isValid;
	}

	abstract protected boolean internalValidation();

	@Override
	public Set<IParameter<?>> getParameters() {
		return Collections.unmodifiableSet(this.parameters);
	}

	protected void checkInputSize(List<ILogicalOperator> inputOps,
			int inputPortCount) {
		if (inputOps.size() != inputPortCount) {
			throw new IllegalArgumentException("operator expects "
					+ inputPortCount + " inputs, but got " + inputOps.size());
		}
	}

	@Override
	final public ILogicalOperator createOperator() {
		if (!validate()) {
			throw new RuntimeException("validation error");
		}

		ILogicalOperator op = createOperatorInternal();
		for (Map.Entry<Integer, InputOperatorItem> curEntry : this.inputOperators
				.entrySet()) {
			InputOperatorItem curInputOperatorItem = curEntry.getValue();
			ILogicalOperator curInputOperator = curInputOperatorItem.operator;
			op.subscribeToSource(curInputOperator, curEntry.getKey(),
					curInputOperatorItem.outputPort, curInputOperator
							.getOutputSchema());
		}

		return op;
	}

	abstract protected ILogicalOperator createOperatorInternal();

	protected void initOperatorCreation(Map<String, Object> parameters2,
			List<ILogicalOperator> inputOps) {
		IAttributeResolver attributeResolver = buildAttributeResolver(inputOps);
		for (IParameter<?> parameter : parameters) {
			parameter.setAttributeResolver(attributeResolver);
		}
	}

	private IAttributeResolver buildAttributeResolver(
			List<ILogicalOperator> inputOps) {
		SDFAttributeList attributes = new SDFAttributeList();
		for (ILogicalOperator op : inputOps) {
			attributes.addAll(op.getOutputSchema());
		}

		return new DirectAttributeResolver(attributes);
	}

	@Override
	public void setInputOperator(int inputPort, ILogicalOperator operator,
			int outputPort) {
		if (this.maxPortCount <= inputPort) {
			throw new IllegalArgumentException("illegal input port: " + inputPort);
		}
		if( operator != null ) {
			this.inputOperators.put(inputPort, new InputOperatorItem(operator,
					outputPort));
		} else {
			this.inputOperators.remove(inputPort);
		}
	}
	
	public ILogicalOperator getInputOperator(int inputPort){
		return this.inputOperators.get(inputPort).operator;
	}
	
	public boolean hasInputOperator(int inputPort) {
		return this.inputOperators.containsKey(inputPort);
	}
	
	public int getInputOperatorCount() {
		return this.inputOperators.size();
	}

}
