package de.uniol.inf.is.odysseus.mep.functions;

import java.io.Serializable;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IStatefulFunction;

public class GroupedCounterFunction extends AbstractFunction<Long> implements IStatefulFunction {

	private static final long serialVersionUID = -568141993809029277L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "GroupedCounter";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.LONG;

	/**
	 * Creates a new MEP function.
	 */
	public GroupedCounterFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	/**
	 * Creates a new MEP function.
	 */
	public GroupedCounterFunction(GroupedCounterFunction other) {
		this();
		this.counters = other.counters;
	}

	// Counters mapped to key (argument of MEP function)
	private HashMap<Object, Long> counters = new HashMap<>();

	@Override
	public Long getValue() {
		Object key = getInputValue(0);
		if (key == null) {
			return null;
		}

		long retVal = 0;
		if (counters.containsKey(key)) {
			retVal = counters.get(key) + 1;
		}

		counters.put(key, retVal);
		return retVal;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

	@Override
	public Serializable getState() {
		return counters;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable state) {
		counters = (HashMap<Object, Long>) state;
	}

	@Override
	public IMepExpression<Long> clone() {
		return new GroupedCounterFunction(this);
	}

}